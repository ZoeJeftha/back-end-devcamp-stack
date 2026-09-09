
package za.co.entelect.devcamp.fulfilment.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import za.co.entelect.devcamp.fulfilment.requests.LoginRequest;

public interface IAuthApiClient
{
    String GetSystemToken(LoginRequest request) throws Exception;
}

