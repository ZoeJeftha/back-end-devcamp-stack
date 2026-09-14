package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name= "customer_checks", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerChecks {
    @Id
    @Column(name="customer_checks_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_checks_sequence")
    @SequenceGenerator(name = "customer_checks_sequence", sequenceName = "pc.customer_checks_sequence", allocationSize = 1)
    private Long customerChecksId;

    @Column(name="description", nullable = false)
    private String description;
}