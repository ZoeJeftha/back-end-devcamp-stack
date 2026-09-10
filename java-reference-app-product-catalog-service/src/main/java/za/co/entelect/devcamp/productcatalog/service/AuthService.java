package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.client.IAuthApiClient;
import za.co.entelect.devcamp.productcatalog.requests.LoginRequest;

@Service
public class AuthService implements IAuthService {

    private final String systemUsername;
    private final String systemPassword;
    private final IAuthApiClient authApiClient;

    @Autowired
    public AuthService(
            @Value("${auth.system-username}") String systemUsername,
            @Value("${auth.system-password}") String systemPassword,
            IAuthApiClient authApiClient)
    {
        this.systemUsername = systemUsername;
        this.systemPassword = systemPassword;
        this.authApiClient = authApiClient;
    }

    @Override
    public String GetSystemToken() throws Exception
    {
        try {
            LoginRequest request = new LoginRequest();
            request.setUsername(systemUsername);
            request.setPassword(systemPassword);

            String token = authApiClient.GetSystemToken(request);

            return token;
        }
        catch(Exception e)
        {
            System.out.println("--------------------Exception in auth api client: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}