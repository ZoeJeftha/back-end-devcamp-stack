package za.co.entelect.devcamp.fulfilment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import za.co.entelect.devcamp.fulfilment.dha.ApiClient;
import za.co.entelect.devcamp.fulfilment.dha.api.DhaApi;

@Configuration
public class DhaApiConfig {

    @Bean
    public ApiClient dhaApiClient() {
        ApiClient apiClient = new ApiClient();

        apiClient.setBasePath("http://devcamp-dha-service:80");

        return apiClient;
    }

    @Bean
    public DhaApi dhaApi(ApiClient apiClient) {
        return new DhaApi(apiClient);
    }
}