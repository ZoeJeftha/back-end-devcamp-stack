package za.co.entelect.devcamp.fulfilment.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import za.co.entelect.devcamp.fulfilment.interfaces.IAuthApiClient;
import za.co.entelect.devcamp.fulfilment.requests.LoginRequest;

@Slf4j
@Component
public class AuthApiClient implements IAuthApiClient
{
    private final RestTemplate restTemplate;

    public AuthApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String GetSystemToken(LoginRequest request) throws Exception {
        try {
            log.info("getting system token");
            String url = "http://devcamp-auth-service:8080/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.setBasicAuth(
                    request.getUsername(),
                    request.getPassword()
            );

            HttpEntity<Void> entity =
                    new HttpEntity<>(headers);

            ResponseEntity<String> token = restTemplate.postForEntity(
                    url,
                    entity,
                    String.class
            );

            return token.getBody();
        }
        catch(Exception e)
        {
            System.out.println("--------------------Exception in auth api client: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}