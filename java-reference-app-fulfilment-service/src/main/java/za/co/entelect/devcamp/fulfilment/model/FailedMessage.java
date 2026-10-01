package za.co.entelect.devcamp.fulfilment.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import za.co.entelect.devcamp.fulfilment.enums.FailedMessageStatus;

@Data
@Entity
@Table(name= "failed_messages", schema="f")
@AllArgsConstructor
@NoArgsConstructor
public class FailedMessage {
    @Id
    @Column(name="failed_message_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "failed_message_sequence")
    @SequenceGenerator(name = "failed_message_sequence", sequenceName = "f.failed_message_sequence", allocationSize = 1)
    private Long failedMessageId;

    @Column(name="message_id", nullable = false,unique = true)
    private String messageId;

    @Column(name="original_queue",  nullable = false)
    private String originalQueue;

    @Column(name="payload" ,nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name="failure_reason",columnDefinition = "TEXT")
    private String failureReason;

    @Column(name="retry_count")
    private Integer retryCount;

    @Enumerated(EnumType.STRING)
    private FailedMessageStatus status;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="replayed_at")
    private LocalDateTime replayedAt;

    @Column(name="replayed_by")
    private String replayedBy;
}