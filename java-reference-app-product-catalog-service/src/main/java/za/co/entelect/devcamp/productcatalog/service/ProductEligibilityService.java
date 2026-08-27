package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.repository.QualifyingAccountsRepository;
import za.co.entelect.devcamp.productcatalog.repository.QualifyingCustomerTypesRepository;
import za.co.entelect.devcamp.productcatalog.requests.CustomerProductEligibilityRequest;
import za.co.entelect.devcamp.productcatalog.requests.CustomerAccountEligibilityRequest;
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

    public boolean isCustomerEligible(CustomerProductEligibilityRequest customerProductEligibilityRequest) {

        return qualifyingCustomerTypesRepository.existsByProductIdAndCustomerTypesId(
                customerProductEligibilityRequest.getProductId(),
                customerProductEligibilityRequest.getCustomerTypesId()
        );
    }


    public boolean isCustomerAccountEligible(CustomerAccountEligibilityRequest customerAccountEligibilityRequest) {

        return qualifyingAccountsRepository.existsByProductIdAndAccountId(
                customerAccountEligibilityRequest.getProductId(),
                customerAccountEligibilityRequest.getAccountId()
        );
    }
}