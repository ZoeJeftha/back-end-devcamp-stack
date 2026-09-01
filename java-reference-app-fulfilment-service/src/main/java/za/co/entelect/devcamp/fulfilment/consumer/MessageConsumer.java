package za.co.entelect.devcamp.fulfilment.consumer;

import za.co.entelect.devcamp.fulfilment.configuration.RabbitConfig;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import za.co.entelect.devcamp.fulfilment.dto.CustomerDto;

@Service
public class MessageConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receiveMessage(CustomerDto customerDto) {
        System.out.println("------------------Received: " + customerDto);
    }
}
