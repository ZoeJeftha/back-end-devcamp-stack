
package za.co.entelect.devcamp.productcatalog.service;

import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;

public interface ICustomerService {

    CustomerDto GetMyProfile(String token, String username) throws NotFoundException, Exception;
}

