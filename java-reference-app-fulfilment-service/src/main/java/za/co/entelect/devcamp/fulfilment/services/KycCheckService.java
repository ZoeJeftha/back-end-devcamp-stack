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
    public boolean DoKycCheck(Long idNumber) throws Exception
    {
        try {
            String token = authService.GetSystemToken();
            KycDto kycDto = kycChecksApiClient.DoKycCheck(token, idNumber);

            boolean primaryIndicatorFlag =  kycDto.getPrimaryIndicator() == true;
            String taxCompliance = kycDto.getTaxCompliance();
            boolean taxComplianceFlag = taxCompliance.equalsIgnoreCase("AMBER") || taxCompliance.equalsIgnoreCase("GREEN");
            return primaryIndicatorFlag && taxComplianceFlag;
        }
        catch(Exception e)
        {
            System.out.println("------------------kyc check service" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


//    private Boolean primaryIndicator;
//    private Boolean secondaryIndicator;
//    private String taxCompliance
}