package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name= "order_customer_checks", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomerChecks {
    @Id
    @Column(name="order_customer_checks_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_customer_checks_sequence")
    @SequenceGenerator(name = "order_customer_checks_sequence", sequenceName = "pc.order_customer_checks_sequence", allocationSize = 1)
    private Long orderCustomerChecksId;

    @Column(name="order_id", nullable = false)
    private Long orderId;

    @Column(name="customer_checks_id")
    private Long customerChecksId;

    @Column(name="has_passed", nullable = false)
    private Boolean hasPassed;
}