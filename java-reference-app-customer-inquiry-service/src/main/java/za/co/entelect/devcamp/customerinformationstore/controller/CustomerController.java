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

@RestController
@RequestMapping("/v1")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/my-profile")
    public ResponseEntity<CustomerDto> getMyProfile(
            @AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getSubject();

        return customerService.getCustomerByEmailAddress(username);
    }
}