
package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;

public interface IAuthService
{
    String GetSystemToken() throws Exception;
}

