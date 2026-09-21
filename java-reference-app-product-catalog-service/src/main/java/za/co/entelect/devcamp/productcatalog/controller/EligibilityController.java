package za.co.entelect.devcamp.productcatalog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.IProductEligibilityService;

@Slf4j
@RestController
@RequestMapping("/v1/eligibility")
public class EligibilityController {

    public final IProductEligibilityService productEligibilityService;

    public EligibilityController(IProductEligibilityService productEligibilityService) {
        this.productEligibilityService = productEligibilityService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> CustomerTypeEligibilityCheck(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId)
    {
        log.info("Customer product eligibility request received");
        try {
            String token = jwt.getTokenValue();
            String username = jwt.getSubject();
            Boolean isEligible = productEligibilityService.isCustomerEligible(token, username, productId);
            ApiResponse<Boolean> response = new ApiResponse<Boolean>(true, "Customer Eligibility Result Retrieved",isEligible);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<Boolean> response = new ApiResponse<Boolean>(false, "Customer not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e)
        {
            log.info("Failed to check customer eligibility" + e.getMessage());
            ApiResponse<Boolean> response = new ApiResponse<Boolean>(false, "Failed to retrieve customer eligibility: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
