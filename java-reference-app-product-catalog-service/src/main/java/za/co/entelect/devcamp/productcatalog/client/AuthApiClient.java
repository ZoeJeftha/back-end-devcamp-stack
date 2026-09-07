package za.co.entelect.devcamp.productcatalog.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;

@Slf4j
@Component
public class AuthApiClient implements IAuthApiClient
{
    private final RestTemplate restTemplate;

    public AuthApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<CustomerDto> GetMyProfile(String token, String username) throws NotFoundException, Exception
    {
        try {
            String url = "http://devcamp-cis-service:8080/v1/customer?emailAddress=" + username; //username@gmail.com";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CustomerDto> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<CustomerDto>() {
                            }
                    );

            return response;
        }
        catch(HttpClientErrorException e)
        {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                throw new NotFoundException(e.getMessage());
            }
            else
            {
                throw new Exception(e.getMessage());
            }
        }
    }
}