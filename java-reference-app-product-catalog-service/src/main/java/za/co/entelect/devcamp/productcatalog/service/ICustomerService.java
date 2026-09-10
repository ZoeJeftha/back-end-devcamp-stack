
package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;

public interface ICustomerService {

    CustomerDto GetMyProfile(String token, String username) throws NotFoundException, Exception;

    List<CustomerDto> GetProfiles(String token) throws NotFoundException, Exception;

    CustomerDto CreateCustomer(CustomerDto request) throws Exception;

    CustomerDto OpenAccount(String token, String username, Integer accountTypeId) throws Exception;
}

