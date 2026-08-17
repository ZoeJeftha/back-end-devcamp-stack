package za.co.entelect.devcamp.customerinformationstore.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import za.co.entelect.devcamp.customerinformationstore.model.*;
import za.co.entelect.devcamp.customerinformationstore.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static za.co.entelect.devcamp.customerinformationstore.TestDataUtil.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CustomerServiceTest {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceTest(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @Test
    void givenValidCustomerRequestWithoutCustomerType_WhenRegisteringANewCustomer_ThenReturnCreatedCustomer() {
        CustomerDto newCustomer = getCustomerDto();
        ResponseEntity<CustomerDto> customerDtoResponseEntity = customerService.registerCustomer(newCustomer);
        assertEquals(HttpStatus.OK, customerDtoResponseEntity.getStatusCode());
        CustomerDto customerDto = customerDtoResponseEntity.getBody();
        assertNotNull(customerDto);
        assertEquals(CURRENT_CUSTOMERS + 1, customerDto.getId());
        Optional<Customer> customerById = customerRepository.findById((long) (CURRENT_CUSTOMERS+1));
        assertTrue(customerById.isPresent());
        assertEquals(newCustomer.getUsername(), customerById.get().getEmail());
    }

    @Test
    void givenValidCustomerRequestWithCustomerTypeId_WhenRegisteringANewCustomer_ThenReturnCreatedCustomer() {
        CustomerDto customerDto = getCustomerDto();
        int customerTypeId = 1;
        customerDto.customerTypeId(customerTypeId);

        ResponseEntity<CustomerDto> customerDtoResponseEntity = customerService.registerCustomer(customerDto);
        assertEquals(HttpStatus.OK, customerDtoResponseEntity.getStatusCode());
        CustomerDto responseCustomerDto = customerDtoResponseEntity.getBody();
        assertNotNull(responseCustomerDto);
        assertEquals(customerDto.getUsername(), responseCustomerDto.getUsername());
        assertEquals(customerTypeId, responseCustomerDto.getCustomerType().getId());
        Optional<Customer> customerById = customerRepository.findById(Long.valueOf(responseCustomerDto.getId()));
        assertTrue(customerById.isPresent());
        Customer customer = customerById.get();
        assertNotNull(customer.getCustomerTypes());
        assertEquals((long)customerDto.getCustomerTypeId(), customer.getCustomerTypes().getCustomerTypesId());
    }

    @Test
    void givenCustomerId1_whenFetchingAccountsById_thenExpect1Account() {
        ResponseEntity<List<AccountTypeDto>> customerAccountsByCustomerId = customerService.getCustomerAccountsByCustomerId(1);
        assertEquals(HttpStatus.OK, customerAccountsByCustomerId.getStatusCode());
        List<AccountTypeDto> customerAccounts = customerAccountsByCustomerId.getBody();
        assertNotNull(customerAccounts);
        assertEquals(
                2,customerAccounts.size());
        assertEquals(1, customerAccounts.get(0).getId().intValue());
        assertEquals(5, customerAccounts.get(1).getId().intValue());
    }

    @Test
    void givenCustomerId5_whenFetchingAccountsById_thenExpect404() {
        ResponseEntity<List<AccountTypeDto>> customerAccountsByCustomerId = customerService.getCustomerAccountsByCustomerId(5);
        assertEquals(HttpStatus.OK, customerAccountsByCustomerId.getStatusCode());
        assertNotNull(customerAccountsByCustomerId.getBody());
        assertEquals(0, customerAccountsByCustomerId.getBody().size());
    }

    @Test
    void givenCustomerThatDoesNotExist_whenAddCustomerDocumentsById_thenExpect404ResponseCode() {
        ResponseEntity<AddCustomerDocumentByCustomerId200Response> addDocumentsResponse = customerService.addCustomerDocumentByCustomerId(5, getDocumentDto());
        assertEquals(HttpStatus.NOT_FOUND, addDocumentsResponse.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void givenCustomerThatDoesExist_whenAddCustomerDocumentsById_thenExpect200Response() {
        DocumentDto documentDto = getDocumentDto();
        ResponseEntity<AddCustomerDocumentByCustomerId200Response> addDocumentsResponse = customerService.addCustomerDocumentByCustomerId(1, documentDto);
        assertEquals(HttpStatus.OK, addDocumentsResponse.getStatusCode());
        assertNotNull(addDocumentsResponse.getBody());
        assertEquals(CURRENT_DOCUMENT+1, addDocumentsResponse.getBody().getDocumentId());
    }

    @Test
    void givenValidCustomerId_whenRequestingDocumentsById_thenReturnListOfDocuments() {
        ResponseEntity<List<DocumentsDtoInner>> customerDocumentsByCustomerId = customerService.getCustomerDocumentsByCustomerId(1);
        assertEquals(HttpStatus.OK, customerDocumentsByCustomerId.getStatusCode());
        assertNotNull(customerDocumentsByCustomerId.getBody());
        assertEquals(2, customerDocumentsByCustomerId.getBody().size());
    }

    @Test
    void givenInvalidCustomerId_whenRequestingDocumentsById_thenReturnEmptyList() {
        ResponseEntity<List<DocumentsDtoInner>> customerDocumentsByCustomerId = customerService.getCustomerDocumentsByCustomerId(10);
        assertEquals(HttpStatus.OK, customerDocumentsByCustomerId.getStatusCode());
        assertNotNull(customerDocumentsByCustomerId.getBody());
        assertEquals(0, customerDocumentsByCustomerId.getBody().size());
    }

    @Test
    void givenValidCustomerId_whenRequestingCustomerById_thenReturn200AndCustomerObject() {
        ResponseEntity<CustomerDto> customerById = customerService.getCustomerById(1);
        assertEquals(HttpStatus.OK, customerById.getStatusCode());
        assertNotNull(customerById.getBody());
        assertEquals("Jesse", customerById.getBody().getFirstName());
    }

    @Test
    void givenInvalidCustomerId_whenRequestingCustomerById_thenReturn404AndEmptyResponseBody() {
        ResponseEntity<CustomerDto> customerById = customerService.getCustomerById(20);
        assertEquals(HttpStatus.NOT_FOUND, customerById.getStatusCode());
        assertNull(customerById.getBody());
    }

    @Test
    void givenValidCustomerAndValidAccountToLink_whenAddingCustomerAccountsToCustomer_thenReturn200() {
        ResponseEntity<Void> responseEntity = customerService.addCustomerAccountsToCustomerById(1, 3);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        Optional<Customer> customerRepositoryById = customerRepository.findById(1L);
        assertTrue(customerRepositoryById.isPresent());
        Customer customer = customerRepositoryById.get();
        assertNotNull(customer.getCustomerAccounts());
        assertEquals(3,  customer.getCustomerAccounts().size());
        assertEquals(3, customer.getCustomerAccounts().get(2).getAccountType().getAccountTypeId());
    }

    @Test
    void givenInvalidCustomerAndValidAccountToLink_whenAddingCustomerAccountsToCustomer_thenReturn404() {
        ResponseEntity<Void> responseEntity = customerService.addCustomerAccountsToCustomerById(10, 1);
        assertEquals(HttpStatus.NOT_FOUND,  responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void givenValidCustomerAndInvalidAccountToLink_whenAddingCustomerAccountsToCustomer_thenReturn404() {
        ResponseEntity<Void> responseEntity = customerService.addCustomerAccountsToCustomerById(1, 10);
        assertEquals(HttpStatus.NOT_FOUND,  responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void givenValidCustomerTypeAndValidCustomerTypeId_whenUpdatingCustomerWithNewCustomerId_thenReturn200ResponseCodeAndUpdateCustomerObject() {
        ResponseEntity<Void> responseEntity = customerService.updateCustomerTypeToCustomerById(1, 2);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        Optional<Customer> customerRepositoryById = customerRepository.findById(1L);
        assertTrue(customerRepositoryById.isPresent());
        assertEquals(2, customerRepositoryById.get().getCustomerTypes().getCustomerTypesId());
    }

    @Test
    void givenInvalidCustomerTypeAndValidCustomerTypeId_whenUpdatingCustomerWithNewCustomerId_thenReturn404ResponseCode() {
        ResponseEntity<Void> responseEntity = customerService.updateCustomerTypeToCustomerById(10, 1);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void givenValidCustomerTypeAndInvalidCustomerTypeId_whenUpdatingCustomerWithNewCustomerId_thenReturn404ResponseCode() {
        ResponseEntity<Void> responseEntity = customerService.updateCustomerTypeToCustomerById(1, 10);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void givenValidCustomerEmailAddress_whenRequestingCustomerByEmailAddress_thenReturn200AndCustomerObject() {
        ResponseEntity<CustomerDto> customerByEmailAddress = customerService.getCustomerByEmailAddress("jesse.leresche@entelect.co.za");
        assertEquals(HttpStatus.OK, customerByEmailAddress.getStatusCode());
        assertNotNull(customerByEmailAddress.getBody());
        assertEquals(1, customerByEmailAddress.getBody().getId());
    }

    @Test
    void givenInvalidCustomerEmailAddress_whenRequestingCustomerByEmailAddress_thenReturn404Response() {
        ResponseEntity<CustomerDto> customerByEmailAddress = customerService.getCustomerByEmailAddress("invalid@Email.co.za");
        assertEquals(HttpStatus.NOT_FOUND, customerByEmailAddress.getStatusCode());
        assertNull(customerByEmailAddress.getBody());
    }
}
