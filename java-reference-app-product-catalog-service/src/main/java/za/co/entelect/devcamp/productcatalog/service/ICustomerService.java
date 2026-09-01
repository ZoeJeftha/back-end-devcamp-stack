
package za.co.entelect.devcamp.productcatalog.service;

import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;


public interface ICustomerService {

    ResponseEntity<ApiResponse<CustomerDto>> GetMyProfile(String token);
}

