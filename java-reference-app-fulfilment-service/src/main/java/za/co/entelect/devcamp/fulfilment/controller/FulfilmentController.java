package za.co.entelect.devcamp.fulfilment.controller;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.devcamp.fulfilment.dha.model.DuplicateIDDocumentCheckResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatusResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.MaritalStatusResponse;
import za.co.entelect.devcamp.fulfilment.dto.DuplicateIdStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.dto.LivingStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.MaritalStatusesDto;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaService;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycCheckService;

@Slf4j
@RestController
@RequestMapping("/v1")
public class FulfilmentController {

    public final ICreditCheckService creditCheckService;
    public final IDhaService dhaService;
    public final IKycCheckService kycCheckService;

    public FulfilmentController(ICreditCheckService creditCheckService,
                                IDhaService dhaService,
                                IKycCheckService kycCheckService)
    {
        this.creditCheckService = creditCheckService;
        this.dhaService = dhaService;
        this.kycCheckService = kycCheckService;
    }

    @GetMapping("/credit-check")
    public String DoCreditCheck()
    {
        try {
            return creditCheckService.DoCreditCheck(1L);
        }
        catch (IOException e) {
            return "IOException thrown: "+ e.getMessage();
        }
        catch(Exception e)
        {
            return "Exception thrown: "+ e.getMessage();
        }
    }

    @GetMapping("/dha-marital-check")
    public MaritalStatusResponse DoDhaMaritalCheck()
    {
        try {
            return dhaService.DoMaritalCheck(9001010000081L);
        }
        catch(Exception e)
        {
            System.out.println("------------------do-dha-marital-check " + e.getMessage());
            return null;
                    //"Exception thrown: "+ e.getMessage();
        }
    }

    @GetMapping("/dha-duplicate-id-check")
    public DuplicateIDDocumentCheckResponse DoDhaDuplicateIdCheck()
    {
        try {
            return dhaService.DoDuplicateIdCheck(9001010000081L);
        }
        catch(Exception e)
        {
            System.out.println("------------------do-dha-duplicate-id-check " + e.getMessage());
            return null;
            //"Exception thrown: "+ e.getMessage();
        }
    }


    @GetMapping("/dha-living-status-check")
    public LivingStatusResponse DoLivingStatusCheck()
    {
        try {
            return dhaService.DoLivingStatusCheck(9001010000081L);
        }
        catch(Exception e)
        {
            System.out.println("------------------do-dha-living-status-check " + e.getMessage());
            return null;
            //"Exception thrown: "+ e.getMessage();
        }
    }

    @GetMapping("/kyc-check")
    public boolean DoKycCheck()
    {
        try {
            return kycCheckService.DoKycCheck(1L);
        }
        catch(Exception e)
        {
            log.info("------------------do-dha-living-status-check " + e.getMessage());
            return false;
            //"Exception thrown: "+ e.getMessage();
        }
    }
}
