package za.co.entelect.devcamp.productcatalog.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.model.OrderCustomerChecks;
import za.co.entelect.devcamp.productcatalog.producer.MessageProducer;
import za.co.entelect.devcamp.productcatalog.requests.SaveCustomerChecksRequest;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.ICustomerChecksService;
import za.co.entelect.devcamp.productcatalog.service.ICustomerService;

@Slf4j
@RestController
@RequestMapping("/v1/customer")
public class CustomerController {

    public final ICustomerService customerService;
    public final ICustomerChecksService customerChecksService;

    public CustomerController(ICustomerService customerService,
                              ICustomerChecksService customerChecksService) {
        this.customerService = customerService;
        this.customerChecksService = customerChecksService;
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<CustomerDto>> getMyProfile(@AuthenticationPrincipal Jwt jwt) {

        log.info("Getting my profile");
        try {
            String username = jwt.getSubject();
            String token = jwt.getTokenValue();

            CustomerDto customerDto = customerService.GetMyProfile(token,username);
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(true, "Profile retrieved successfully",customerDto);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e)
        {
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Profile not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e)
        {
            log.info("Failed to retrieve profile" + e.getMessage());
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to retrieve profile: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getProfiles(@AuthenticationPrincipal Jwt jwt) {
        log.info("Getting profiles");
        try {
            String token = jwt.getTokenValue();
            String role = jwt.getClaimAsString("role");

            if (!"admin".equals(role)) {
                ApiResponse<List<CustomerDto>> response = new ApiResponse<>(false, "Not authorised to retrieve profiles", null);

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            List<CustomerDto> customerDto = customerService.GetProfiles(token);
            ApiResponse<List<CustomerDto>> response = new ApiResponse<List<CustomerDto>>(true, "Profile retrieved successfully",customerDto);
            return ResponseEntity.ok(response);
        }
        catch(NotFoundException e) {
            ApiResponse<List<CustomerDto>> response = new ApiResponse<List<CustomerDto>>(false, "Profile not found",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception e) {
            log.info("Failed to retrieve profile" + e.getMessage());
            ApiResponse<List<CustomerDto>> response = new ApiResponse<List<CustomerDto>>(false, "Failed to retrieve profile: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @PostMapping("/open-account")
    public ResponseEntity<ApiResponse<CustomerDto>> OpenAccount(
            @AuthenticationPrincipal Jwt jwt , @RequestBody Integer accountTypeId) {

        try {
            String username = jwt.getSubject();
            String token = jwt.getTokenValue();

            CustomerDto customer = customerService.OpenAccount(token, username, accountTypeId);
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(true, "Account opened successfully", customer);
            return ResponseEntity.ok(response);
        }
        catch(Exception e) {
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, "Failed to open account: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @PostMapping("/checks")
    public ResponseEntity<ApiResponse<List<OrderCustomerChecks>>> SaveCustomerChecks(@RequestBody List<SaveCustomerChecksRequest> customerChecks)
    {
        try
        {
            List<OrderCustomerChecks> customerChecksList =  customerChecksService.SaveCustomerChecks(customerChecks);
            ApiResponse<List<OrderCustomerChecks>> response = new ApiResponse<List<OrderCustomerChecks>>(true, "Customer checks saved successfully", customerChecksList);
            return ResponseEntity.ok(response);
        }
        catch(Exception e)
        {
            ApiResponse<List<OrderCustomerChecks>> response = new ApiResponse<List<OrderCustomerChecks>>(false, "Failed to save customer checks: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
