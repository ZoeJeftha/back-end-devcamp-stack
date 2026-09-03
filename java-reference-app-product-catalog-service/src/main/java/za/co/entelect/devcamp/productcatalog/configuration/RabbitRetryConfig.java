package za.co.entelect.devcamp.productcatalog.configuration;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitRetryConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            RabbitMessageRecoverer recoverer,
            Jackson2JsonMessageConverter converter) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        // Use JSON conversion for RabbitMQ messages
        factory.setMessageConverter(converter);

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