package za.co.entelect.devcamp.authenticationservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import za.co.entelect.devcamp.authenticationservice.requests.CreateCustomerRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CustomerApiClient {

    private final RestTemplate restTemplate;

    public CustomerApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer");
        String url = "http://devcamp-cis-service:8080/v1/customer";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCustomerRequest> entity =
                new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
                url,
                entity,
                Void.class
        );

        log.info("Customer created");
    }
}