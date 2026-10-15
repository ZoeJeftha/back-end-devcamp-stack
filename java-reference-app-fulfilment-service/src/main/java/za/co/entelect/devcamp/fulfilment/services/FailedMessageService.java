package za.co.entelect.devcamp.fulfilment.services;

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

    public FailedMessageService(
            FailedMessageRepository failedMessageRepository,
            ObjectMapper objectMapper) {

        this.failedMessageRepository = failedMessageRepository;
    }

    public void saveFailedMessage(
            String messageId,
            String originalQueue,
            byte[] payload,
            String failureReason,
            int retryCount) {

        if (failedMessageRepository.existsByMessageId(messageId)) {
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

        failedMessageRepository.save(failedMessage);
    }
}
