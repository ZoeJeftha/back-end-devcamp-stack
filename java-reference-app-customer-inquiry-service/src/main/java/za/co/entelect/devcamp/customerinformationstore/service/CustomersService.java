package za.co.entelect.devcamp.customerinformationstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.customerinformationstore.controller.CustomersApiDelegate;
import za.co.entelect.devcamp.customerinformationstore.model.Customer;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerDto;
import za.co.entelect.devcamp.customerinformationstore.repository.CustomerRepository;
import za.co.entelect.devcamp.authenticationservice.helpers.CustomerHelper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomersService implements CustomersApiDelegate {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomersService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();
        List<CustomerDto> customerDtoList = allCustomers.stream()
                .map(customer ->
                {
                    CustomerDto dto = customer.toCustomerDto();
                    dto.setIdNumber(CustomerHelper.maskIdNumber(customer.getIdNumber()));
                    return dto;
                }) .collect(Collectors.toList());
        return new ResponseEntity<>(customerDtoList, HttpStatus.OK);
    }

}
