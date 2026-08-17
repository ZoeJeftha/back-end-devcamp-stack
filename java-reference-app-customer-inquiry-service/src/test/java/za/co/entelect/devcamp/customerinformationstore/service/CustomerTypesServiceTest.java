package za.co.entelect.devcamp.customerinformationstore.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.customerinformationstore.TestDataUtil;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerTypes;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerTypesDto;
import za.co.entelect.devcamp.customerinformationstore.repository.CustomerTypesRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static za.co.entelect.devcamp.customerinformationstore.TestDataUtil.CURRENT_CUSTOMER_TYPES;
import static za.co.entelect.devcamp.customerinformationstore.TestDataUtil.getCustomerTypesDto;

@SpringBootTest
public class CustomerTypesServiceTest {

    private final CustomerTypesService customerTypesService;
    private final CustomerTypesRepository customerTypesRepository;

    @Autowired
    public CustomerTypesServiceTest(CustomerTypesService customerTypesService, CustomerTypesRepository customerTypesRepository) {
        this.customerTypesService = customerTypesService;
        this.customerTypesRepository = customerTypesRepository;
    }

    @Test
    void givenValidCustomerTypes_WhenRequestComesIn_ThenAddCustomerTypeToDatabase() {
        CustomerTypesDto customerTypes = getCustomerTypesDto();
        ResponseEntity<CustomerTypesDto> customerTypesDtoResponseEntity = customerTypesService.addCustomerTypes(customerTypes);
        assertEquals(HttpStatus.OK, customerTypesDtoResponseEntity.getStatusCode());
        CustomerTypesDto customerTypesDtoResponseEntityBody = customerTypesDtoResponseEntity.getBody();
        assertEquals(CURRENT_CUSTOMER_TYPES + 1, customerTypesDtoResponseEntityBody.getId().intValue());
        assertEquals(customerTypes.getName(), customerTypesDtoResponseEntityBody.getName());
        assertEquals(customerTypes.getDescription(), customerTypesDtoResponseEntityBody.getDescription());
        Optional<CustomerTypes> customerTypesRepositoryById = customerTypesRepository.findById(customerTypesDtoResponseEntityBody.getId().longValue());
        assertTrue(customerTypesRepositoryById.isPresent());
        CustomerTypes customerTypesById = customerTypesRepositoryById.get();
        assertEquals(customerTypes.getName(), customerTypesById.getName());
        assertEquals(customerTypes.getDescription(), customerTypesById.getDescription());
    }

    @Test
    void givenValidRequest_WhenRequestingForAllCustomerTypes_ThenAllCustomerTypesAreReturned() {
        ResponseEntity<List<CustomerTypesDto>> customerTypesResponseEntity = customerTypesService.getCustomerTypes();
        assertEquals(HttpStatus.OK, customerTypesResponseEntity.getStatusCode());
        assertNotNull(customerTypesResponseEntity.getBody());
        assertEquals(TestDataUtil.CURRENT_CUSTOMER_TYPES, customerTypesResponseEntity.getBody().size());
    }
}
