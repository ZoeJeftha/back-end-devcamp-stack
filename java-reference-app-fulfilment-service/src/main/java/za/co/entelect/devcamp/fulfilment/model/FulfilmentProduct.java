package za.co.entelect.devcamp.fulfilment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name= "fulfilment_product", schema="fulfilment")
@AllArgsConstructor
@NoArgsConstructor
public class FulfilmentProduct {
    @Id
    @Column(name="fulfilment_product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fulfilment_sequence")
    @SequenceGenerator(name = "fulfilment_sequence", sequenceName = "fulfilment.fulfilment_sequence", allocationSize = 1)
    private Long fulfilment_product_id;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="fulfilment_type")
    private String fulfilmentType;
}