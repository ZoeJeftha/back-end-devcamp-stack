package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.repository.QualifyingAccountsRepository;
import za.co.entelect.devcamp.productcatalog.repository.QualifyingCustomerTypesRepository;
import za.co.entelect.devcamp.productcatalog.requests.CustomerEligibilityRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductEligibilityService implements IProductEligibilityService {

    private final QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository;
    private final QualifyingAccountsRepository qualifyingAccountsRepository;

    public ProductEligibilityService(
            QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository,
            QualifyingAccountsRepository qualifyingAccountsRepository) {
        this.qualifyingCustomerTypesRepository = qualifyingCustomerTypesRepository;
        this.qualifyingAccountsRepository = qualifyingAccountsRepository;
    }

    public boolean isCustomerEligible(CustomerEligibilityRequest customerEligibilityRequest)
    {
        boolean eligible = qualifyingAccountsRepository.existsByProductIdAndAccountIdIn(
                customerEligibilityRequest.getProductId(),
                customerEligibilityRequest.getAccountIds());
        eligible = eligible && qualifyingCustomerTypesRepository.existsByProductIdAndCustomerTypesId(
                customerEligibilityRequest.getProductId(),
                customerEligibilityRequest.getCustomerTypesId());

        return eligible;

    }
}