package za.co.entelect.devcamp.fulfilment.configuration;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitRetryConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            RabbitMessageRecoverer recoverer) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        // Don't put the failed message back onto the original queue
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

        factory.setAdviceChain(interceptor);

        return factory;
    }
}