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

        log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        log.error("!!! RABBIT MESSAGE RECOVERER CALLED !!!");
        log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        try {
            log.error("------------------------1. Getting message properties");

            String messageId =
                    message.getMessageProperties().getMessageId();

            log.error("------------------------2. Message ID from RabbitMQ: {}", messageId);

            if (messageId == null) {
                messageId = UUID.randomUUID().toString();
                log.error("3. Generated message ID: {}", messageId);
            }

            log.error("------------------------4. Calling failedMessageService.saveFailedMessage()");

            failedMessageService.saveFailedMessage(
                    messageId,
                    RabbitConfig.QUEUE,
                    message.getBody(),
                    cause.getMessage(),
                    6
            );

            log.error("------------------------5. Failed message saved successfully");

            log.error("------------------------6. Sending message to DLQ");

            rabbitTemplate.send(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.DLQ_ROUTING_KEY,
                    message
            );

            log.error("------------------------7. Message sent to DLQ");
        } catch(Exception e)
        {
            log.error(
                    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
            );

            log.error(
                    "!!! ERROR INSIDE RABBIT MESSAGE RECOVERER !!!",
                    e
            );

            log.error(
                    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
            );

            throw e;
        }
    }
}