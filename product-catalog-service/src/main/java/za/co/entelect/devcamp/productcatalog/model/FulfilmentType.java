package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name= "fulfilment_type", shcema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class Products {

    @Column(name="product_id"
    private Long productId;

    @Column(name="name" nullable = false)
    private String name;

    @Column(name="description" nullable = false)
    private String description;
}