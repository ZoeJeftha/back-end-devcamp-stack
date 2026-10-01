package za.co.entelect.devcamp.fulfilment.configuration;

import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitRetryConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            RabbitMessageRecoverer recoverer,
            Jackson2JsonMessageConverter converter) {

        System.out.println("-----------------------Creating rabbitListenerContainerFactory !!!");
        System.out.println("-----------------------Recoverer = " + recoverer.getClass().getName());

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setMessageConverter(converter);

        factory.setDefaultRequeueRejected(false);

        RetryOperationsInterceptor interceptor =
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(6)
                        .backOffOptions(
                                1000,
                                2.0,
                                10000
                        )
                        .recoverer(recoverer)
                        .build();

        System.out.println("----------------------------------Retry interceptor created !!!");
        factory.setAdviceChain(interceptor);

        return factory;
    }

}