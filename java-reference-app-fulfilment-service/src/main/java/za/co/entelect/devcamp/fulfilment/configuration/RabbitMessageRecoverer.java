package za.co.entelect.devcamp.fulfilment.configuration;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.fulfilment.services.FailedMessageService;

@Slf4j
@Component
public class RabbitMessageRecoverer implements MessageRecoverer {

    private final RabbitTemplate rabbitTemplate;
    private final FailedMessageService failedMessageService;

    public RabbitMessageRecoverer(
            RabbitTemplate rabbitTemplate,
            FailedMessageService failedMessageService) {

        this.rabbitTemplate = rabbitTemplate;
        this.failedMessageService = failedMessageService;
    }

    @Override
    public void recover(Message message, Throwable cause) {

        try {
            String messageId =
                    message.getMessageProperties().getMessageId();

            if (messageId == null) {
                messageId = UUID.randomUUID().toString();
            }

           failedMessageService.saveFailedMessage(
                    messageId,
                    RabbitConfig.QUEUE,
                    message.getBody(),
                    cause.getMessage(),
                    6
            );

            rabbitTemplate.send(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.DLQ_ROUTING_KEY,
                    message
            );

        } catch(Exception e)
        {
            log.error("Error inside rabbit message recoverer");
            throw e;
        }
    }
}