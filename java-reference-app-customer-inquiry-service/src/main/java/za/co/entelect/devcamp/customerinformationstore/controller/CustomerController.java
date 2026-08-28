package za.co.entelect.devcamp.customerinformationstore.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.devcamp.customerinformationstore.service.CustomerService;
import org.springframework.http.ResponseEntity;
import javax.persistence.*;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import za.co.entelect.devcamp.customerinformationstore.responses.ApiResponse;
import  za.co.entelect.devcamp.customerinformationstore.service.CustomersService;
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/v1")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomersService customersService;

    public CustomerController(CustomerService customerService, CustomersService customersService) {
        this.customerService = customerService;
        this.customersService = customersService;
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<ResponseEntity<CustomerDto>>> getMyProfile(
            @AuthenticationPrincipal Jwt jwt) {
        try {
            String username = jwt.getSubject();

            ResponseEntity<CustomerDto> customer = customerService.getCustomerByEmailAddress(username);
            ApiResponse<ResponseEntity<CustomerDto>> response = new ApiResponse<ResponseEntity<CustomerDto>>(true, "Profile retrieved successfully", customer);
            return ResponseEntity.ok(response);
        }
        catch(Exception e)
        {
            ApiResponse<ResponseEntity<CustomerDto>> response = new ApiResponse<ResponseEntity<CustomerDto>>(false, "Failed to retrieve profile: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/open-account")
    public ResponseEntity<ApiResponse<ResponseEntity<CustomerDto>>> OpenAccount(
            @AuthenticationPrincipal Jwt jwt , @RequestBody Integer accountTypeId) {

        try {
            String username = jwt.getSubject();

            ResponseEntity<CustomerDto> customer = customerService.OpenAccount(username, accountTypeId);
            ApiResponse<ResponseEntity<CustomerDto>> response = new ApiResponse<ResponseEntity<CustomerDto>>(true, "Account opened successfully", customer);
            return ResponseEntity.ok(response);
        }
        catch(Exception e)
        {
            ApiResponse<ResponseEntity<CustomerDto>> response = new ApiResponse<ResponseEntity<CustomerDto>>(false, "Failed to open account: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getProfiles(
            @AuthenticationPrincipal Jwt jwt) {

        try {
            String role = jwt.getClaimAsString("role");

            if (!"admin".equals(role)) {

                ApiResponse<List<CustomerDto>> response = new ApiResponse<>(false, "Not authorised to retrieve profiles", null);

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            ResponseEntity<List<CustomerDto>> customers =
                    customersService.getCustomers();

            ApiResponse<List<CustomerDto>> response = new ApiResponse<>(true, "Profiles retrieved successfully", customers.getBody());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            ApiResponse<List<CustomerDto>> response = new ApiResponse<>(false, "Failed to retrieve profiles: " + e.getMessage(), null);

            return ResponseEntity
                    .internalServerError()
                    .body(response);
        }
    }
//
//    @PostMapping("/customer-eligibility")
//    public String CheckCustomerEligibility(
//            @AuthenticationPrincipal Jwt jwt , @RequestBody Integer accountTypeId) {
//
//        try {
//            String username = jwt.getSubject();
//
//
//            ResponseEntity<CustomerDto> customer = customerService.getCustomerByEmailAddress(username);
//
//            ResponseEntity<CustomerDto> customer = customerService.OpenAccount(username, accountTypeId);
//            ApiResponse<ResponseEntity<CustomerDto>> response = new ApiResponse<ResponseEntity<CustomerDto>>(true, "Account opened successfully", customer);
//            return ResponseEntity.ok(response);
//        }
//        catch(Exception e)
//        {
//            ApiResponse<ResponseEntity<CustomerDto>> response = new ApiResponse<ResponseEntity<CustomerDto>>(false, "Failed to open account: "+ e.getMessage(), null);
//            return ResponseEntity.internalServerError().body(response);
//        }
//    }

}