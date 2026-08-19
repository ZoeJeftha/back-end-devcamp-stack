package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name= "qualifying_customer_types", schema = "pc")
@AllArgsConstructor
@NoArgsConstructor
public class QualifyingCustomerTypes {
    @Id
    @Column(name="qualifying_customer_types_id", nullable=false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qualifying_customer_types_sequence")
    @SequenceGenerator(name = "qualifying_customer_types_sequence", sequenceName = "qualifying_customer_types_sequence",  schema="pc",allocationSize = 1)
    private Long qualifyingCustomerTypesId;

    @Column(name="product_id", nullable=false)
    private Long productId;

    @Column(name="customer_types_id", nullable=false)
    private Long customerTypesId;
}