package za.co.entelect.devcamp.productcatalog.client;

import java.util.List;
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
import za.co.entelect.devcamp.productcatalog.requests.RegisterRequest;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.service.IAuthService;

@Slf4j
@Component
public class CustomerApiClient implements ICustomerApiClient
{
    private final RestTemplate restTemplate;
    private final IAuthService authService;

    public CustomerApiClient(RestTemplate restTemplate,
                             IAuthService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<CustomerDto> GetMyProfile(String token, String username) throws NotFoundException, Exception
    {
        try {
            String url = "http://devcamp-cis-service:8080/v1/customer?emailAddress=" + username;
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

    @Override
    public ResponseEntity<List<CustomerDto>> GetProfiles(String token) throws NotFoundException, Exception
    {
        try {
            String url = "http://devcamp-cis-service:8080/v1/customers";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List<CustomerDto>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<List<CustomerDto>>() {
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

    @Override
    public ResponseEntity<CustomerDto> CreateCustomer(CustomerDto request) throws Exception {
        log.info("Creating customer");
        try {
            String token = authService.GetSystemToken();

            String url = "http://devcamp-cis-service:8080/v1/customer";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<CustomerDto> entity =
                    new HttpEntity<>(request, headers);

            ResponseEntity<CustomerDto> customer = restTemplate.postForEntity(
                    url,
                    entity,
                    CustomerDto.class
            );

            log.info("Customer created");
            return customer;
        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CustomerDto> OpenAccount(String token2, String username, Integer accountTypeId) throws Exception {
        log.info("Open Account");
        try
        {
            String token = authService.GetSystemToken();
            log.info("customer "+ username+ " accountTypeId" +accountTypeId + "token"+ token);

            ResponseEntity<CustomerDto> customerDto = GetMyProfile(token, username);
            CustomerDto customer = customerDto.getBody();
            log.info("customer "+ customer);
            log.info("customer.getId() "+ customer.getId());

            String url = "http://devcamp-cis-service:8080/v1/customer/"
                    + customer.getId()
                    + "/accounts/"
                    + accountTypeId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<Void> entity =
                    new HttpEntity<>(headers);

            ResponseEntity<Void> savedCustomer = restTemplate.postForEntity(
                    url,
                    entity,
                    Void.class
            );
            token = authService.GetSystemToken();
            return GetMyProfile(token, username);
        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}