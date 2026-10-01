package za.co.entelect.devcamp.fulfilment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.entelect.devcamp.fulfilment.enums.FailedMessageStatus;
import za.co.entelect.devcamp.fulfilment.model.FailedMessage;

import java.util.List;

public interface FailedMessageRepository extends JpaRepository<FailedMessage, Long> {

    List<FailedMessage> findByStatus(FailedMessageStatus status);

    boolean existsByMessageId(String messageId);
}