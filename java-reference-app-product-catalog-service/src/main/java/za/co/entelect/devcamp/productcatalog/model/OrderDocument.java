package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name= "order_document", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class OrderDocument {
    @Id
    @Column(name="order_document_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_document_sequence")
    @SequenceGenerator(name = "order_document_sequence", sequenceName = "pc.order_document_sequence", allocationSize = 1)
    private Long orderDocumentId;

    @Column(name="customer_id", nullable = false)
    private Long customerId;

    @Column(name="document")
    private byte[] document;
}