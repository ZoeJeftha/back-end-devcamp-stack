
package za.co.entelect.devcamp.productcatalog.service;

import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;

public interface IProductEligibilityService {

    boolean isCustomerEligible(String token,String username,Long productId) throws NotFoundException, Exception;
}

