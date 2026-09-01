package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.ICustomerService;
import za.co.entelect.devcamp.productcatalog.client.CustomerApiClient;

@Service
public class CustomerService implements ICustomerService {
    private final CustomerApiClient customerApiClient;

    @Autowired
    public CustomerService(CustomerApiClient customerApiClient)
    {
        this.customerApiClient = customerApiClient;
    }

    @Override
    public ResponseEntity<ApiResponse<CustomerDto>> GetMyProfile(String token) {
        return customerApiClient.GetMyProfile(token);
    }

}