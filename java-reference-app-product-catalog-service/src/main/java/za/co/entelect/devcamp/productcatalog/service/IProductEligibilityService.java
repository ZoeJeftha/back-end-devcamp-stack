
package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.requests.CustomerProductEligibilityRequest;
import za.co.entelect.devcamp.productcatalog.requests.CustomerAccountEligibilityRequest;

import java.util.List;

public interface IProductEligibilityService {

    boolean isCustomerEligible(CustomerProductEligibilityRequest customerProductEligibilityRequest);

    boolean isCustomerAccountEligible(CustomerAccountEligibilityRequest customerAccountEligibilityRequest);
}

