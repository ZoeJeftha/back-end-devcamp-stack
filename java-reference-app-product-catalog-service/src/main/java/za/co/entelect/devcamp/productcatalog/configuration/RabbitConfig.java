package za.co.entelect.devcamp.productcatalog.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitConfig {
    public static final String QUEUE = "customer-checks.queue";
    public static final String EXCHANGE = "customer-checks.exchange";
    public static final String ROUTING_KEY = "customer-checks.routing.key";

    @Bean
    public Queue queue() { return new Queue(QUEUE, true); }

    @Bean
    public DirectExchange exchange() { return new DirectExchange(EXCHANGE); }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
