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
import za.co.entelect.devcamp.fulfilment.dto.DuplicateIdStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.LivingStatusDto;
import za.co.entelect.devcamp.fulfilment.dto.MaritalStatusesDto;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaChecksApiClient;

@Slf4j
@Component
public class DhaChecksApiClient implements IDhaChecksApiClient
{
    private final RestTemplate restTemplate;

    public DhaChecksApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public MaritalStatusesDto DoMaritalCheck(String token, Long idNumber)
    {
        String url = "http://devcamp-dha-service:80/status/marital/" + idNumber;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Long> entity =
                new HttpEntity<>(headers);

        ResponseEntity<MaritalStatusesDto> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<MaritalStatusesDto>() {}
                );

        return response.getBody();
    }

    @Override
    public DuplicateIdStatusDto DoDuplicateIdCheck(String token, Long idNumber)
    {
        String url = "http://devcamp-dha-service:80/status/duplicateId/" + idNumber;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Long> entity =
                new HttpEntity<>(headers);

        ResponseEntity<DuplicateIdStatusDto> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<DuplicateIdStatusDto>() {}
                );

        return response.getBody();
    }

    @Override
    public LivingStatusDto DoLivingStatusCheck(String token, Long idNumber)
    {
        String url = "http://devcamp-dha-service:80/status/living/" + idNumber;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Long> entity =
                new HttpEntity<>(headers);

        ResponseEntity<LivingStatusDto> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<LivingStatusDto>() {}
                );

        return response.getBody();
    }
}