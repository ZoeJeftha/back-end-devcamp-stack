package za.co.entelect.devcamp.fulfilment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.enums.FailedMessageStatus;
import za.co.entelect.devcamp.fulfilment.model.FailedMessage;
import za.co.entelect.devcamp.fulfilment.repository.FailedMessageRepository;

@Slf4j
@Service
public class FailedMessageService {

    private final FailedMessageRepository failedMessageRepository;
    private final ObjectMapper objectMapper;

    public FailedMessageService(
            FailedMessageRepository failedMessageRepository,
            ObjectMapper objectMapper) {

        this.failedMessageRepository = failedMessageRepository;
        this.objectMapper = objectMapper;
    }

    public void saveFailedMessage(
            String messageId,
            String originalQueue,
            byte[] payload,
            String failureReason,
            int retryCount) {

        log.info("---------------saveFailedMessage");
        if (failedMessageRepository.existsByMessageId(messageId)) {
            log.info("---------------saveFailedMessage existsByMessageId: " +messageId );
            return;
        }

        FailedMessage failedMessage = new FailedMessage();

        failedMessage.setMessageId(messageId);
        failedMessage.setOriginalQueue(originalQueue);
        failedMessage.setPayload(new String(payload, StandardCharsets.UTF_8));
        failedMessage.setFailureReason(failureReason);
        failedMessage.setRetryCount(retryCount);
        failedMessage.setStatus(FailedMessageStatus.FAILED);
        failedMessage.setCreatedAt(LocalDateTime.now());

        log.info("---------------saveFailedMessage messageId: " +messageId );
        log.info("---------------saveFailedMessage failedMessage: " +failedMessage );

        failedMessageRepository.save(failedMessage);
    }

    private String convertToJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException exception) {
            log.error("---------------convertToJson payload: " + payload);
            throw new IllegalStateException(
                    "Unable to convert failed message to JSON",
                    exception
            );
        }
    }
}
