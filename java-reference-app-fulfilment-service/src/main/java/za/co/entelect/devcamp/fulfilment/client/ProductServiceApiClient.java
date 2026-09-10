package za.co.entelect.devcamp.fulfilment.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.interfaces.IProductServiceApiClient;
import za.co.entelect.devcamp.productcatalog.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.fulfilment.responses.OrderResponse;

@Slf4j
@Component
public class ProductServiceApiClient implements IProductServiceApiClient
{
    private final RestTemplate restTemplate;

    public ProductServiceApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<OrderResponse> UpdateOrder(String token, OrderStatusUpdateRequest request) {
        String url = "http://devcamp-pc-service:8080/v1/order-status-update/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);


        HttpEntity<OrderStatusUpdateRequest> entity =
                new HttpEntity<>(request,headers);

        ResponseEntity<OrderResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        OrderResponse.class
                );

        log.info("fulfilment response: "+ response)
        return response.getBody();
    }

}