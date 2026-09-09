package za.co.entelect.devcamp.fulfilment.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.interfaces.IAuthService;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycChecksApiClient;

@Slf4j
@Component
public class KycCheckService implements IKycCheckService {

    public final IKycChecksApiClient kycChecksApiClient;
    public final IAuthService authService;

    public KycCheckService(IKycChecksApiClient kycChecksApiClient,
                      IAuthService authService)
    {
        this.kycChecksApiClient = kycChecksApiClient;
        this.authService = authService;
    }

    @Override
    public KycDto DoKycCheck(Long idNumber) throws Exception
    {
        try {
            String token = authService.GetSystemToken();
            return kycChecksApiClient.DoKycCheck(token, idNumber);
        }
        catch(Exception e)
        {
            System.out.println("------------------kyc check service" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}