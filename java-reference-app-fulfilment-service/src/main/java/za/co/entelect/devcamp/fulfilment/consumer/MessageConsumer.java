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
import za.co.entelect.devcamp.fulfilment.interfaces.IKycCheckService;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;

@Slf4j
@Service
public class MessageConsumer {

    public final ICreditCheckService creditCheckService;
    public final IDhaService dhaService;
    public final IKycCheckService kycCheckService;

    public MessageConsumer(ICreditCheckService creditCheckService,
                           IDhaService dhaService,
                           IKycCheckService kycCheckService)
    {
        this.creditCheckService = creditCheckService;
        this.dhaService = dhaService;
        this.kycCheckService = kycCheckService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE,   containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(FulfilmentRequest fulfilmentRequest) {
        try {
            log.info("Message queue received: " + fulfilmentRequest);

            switch (fulfilmentRequest.getFulfilmentType()) {
                case "A":
                    ProcessFulfilmentTypeA(fulfilmentRequest);
                    break;
                case "B":
                    ProcessFulfilmentTypeB(fulfilmentRequest);
                    break;
                case "C":
                    ProcessFulfilmentTypeC(fulfilmentRequest);
                    break;
            }
        }
        catch(Exception e)
        {
            log.info("Message queue received, failed to process: " + e.getMessage());
        }
    }

    public void ProcessFulfilmentTypeA(FulfilmentRequest fulfilmentRequest)
    {
        try
        {
            boolean kycCheck = kycCheckService.DoKycCheck(fulfilmentRequest.getId());
            log.info("Fulfilment kyc check: " + kycCheck);
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type A: " + e.getMessage());
        }
    }

    public void ProcessFulfilmentTypeB(FulfilmentRequest fulfilmentRequest)
    {
        try
        {
            boolean kycCheck = kycCheckService.DoKycCheck(fulfilmentRequest.getId());
            log.info("Fulfilment kyc check: " + kycCheck);
            //to do: Add fraud check
            boolean livingStatus = dhaService.DoLivingStatusCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));
            log.info("Fulfilment living status check: " + kycCheck);

            boolean duplicateIdStatus = dhaService.DoDuplicateIdCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));
            log.info("Fulfilment duplicate id status check: " + duplicateIdStatus);
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type B" + e.getMessage());
        }

    }

    public void ProcessFulfilmentTypeC(FulfilmentRequest fulfilmentRequest)
    {
        try {
            ProcessFulfilmentTypeB(fulfilmentRequest);

            boolean maritalStatus = dhaService.DoMaritalCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));

            log.info("Fulfilment marital statuses check: " + maritalStatus);

            boolean creditCheck = creditCheckService.DoCreditCheck(fulfilmentRequest.getId());

            log.info("Fulfilment credit check" + creditCheck);
        }
        catch (IOException e) {
            log.info("Fulfilment IOException type C: " + e.getMessage());
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type C: " + e.getMessage());
        }
    }



}
