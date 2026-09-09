package za.co.entelect.devcamp.fulfilment.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.fulfilment.dha.api.DhaApi;
import za.co.entelect.devcamp.fulfilment.dha.ApiClient;
import za.co.entelect.devcamp.fulfilment.dha.model.DuplicateIDDocumentCheckResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatusResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.MaritalStatusResponse;
import za.co.entelect.devcamp.fulfilment.interfaces.IAuthService;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaService;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaChecksApiClient;

@Slf4j
@Component
public class DhaService implements IDhaService {

    public final IDhaChecksApiClient dhaChecksApiClient;
    public final IAuthService authService;

    public DhaService(IDhaChecksApiClient dhaChecksApiClient,
                      IAuthService authService)
    {
        this.dhaChecksApiClient = dhaChecksApiClient;
        this.authService = authService;
    }

    @Override
    public boolean DoMaritalCheck(Long idNumber) throws Exception
    {
        try {
            String token = authService.GetSystemToken();
            MaritalStatusResponse response = dhaChecksApiClient.DoMaritalCheck(token, idNumber);

            String status = response.getCurrentStatus().getStatus().name();
            return status.equalsIgnoreCase("married");
        }
        catch(Exception e)
        {
            log.info("Marital check failed: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean DoDuplicateIdCheck(Long idNumber) throws Exception
    {
        try
        {
            String token = authService.GetSystemToken();
            DuplicateIDDocumentCheckResponse response = dhaChecksApiClient.DoDuplicateIdCheck(token, idNumber);
            return response.getHasDuplicateId();
        }
        catch(Exception e)
        {
            log.info("Duplicate Id check failed" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean DoLivingStatusCheck(Long idNumber) throws Exception
    {
        try {
            String token = authService.GetSystemToken();
            LivingStatusResponse response = dhaChecksApiClient.DoLivingStatusCheck(token, idNumber);

            String livingStatus = response.getLivingStatus().name();
            return livingStatus.equalsIgnoreCase("Alive");
        }
        catch(Exception e)
        {
            log.info("Living status check failed: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}