package za.co.entelect.devcamp.customerinformationstore.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static za.co.entelect.devcamp.customerinformationstore.TestDataUtil.CURRENT_CUSTOMERS;

@SpringBootTest
public class CustomersServiceTest {

    private final CustomersService customersService;

    @Autowired
    public CustomersServiceTest(CustomersService customersService) {
        this.customersService = customersService;
    }

    @Test
    void givenValidRequest_WhenFetchingAllCustomers_ThenReturnAllCustomers() {
        ResponseEntity<List<CustomerDto>> customersResponseEntity = customersService.getCustomers();
        assertEquals(HttpStatus.OK, customersResponseEntity.getStatusCode());
        List<CustomerDto> customers = customersResponseEntity.getBody();
        assertNotNull(customers);
        assertEquals(CURRENT_CUSTOMERS, customers.size());
    }
}
