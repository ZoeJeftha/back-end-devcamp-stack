
package za.co.entelect.devcamp.productcatalog.client;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import za.co.entelect.devcamp.productcatalog.requests.LoginRequest;

public interface IAuthApiClient
{
    String GetSystemToken(LoginRequest request) throws Exception;
}

