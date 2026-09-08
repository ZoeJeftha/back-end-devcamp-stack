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
    public MaritalStatusResponse DoMaritalCheck(Long idNumber) throws Exception
    {
        try {
            String token = authService.GetSystemToken();
            return dhaChecksApiClient.DoMaritalCheck(token, idNumber);
        }
        catch(Exception e)
        {
            System.out.println("------------------dha-marital-check dha service" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public DuplicateIDDocumentCheckResponse DoDuplicateIdCheck(Long idNumber) throws Exception
    {
        try
        {
            String token = authService.GetSystemToken();
            return dhaChecksApiClient.DoDuplicateIdCheck(token, idNumber);
        }
        catch(Exception e)
        {
            System.out.println("------------------dha-marital-check dha service" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public LivingStatusResponse DoLivingStatusCheck(Long idNumber) throws Exception
    {
        try {
            String token = authService.GetSystemToken();
            return dhaChecksApiClient.DoLivingStatusCheck(token, idNumber);
        }
        catch(Exception e)
        {
            System.out.println("------------------dha-marital-check dha service" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}