package za.co.entelect.devcamp.fulfilment.controller;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.devcamp.fulfilment.dto.DuplicateIdStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.LivingStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.MaritalStatusesDto;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditChecksApiClient;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaChecksApiClient;

@Slf4j
@RestController
@RequestMapping("/v1")
public class FulfilmentController {

    public final ICreditChecksApiClient creditChecksApiClient;
    public final IDhaChecksApiClient dhaChecksApiClient;

    public FulfilmentController(ICreditChecksApiClient creditChecksApiClient,
                                IDhaChecksApiClient dhaChecksApiClient)
    {
        this.creditChecksApiClient = creditChecksApiClient;
        this.dhaChecksApiClient = dhaChecksApiClient;
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
}
