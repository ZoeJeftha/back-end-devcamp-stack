package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.repository.QualifyingCustomerTypesRepository;
import za.co.entelect.devcamp.productcatalog.requests.CustomerEligibilityRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductEligibilityService implements IProductEligibilityService {

    private final QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository;

    public ProductEligibilityService(
            QualifyingCustomerTypesRepository qualifyingCustomerTypesRepository) {
        this.qualifyingCustomerTypesRepository = qualifyingCustomerTypesRepository;
    }

    public boolean isCustomerEligible(CustomerEligibilityRequest customerEligibilityRequest) {
        
        return qualifyingCustomerTypesRepository.existsByProductIdAndCustomerTypesId(
                customerEligibilityRequest.getProductId(),
                customerEligibilityRequest.getCustomerTypesId()
        );
    }
}