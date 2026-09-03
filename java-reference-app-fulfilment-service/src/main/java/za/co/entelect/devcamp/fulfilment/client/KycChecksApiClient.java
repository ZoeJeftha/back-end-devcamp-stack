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
import za.co.entelect.devcamp.fulfilment.interfaces.IKycChecksApiClient;

@Slf4j
@Component
public class KycChecksApiClient implements IKycChecksApiClient {
    private final RestTemplate restTemplate;

    public KycChecksApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public KycDto DoKycCheck(String token, Long customerId) {
        String url = "http://devcamp-kyc-service:80/kyc/" + customerId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Long> entity =
                new HttpEntity<>(headers);

        ResponseEntity<KycDto> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<KycDto>() {
                        }
                );

        return response.getBody();
    }

}