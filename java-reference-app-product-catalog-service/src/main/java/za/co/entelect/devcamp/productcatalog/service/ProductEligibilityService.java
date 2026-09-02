package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.dto.AccountsDto;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.repository.QualifyingAccountsRepository;
import za.co.entelect.devcamp.productcatalog.repository.QualifyingCustomerTypesRepository;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;

@Service
public class ProductEligibilityService implements IProductEligibilityService {

    private final QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository;
    private final QualifyingAccountsRepository qualifyingAccountsRepository;
    private final ICustomerService customerService;

    public ProductEligibilityService(
            QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository,
            QualifyingAccountsRepository qualifyingAccountsRepository,
            ICustomerService customerService) {
        this.qualifyingCustomerTypesRepository = qualifyingCustomerTypesRepository;
        this.qualifyingAccountsRepository = qualifyingAccountsRepository;
        this.customerService = customerService;
    }

    public boolean isCustomerEligible(String token, Long productId)
    {
        ResponseEntity<ApiResponse<CustomerDto>> customerResponse = customerService.GetMyProfile(token);

        CustomerDto customer = customerResponse.getBody().getResult();

        List<AccountsDto> accounts = customer.getCustomerAccounts();

        List<Long> accountIds = accounts.stream()
            .map(AccountsDto::getId)
            .collect(Collectors.toList());

        Long customerTypeId = customer.getCustomerType().getId();

        boolean eligible = qualifyingAccountsRepository.existsByProductIdAndAccountIdIn(
                productId,
                accountIds);

        eligible = eligible && qualifyingCustomerTypesRepository.existsByProductIdAndCustomerTypesId(
                productId,
                customerTypeId);

        return eligible;

    }
}