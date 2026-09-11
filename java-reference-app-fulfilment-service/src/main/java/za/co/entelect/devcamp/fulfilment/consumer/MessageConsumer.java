package za.co.entelect.devcamp.fulfilment.consumer;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.configuration.RabbitConfig;
import za.co.entelect.devcamp.fulfilment.enums.OrderStatusEnum;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaService;
import za.co.entelect.devcamp.fulfilment.interfaces.IFraudCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IProductService;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;
import za.co.entelect.devcamp.fulfilment.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.fulfilment.responses.OrderResponse;
@Slf4j
@Service
public class MessageConsumer {

    public final ICreditCheckService creditCheckService;
    public final IDhaService dhaService;
    public final IKycCheckService kycCheckService;
    public final IFraudCheckService fraudCheckService;
    public final IProductService productService;

    public MessageConsumer(ICreditCheckService creditCheckService,
                           IDhaService dhaService,
                           IKycCheckService kycCheckService,
                           IFraudCheckService fraudCheckService,
                           IProductService productService)
    {
        this.creditCheckService = creditCheckService;
        this.dhaService = dhaService;
        this.kycCheckService = kycCheckService;
        this.fraudCheckService = fraudCheckService;
        this.productService = productService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE,   containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(FulfilmentRequest fulfilmentRequest) {
        int retryCount = RetrySynchronizationManager
                .getContext()
                .getRetryCount();
        try {

            log.info("-------------Processing FulfilmentRequest - Attempt: " + (retryCount + 1));

            log.info("----------Fulfilment Checks---------------");
            boolean passed = false;

            switch (fulfilmentRequest.getFulfilmentType()) {
                case "A":
                    passed = ProcessFulfilmentTypeA(fulfilmentRequest);
                    break;
                case "B":
                    passed = ProcessFulfilmentTypeB(fulfilmentRequest);
                    break;
                case "C":
                    passed =ProcessFulfilmentTypeC(fulfilmentRequest);
                    break;
            }
            log.info("ALL CHECKS DONE, RESULT: "+ passed);
            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
            request.setOrderId(fulfilmentRequest.getOrderId());
            if(passed)
            {
                request.setStatus(OrderStatusEnum.ACCEPTED);
                productService.UpdateOrder(request);
            }
            else
            {
                request.setStatus(OrderStatusEnum.REJECTED);
                productService.UpdateOrder(request);
            }
        }
        catch(Exception e)
        {
            log.info("-----------------Fulfilment processing failed - Attempt: " + (retryCount + 1) + " of 6");
            throw new RuntimeException(e);
        }
    }

    public boolean ProcessFulfilmentTypeA(FulfilmentRequest fulfilmentRequest) throws Exception
    {
        try
        {
            boolean kycCheck = kycCheckService.DoKycCheck(fulfilmentRequest.getId());
            log.info("Fulfilment kyc check: " + kycCheck);
            return kycCheck;
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type A: " + e.getMessage());
            throw new Exception("ProcessFulfilmentTypeA failed: " + e.getMessage());
        }
    }

    public boolean ProcessFulfilmentTypeB(FulfilmentRequest fulfilmentRequest) throws Exception
    {
        try
        {
            boolean kycCheck = kycCheckService.DoKycCheck(fulfilmentRequest.getId());
            log.info("Fulfilment kyc check: " + kycCheck);

            boolean fraudCheck = fraudCheckService.DoFraudCheck(fulfilmentRequest.getId(),fulfilmentRequest.getIdNumber());
            log.info("Fulfilment fraud check: " + fraudCheck);

            boolean livingStatus = dhaService.DoLivingStatusCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));
            log.info("Fulfilment living status check: " + livingStatus);

            boolean duplicateIdStatus = dhaService.DoDuplicateIdCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));
            log.info("Fulfilment duplicate id status check: " + duplicateIdStatus);

            log.info("ProcessFulfilmentTypeB: "+ (kycCheck && fraudCheck && livingStatus && duplicateIdStatus));

            return kycCheck && fraudCheck && livingStatus && duplicateIdStatus;
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type B" + e.getMessage());
            throw new Exception("ProcessFulfilmentTypeB failed: " + e.getMessage());
        }

    }

    public boolean ProcessFulfilmentTypeC(FulfilmentRequest fulfilmentRequest) throws Exception
    {
        try {
            boolean processBFlag = ProcessFulfilmentTypeB(fulfilmentRequest);

            log.info("Fulfilment processBFlag: " + processBFlag);

            boolean maritalStatus = dhaService.DoMaritalCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));

            log.info("Fulfilment marital statuses check: " + maritalStatus);

            boolean creditCheck = creditCheckService.DoCreditCheck(fulfilmentRequest.getId());

            log.info("Fulfilment credit check" + creditCheck);

            log.info("ProcessFulfilmentTypeC check: " + (processBFlag && maritalStatus && creditCheck));

            return processBFlag && maritalStatus && creditCheck;
        }
        catch (IOException e) {
            log.info("Fulfilment IOException type C: " + e.getMessage());
            throw new Exception("ProcessFulfilmentTypeC failed IOException: " + e.getMessage());
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type C: " + e.getMessage());
            throw new Exception("ProcessFulfilmentTypeC failed: " + e.getMessage());
        }
    }

}
