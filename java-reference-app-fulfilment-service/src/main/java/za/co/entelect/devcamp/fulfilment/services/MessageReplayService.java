package za.co.entelect.devcamp.fulfilment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;

import net.bytebuddy.asm.Advice;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.entelect.devcamp.fulfilment.configuration.RabbitConfig;
import za.co.entelect.devcamp.fulfilment.model.FailedMessage;
import za.co.entelect.devcamp.fulfilment.enums.FailedMessageStatus;
import za.co.entelect.devcamp.fulfilment.repository.FailedMessageRepository;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;

@Service
public class MessageReplayService {

    private final FailedMessageRepository failedMessageRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;


    public MessageReplayService(
            FailedMessageRepository failedMessageRepository,
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper) {

        this.failedMessageRepository = failedMessageRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void replay(String username)
    {
        List<FailedMessage> failedMessages = failedMessageRepository.findByStatus(FailedMessageStatus.REPLAY_FAILED);
        failedMessages.addAll(failedMessageRepository.findByStatus(FailedMessageStatus.FAILED));

        for(FailedMessage failedMessage:failedMessages) {
            try {
                FulfilmentRequest request =
                        objectMapper.readValue(
                                failedMessage.getPayload(),
                                FulfilmentRequest.class
                        );

                rabbitTemplate.convertAndSend(
                        RabbitConfig.EXCHANGE,
                        RabbitConfig.ROUTING_KEY,
                        request
                );


                failedMessage.setStatus(FailedMessageStatus.REPLAYED);
                failedMessage.setReplayedAt(LocalDateTime.now());
                failedMessage.setReplayedBy(username);

                failedMessageRepository.save(failedMessage);
            } catch (JsonProcessingException e) {
                failedMessage.setStatus(
                        FailedMessageStatus.REPLAY_FAILED
                );

                failedMessageRepository.save(failedMessage);
            }
        }
    }

}
