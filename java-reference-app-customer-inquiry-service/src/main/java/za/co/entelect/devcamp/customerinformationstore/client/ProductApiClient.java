package za.co.entelect.devcamp.customerinformationstore.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import za.co.entelect.devcamp.customerinformationstore.requests.CustomerEligibilityRequest;
import za.co.entelect.devcamp.customerinformationstore.responses.ApiResponse;

@Slf4j
@Component
public class ProductApiClient {

    private final RestTemplate restTemplate;

    public ProductApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<ApiResponse<Boolean>> IsCustomerEligible(String token, CustomerEligibilityRequest request) {
        String url = "http://devcamp-pc-service:8080/v1/customer-eligibility-check";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<CustomerEligibilityRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<ApiResponse<Boolean>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<ApiResponse<Boolean>>() {}
                );

        return response;
    }
}