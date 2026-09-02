package za.co.entelect.devcamp.fulfilment.controller;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.devcamp.fulfilment.dto.DuplicateIdStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.dto.LivingStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.MaritalStatusesDto;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditChecksApiClient;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaChecksApiClient;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycChecksApiClient;

@Slf4j
@RestController
@RequestMapping("/v1")
public class FulfilmentController {

    public final ICreditChecksApiClient creditChecksApiClient;
    public final IDhaChecksApiClient dhaChecksApiClient;
    public final IKycChecksApiClient kycChecksApiClient;

    public FulfilmentController(ICreditChecksApiClient creditChecksApiClient,
                                IDhaChecksApiClient dhaChecksApiClient,
                                IKycChecksApiClient kycChecksApiClient)
    {
        this.creditChecksApiClient = creditChecksApiClient;
        this.dhaChecksApiClient = dhaChecksApiClient;
        this.kycChecksApiClient = kycChecksApiClient;
    }

    @GetMapping("/do-credit-check")
    public String DoCreditCheck()
    {
        try {
            return creditChecksApiClient.DoCreditCheck(1L);
        }
        catch (IOException e) {
            return "IOException thrown: "+ e.getMessage();
        }
        catch(Exception e)
        {
            return "Exception thrown: "+ e.getMessage();
        }
    }

    @GetMapping("/do-dha-marital-check")
    public MaritalStatusesDto DoDhaMaritalCheck(@AuthenticationPrincipal Jwt jwt)
    {
        try {
            String token = jwt.getTokenValue();
            return dhaChecksApiClient.DoMaritalCheck(token, 9001010000081L);
        }
        catch(Exception e)
        {
            return null;
                    //"Exception thrown: "+ e.getMessage();
        }
    }

    @GetMapping("/do-dha-duplicate-id-check")
    public DuplicateIdStatusDto DoDhaDuplicateIdCheck(@AuthenticationPrincipal Jwt jwt)
    {
        try {
            String token = jwt.getTokenValue();
            return dhaChecksApiClient.DoDuplicateIdCheck(token, 9001010000081L);
        }
        catch(Exception e)
        {
            return null;
            //"Exception thrown: "+ e.getMessage();
        }
    }


    @GetMapping("/do-dha-living-status-check")
    public LivingStatusDto DoLivingStatusCheck(@AuthenticationPrincipal Jwt jwt)
    {
        try {
            String token = jwt.getTokenValue();
            return dhaChecksApiClient.DoLivingStatusCheck(token, 9001010000081L);
        }
        catch(Exception e)
        {
            return null;
            //"Exception thrown: "+ e.getMessage();
        }
    }

    @GetMapping("/do-kyc-check")
    public KycDto DoKycCheck(@AuthenticationPrincipal Jwt jwt)
    {
        try {
            String token = jwt.getTokenValue();
            return kycChecksApiClient.DoKycCheck(token, "1");
        }
        catch(Exception e)
        {
            return null;
            //"Exception thrown: "+ e.getMessage();
        }
    }
}
