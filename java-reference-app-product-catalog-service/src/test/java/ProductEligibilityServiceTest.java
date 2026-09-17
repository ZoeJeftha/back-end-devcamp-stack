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
import za.co.entelect.devcamp.productcatalog.client.IAuthApiClient;
import za.co.entelect.devcamp.productcatalog.dto.AccountsDto;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.CustomerTypesDto;
import za.co.entelect.devcamp.productcatalog.requests.LoginRequest;
import za.co.entelect.devcamp.productcatalog.repository.QualifyingAccountsRepository;
import za.co.entelect.devcamp.productcatalog.repository.QualifyingCustomerTypesRepository;
import za.co.entelect.devcamp.productcatalog.service.ProductEligibilityService;

@ExtendWith(MockitoExtension.class)
public class ProductEligibilityServiceTest
{
    @InjectMocks
    private ProductEligibilityService productEligibilityService;

    @Mock
    private QualifyingAccountsRepository qualifyingAccountsRepository;

    @Mock
    private QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository;

    @Mock
    private ICustomerService customerService;

    @Test
    void IsCustomerEligible_ShouldReturnBooleanValue() throws Exception
    {
        String token = "Token";
        String username = "user@gmail.com";
        Long productId = 1L;

        CustomerDto customer = new CustomerDto();
        customer.setId(1L);
        customer.setUsername(username);
        customer.setFirstName("User");
        customer.setLastName("Surname");
        customer.setIdNumber("99070400138082");
        customer.setCustomerTypeId(1L);

        CustomerTypesDto customerType = new CustomerTypesDto();
        customerType.setId(1L);
        customerType.setName("Customer type 1");
        customerType.setDescription("Description");

        AccountsDto account1 = new AccountsDto();
        account1.setId(1L);
        account1.setName("Account 1");
        account1.setDescription("Description 1");

        AccountsDto account2 = new AccountsDto();
        account2.setId(2L);
        account2.setName("Account 2");
        account2.setDescription("Description 2");

        List<AccountsDto> accountDtoList = new ArrayList<>();
        accountDtoList.add(account1);
        accountDtoList.add(account2);

        customer.setCustomerType(customerType);
        customer.setCustomerAccounts(accountDtoList);

        when(customerService.GetMyProfile(token, username))
                .thenReturn(customer);

        List<Long> accountIds = List.of(1L, 2L);

        when(qualifyingAccountsRepository.existsByProductIdAndAccountIdIn(productId,accountIds))
                .thenReturn(true);

        when(qualifyingCustomerTypesRepository.existsByProductIdAndCustomerTypesId(productId, 1L))
                .thenReturn(false);

        boolean result = productEligibilityService.isCustomerEligible(token,username,productId);

        assertFalse(result);

        verify(customerService).GetMyProfile(token, username);

        verify(qualifyingAccountsRepository).existsByProductIdAndAccountIdIn(productId,accountIds);

        verify(qualifyingCustomerTypesRepository).existsByProductIdAndCustomerTypesId(productId, 1L);
    }

}