
package za.co.entelect.devcamp.productcatalog.client;

import java.util.List;
import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;

public interface ICustomerApiClient
{
    ResponseEntity<CustomerDto> GetMyProfile(String token, String username) throws NotFoundException, Exception;

    ResponseEntity<List<CustomerDto>> GetProfiles(String token) throws NotFoundException, Exception;

    ResponseEntity<CustomerDto> CreateCustomer(String token,CustomerDto customerDto);
}

