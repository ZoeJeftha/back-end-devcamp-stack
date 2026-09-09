package za.co.entelect.devcamp.fulfilment.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate dhaRestTemplate() {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JsonNullableModule());
        objectMapper.registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(objectMapper);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().removeIf(
                converterItem -> converterItem instanceof MappingJackson2HttpMessageConverter
        );

        restTemplate.getMessageConverters().add(converter);

        return restTemplate;
    }
}