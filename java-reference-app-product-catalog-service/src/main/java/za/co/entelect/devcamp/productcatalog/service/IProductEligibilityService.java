
package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.requests.CustomerEligibilityRequest;

import java.util.List;

public interface IProductEligibilityService {

    boolean isCustomerEligible(CustomerEligibilityRequest customerEligibilityRequest);
}

