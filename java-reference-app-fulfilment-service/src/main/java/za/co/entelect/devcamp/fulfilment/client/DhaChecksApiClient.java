package za.co.entelect.devcamp.fulfilment.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import za.co.entelect.devcamp.fulfilment.dha.api.DhaApi;
import za.co.entelect.devcamp.fulfilment.dha.ApiClient;
import za.co.entelect.devcamp.fulfilment.dha.model.DuplicateIDDocumentCheckResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatusResponse;
import za.co.entelect.devcamp.fulfilment.dha.model.LivingStatuses;
import za.co.entelect.devcamp.fulfilment.dha.model.MaritalStatusResponse;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaChecksApiClient;

@Slf4j
@Component
public class DhaChecksApiClient implements IDhaChecksApiClient
{
    private final ApiClient apiClient;

    public DhaChecksApiClient(RestTemplate dhaRestTemplate)
    {
        this.apiClient = new ApiClient(dhaRestTemplate);
    }

   @Override
    public MaritalStatusResponse DoMaritalCheck(String token, Long idNumber)
    { 
        apiClient.setBasePath("http://devcamp-dha-service:80");

        apiClient.addDefaultHeader(
                "Authorization",
                "Bearer " + token
        );

        DhaApi dhaApi = new DhaApi(apiClient);

        MaritalStatusResponse response =
                dhaApi.statusMaritalIdNumberGet(idNumber);

        return response;
    }

    @Override
    public DuplicateIDDocumentCheckResponse DoDuplicateIdCheck(String token, Long idNumber)
    {
        apiClient.setBasePath("http://devcamp-dha-service:80");

        apiClient.addDefaultHeader(
                "Authorization",
                "Bearer " + token
        );

        DhaApi dhaApi = new DhaApi(apiClient);

        DuplicateIDDocumentCheckResponse response =
                dhaApi.statusDuplicateIdIdNumberGet(idNumber);

        return response;
    }

    @Override
    public LivingStatusResponse DoLivingStatusCheck(String token, Long idNumber)
    {
        apiClient.setBasePath("http://devcamp-dha-service:80");

        apiClient.addDefaultHeader(
                "Authorization",
                "Bearer " + token
        );

        DhaApi dhaApi = new DhaApi(apiClient);

        LivingStatusResponse response =
                dhaApi.statusLivingIdNumberGet(idNumber);

        return response;
    }

}