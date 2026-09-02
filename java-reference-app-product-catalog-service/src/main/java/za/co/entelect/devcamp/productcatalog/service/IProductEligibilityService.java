
package za.co.entelect.devcamp.productcatalog.service;

public interface IProductEligibilityService {

    boolean isCustomerEligible(String token, Long productId);
}

