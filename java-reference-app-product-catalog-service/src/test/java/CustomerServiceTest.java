package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.client.ICustomerApiClient;
import za.co.entelect.devcamp.productcatalog.dto.AccountsDto;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.CustomerTypesDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.requests.LoginRequest;
import za.co.entelect.devcamp.productcatalog.repository.QualifyingAccountsRepository;
import za.co.entelect.devcamp.productcatalog.repository.QualifyingCustomerTypesRepository;
import za.co.entelect.devcamp.productcatalog.service.ProductEligibilityService;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest
{
   @InjectMocks
   public CustomerService customerService;

   @Mock
   public ICustomerApiClient customerApiClient;

   @Test
   void GetMyProfile_ShouldReturnCustomer() throws NotFoundException, Exception
   {
       String token = "token";
       String username = "user@gmail.com";

       CustomerDto customer = new CustomerDto();
       customer.setId(1L);
       customer.setUsername(username);
       customer.setFirstName("User");
       customer.setLastName("Surname");
       customer.setIdNumber("99070400138082");
       customer.setCustomerTypeId(1L);

       ResponseEntity<CustomerDto> customerDtoResponse =
               ResponseEntity.ok(customer);

       when(customerApiClient.GetMyProfile(token, username))
               .thenReturn(customerDtoResponse);

       CustomerDto result = customerService.GetMyProfile(token, username);

       assertEquals(1L, result.getId());
       assertEquals("user@gmail.com", result.getUsername());
       assertEquals("User", result.getFirstName());
       assertEquals("Surname", result.getLastName());
       assertEquals("**********8082", result.getIdNumber());
       assertEquals(1L, result.getCustomerTypeId());

       verify(customerApiClient).GetMyProfile(token, username);
   }

    void GetMyProfile_ShouldThrowNotFoundExceptionWhenProfileDoesNotExist() throws NotFoundException, Exception
    {
        String token = "token";
        String username = "user@gmail.com";

        when(customerApiClient.GetMyProfile(token, username))
                .thenThrow(new NotFoundException("Customer profile not found"));

        NotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                NotFoundException.class,
                () -> customerService.GetMyProfile(token, username)
        );

        assertEquals("Customer profile not found", exception.getMessage());
    }

    @Test
   void GetMyUnmaskedProfile_ShouldReturnUnmaskedCustomer() throws NotFoundException, Exception
   {
       String token = "token";
       String username = "user@gmail.com";

       CustomerDto customer = new CustomerDto();
       customer.setId(1L);
       customer.setUsername(username);
       customer.setFirstName("User");
       customer.setLastName("Surname");
       customer.setIdNumber("99070400138082");
       customer.setCustomerTypeId(1L);

       ResponseEntity<CustomerDto> customerDtoResponseEntity = ResponseEntity.ok(customer);

       when(customerApiClient.GetMyProfile(token, username))
               .thenReturn(customerDtoResponseEntity);

       CustomerDto result = customerService.GetMyUnmaskedProfile(token, username);

       assertEquals(1L, result.getId());
       assertEquals(username, result.getUsername());
       assertEquals("User", result.getFirstName());
       assertEquals("Surname", result.getLastName());
       assertEquals("99070400138082", result.getIdNumber());
       assertEquals(1L, result.getCustomerTypeId());

       verify(customerApiClient).GetMyProfile(token, username);
   }

    @Test
    void GetMyProfiles_ShouldReturnCustomers() throws NotFoundException, Exception
    {
        String token = "token";

        CustomerDto customer = new CustomerDto();
        customer.setId(1L);
        customer.setUsername("user@gmail.com");
        customer.setFirstName("User");
        customer.setLastName("Surname");
        customer.setIdNumber("99070400138082");
        customer.setCustomerTypeId(1L);

        CustomerDto customer2 = new CustomerDto();
        customer2.setId(2L);
        customer2.setUsername("user2@gmail.com");
        customer2.setFirstName("User2");
        customer2.setLastName("Surname2");
        customer2.setIdNumber("99070400138083");
        customer2.setCustomerTypeId(2L);

        List<CustomerDto> customerDtoList = List.of(customer, customer2);

        ResponseEntity<List<CustomerDto>> customerDtoResponse =
                ResponseEntity.ok(customerDtoList);

        when(customerApiClient.GetProfiles(token))
                .thenReturn(customerDtoResponse);

        List<CustomerDto> result = customerService.GetProfiles(token);

        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("user@gmail.com", result.get(0).getUsername());
        assertEquals("User", result.get(0).getFirstName());
        assertEquals("Surname", result.get(0).getLastName());
        assertEquals("**********8082", result.get(0).getIdNumber());
        assertEquals(1L, result.get(0).getCustomerTypeId());

        assertEquals(2L, result.get(1).getId());
        assertEquals("user2@gmail.com", result.get(1).getUsername());
        assertEquals("User2", result.get(1).getFirstName());
        assertEquals("Surname2", result.get(1).getLastName());
        assertEquals("**********8083", result.get(1).getIdNumber());
        assertEquals(2L, result.get(1).getCustomerTypeId());

        verify(customerApiClient).GetProfiles(token);
    }

    @Test
    void CreateCustomer_ShouldReturnCreatedCustomer() throws Exception
    {

        CustomerDto request = new CustomerDto();
        request.setId(1L);
        request.setUsername("user@gmail.com");
        request.setFirstName("User");
        request.setLastName("Surname");
        request.setIdNumber("99070400138082");
        request.setCustomerTypeId(1L);

        ResponseEntity<CustomerDto> customerDtoResponseEntity = ResponseEntity.ok(request);

        when(customerApiClient.CreateCustomer(request))
                .thenReturn(customerDtoResponseEntity);

        CustomerDto result = customerService.CreateCustomer(request);

        assertEquals(1L, result.getId());
        assertEquals("user@gmail.com", result.getUsername());
        assertEquals("User", result.getFirstName());
        assertEquals("Surname", result.getLastName());
        assertEquals("**********8082", result.getIdNumber());
        assertEquals(1L, result.getCustomerTypeId());

        verify(customerApiClient).CreateCustomer(request);
    }

    @Test
    void CreateCustomer_ShouldThrowExceptionWhenUnableToCreateCustomer() throws Exception
    {
        CustomerDto request = new CustomerDto();
        request.setId(1L);
        request.setUsername("user@gmail.com");
        request.setFirstName("User");
        request.setLastName("Surname");
        request.setIdNumber("99070400138082");
        request.setCustomerTypeId(1L);

        when(customerApiClient.CreateCustomer(request))
                .thenThrow(new Exception("Failed to create customer"));

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class,
                () -> customerService.CreateCustomer(request)
        );

        assertEquals("Failed to create customer",exception.getMessage());

        verify(customerApiClient).CreateCustomer(request);
    }

    @Test
    void OpenAccount_ShouldReturnCustomerWithOpenedAccount() throws Exception
    {
        String token = "token";
        String username = "user@gmail.com";
        Integer accountTypeId = 1;

        CustomerDto customer = new CustomerDto();
        customer.setId(1L);
        customer.setUsername("user@gmail.com");
        customer.setFirstName("User");
        customer.setLastName("Surname");
        customer.setIdNumber("99070400138082");
        customer.setCustomerTypeId(1L);

        AccountsDto account = new AccountsDto();
        account.setId(1L);
        account.setName("Account 1");
        account.setDescription("Description 1");

        List<AccountsDto> accountDtoList = List.of(account);

        customer.setCustomerAccounts(accountDtoList);

        ResponseEntity<CustomerDto> customerDtoResponseEntity = ResponseEntity.ok(customer);

        when(customerApiClient.OpenAccount(token, username, accountTypeId))
                .thenReturn(customerDtoResponseEntity);

        CustomerDto result = customerService.OpenAccount(token, username, accountTypeId);

        assertEquals(1L, result.getId());
        assertEquals("user@gmail.com", result.getUsername());
        assertEquals("User", result.getFirstName());
        assertEquals("Surname", result.getLastName());
        assertEquals("**********8082", result.getIdNumber());
        assertEquals(1L, result.getCustomerTypeId());

        assertEquals(1, result.getCustomerAccounts().size());
        assertEquals(1L, result.getCustomerAccounts().get(0).getId());
        assertEquals("Account 1", result.getCustomerAccounts().get(0).getName());
        assertEquals("Description 1", result.getCustomerAccounts().get(0).getDescription());

        verify(customerApiClient).OpenAccount(token, username, accountTypeId);
    }
}