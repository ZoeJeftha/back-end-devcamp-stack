package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name= "fulfilment_type", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class FulfilmentType {

    @Id
    @Column(name="fulfilment_type_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_sequence")
    @SequenceGenerator(name = "app_user_sequence", sequenceName = "app_user_sequence", allocationSize = 1)
    private Long fulfilmentTypeId;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="name",  nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;
}