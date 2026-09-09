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
import za.co.entelect.devcamp.productcatalog.client.ICustomerApiClient;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.ICustomerService;

@Service
public class CustomerService implements ICustomerService {
    private final ICustomerApiClient customerApiClient;

    @Autowired
    public CustomerService(ICustomerApiClient customerApiClient)
    {
        this.customerApiClient = customerApiClient;
    }

    @Override
    public CustomerDto GetMyProfile(String token, String username) throws NotFoundException, Exception
    {
        ResponseEntity<CustomerDto> customer = customerApiClient.GetMyProfile(token, username);
        CustomerDto customerDto = customer.getBody();

        String maskedIdNumber = maskIdNumber(customerDto.getIdNumber());
        customerDto.setIdNumber(maskedIdNumber);

        return customerDto;
    }

    public List<CustomerDto> GetProfiles(String token) throws NotFoundException, Exception
    {
        ResponseEntity<List<CustomerDto>> customers = customerApiClient.GetProfiles(token);
        List<CustomerDto> customerDtos = customers.getBody();

        List<CustomerDto> maskedCustomers = customerDtos.stream()
                .map(customer ->
                {
                    customer.setIdNumber(maskIdNumber(customer.getIdNumber()));
                    return customer;
                }) .collect(Collectors.toList());

        return maskedCustomers;
    }

    public CustomerDto CreateCustomer(CustomerDto request) throws Exception
    {
        try {
            ResponseEntity<CustomerDto> customer = customerApiClient.CreateCustomer(request);
            CustomerDto customerDto = customer.getBody();

            String maskedIdNumber = maskIdNumber(customerDto.getIdNumber());
            customerDto.setIdNumber(maskedIdNumber);
            return customerDto;
        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }

    }

    private String maskIdNumber(String idNumber) {

        if (idNumber == null || idNumber.length() <= 4) {
            return idNumber;
        }
        return "*".repeat(idNumber.length() - 4)
                + idNumber.substring(idNumber.length() - 4);
    }

}