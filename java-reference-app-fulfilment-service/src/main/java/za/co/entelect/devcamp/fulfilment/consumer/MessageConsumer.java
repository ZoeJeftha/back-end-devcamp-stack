package za.co.entelect.devcamp.fulfilment.consumer;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.configuration.RabbitConfig;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaService;
import za.co.entelect.devcamp.fulfilment.interfaces.IFraudCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycCheckService;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;

@Slf4j
@Service
public class MessageConsumer {

    public final ICreditCheckService creditCheckService;
    public final IDhaService dhaService;
    public final IKycCheckService kycCheckService;
    public final IFraudCheckService fraudCheckService;

    public MessageConsumer(ICreditCheckService creditCheckService,
                           IDhaService dhaService,
                           IKycCheckService kycCheckService,
                           IFraudCheckService fraudCheckService)
    {
        this.creditCheckService = creditCheckService;
        this.dhaService = dhaService;
        this.kycCheckService = kycCheckService;
        this.fraudCheckService = fraudCheckService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE,   containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(FulfilmentRequest fulfilmentRequest) {
        try {
            log.info("Message queue received: " + fulfilmentRequest);
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
        }
        catch(Exception e)
        {
            log.info("Message queue received, failed to process: " + e.getMessage());
        }
    }

    public boolean ProcessFulfilmentTypeA(FulfilmentRequest fulfilmentRequest)
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
            //to do: handle errors properly
            return false;
        }
    }

    public boolean ProcessFulfilmentTypeB(FulfilmentRequest fulfilmentRequest)
    {
        try
        {
            boolean kycCheck = kycCheckService.DoKycCheck(fulfilmentRequest.getId());
            log.info("Fulfilment kyc check: " + kycCheck);

            boolean fraudCheck = fraudCheckService.DoFraudCheck(fulfilmentRequest.getId(),fulfilmentRequest.getIdNumber());
            log.info("Fulfilment fraud check: " + kycCheck);

            boolean livingStatus = dhaService.DoLivingStatusCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));
            log.info("Fulfilment living status check: " + kycCheck);

            boolean duplicateIdStatus = dhaService.DoDuplicateIdCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));
            log.info("Fulfilment duplicate id status check: " + duplicateIdStatus);

            log.info("ProcessFulfilmentTypeB: "+ (kycCheck && fraudCheck && livingStatus && duplicateIdStatus));

            return kycCheck && fraudCheck && livingStatus && duplicateIdStatus;
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type B" + e.getMessage());
            //to do: handle errors properly
            return false;
        }

    }

    public boolean ProcessFulfilmentTypeC(FulfilmentRequest fulfilmentRequest)
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
            //to do: handle errors properly
            return false;
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type C: " + e.getMessage());
            //to do: handle errors properly
            return false;
        }
    }

}
