package za.co.entelect.devcamp.productcatalog.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.configuration.RabbitConfig;
import za.co.entelect.devcamp.productcatalog.request.FulfilmentRequest;

@Slf4j
@Service
public class MessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void SendMessage(FulfilmentRequest fulfilmentRequest) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                fulfilmentRequest);
    }
}