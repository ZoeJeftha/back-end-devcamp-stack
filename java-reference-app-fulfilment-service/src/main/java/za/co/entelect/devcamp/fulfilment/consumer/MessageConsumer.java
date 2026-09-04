package za.co.entelect.devcamp.fulfilment.consumer;

import java.io.IOException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.configuration.RabbitConfig;
import za.co.entelect.devcamp.fulfilment.dha.model.DuplicateIDDocumentCheckResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatusResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.MaritalStatusResponse;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.dto.DuplicateIdStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.LivingStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.MaritalStatusesDto;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditChecksApiClient;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaChecksApiClient;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycChecksApiClient;
import za.co.entelect.devcamp.fulfilment.interfaces.ITokenService;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;

@Service
public class MessageConsumer {

    public final ICreditChecksApiClient creditChecksApiClient;
    public final IKycChecksApiClient kycChecksApiClient;
    public final ITokenService tokenService;
    public final IDhaChecksApiClient dhaChecksApiClient;

    public MessageConsumer(IKycChecksApiClient kycChecksApiClient,
                           ITokenService tokenService,
                           IDhaChecksApiClient dhaChecksApiClient,
                           ICreditChecksApiClient creditChecksApiClient)
    {
        this.kycChecksApiClient = kycChecksApiClient;
        this.tokenService = tokenService;
        this.dhaChecksApiClient = dhaChecksApiClient;
        this.creditChecksApiClient = creditChecksApiClient;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE,   containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(FulfilmentRequest fulfilmentRequest) {
        try {
            System.out.println("-----------------Message queue received: " + fulfilmentRequest);
            String token = tokenService.GetToken(fulfilmentRequest.getUsername());

            System.out.println("----------------Message queue received getFulfilmentType: " + fulfilmentRequest.getFulfilmentType());

            switch (fulfilmentRequest.getFulfilmentType()) {
                case "A":
                    ProcessFulfilmentTypeA(token, fulfilmentRequest);
                    break;
                case "B":
                    ProcessFulfilmentTypeB(token, fulfilmentRequest);
                    break;
                case "C":
                    ProcessFulfilmentTypeC(token, fulfilmentRequest);
                    break;
            }
        }
        catch(Exception e)
        {
            System.out.println("Message queue received, failed to process: " + e.getMessage());
        }
    }

    public void ProcessFulfilmentTypeA(String token, FulfilmentRequest fulfilmentRequest)
    {
        try
        {
            KycDto kycCheck = kycChecksApiClient.DoKycCheck(token, fulfilmentRequest.getId());
            System.out.println("------------------Fulfilment kycCheck type A " + kycCheck);
        }
        catch(Exception e)
        {
            System.out.println("------------------Fulfilment Exception type A " + e.getMessage());
        }
    }

    public void ProcessFulfilmentTypeB(String token, FulfilmentRequest fulfilmentRequest)
    {
        try
        {
            KycDto kycCheck = kycChecksApiClient.DoKycCheck(token, fulfilmentRequest.getId());
            System.out.println("------------------Fulfilment kycCheck type B" + kycCheck);
            //to do: Add fraud check
            LivingStatusResponse livingStatus = dhaChecksApiClient.DoLivingStatusCheck(token, Long.parseLong(fulfilmentRequest.getIdNumber()));
            System.out.println("------------------Fulfilment livingStatusDto type B" + livingStatus);

            DuplicateIDDocumentCheckResponse duplicateIdStatus =dhaChecksApiClient.DoDuplicateIdCheck(token, Long.parseLong(fulfilmentRequest.getIdNumber()));
            System.out.println("------------------Fulfilment duplicateIdStatusDto type B" + duplicateIdStatus);
        }
        catch(Exception e)
        {
            System.out.println("------------------Fulfilment Exception type B" + e.getMessage());
        }

    }

    public void ProcessFulfilmentTypeC(String token, FulfilmentRequest fulfilmentRequest)
    {
        try {
            ProcessFulfilmentTypeB(token, fulfilmentRequest);

            MaritalStatusResponse maritalStatus = dhaChecksApiClient.DoMaritalCheck(token, Long.parseLong(fulfilmentRequest.getIdNumber()));

            System.out.println("------------------Fulfilment maritalStatusesDto type C" + maritalStatus);

            String creditCheck = creditChecksApiClient.DoCreditCheck(fulfilmentRequest.getId());

            System.out.println("------------------Fulfilment creditCheck type C" + creditCheck);
        }
        catch (IOException e) {
            System.out.println("------------------Fulfilment IOException type C" + e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("------------------Fulfilment Exception type C" + e.getMessage());
        }
    }



}
