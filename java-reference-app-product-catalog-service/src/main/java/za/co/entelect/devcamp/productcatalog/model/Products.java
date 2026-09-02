package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name= "products", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Id
    @Column(name="product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_sequence")
    @SequenceGenerator(name = "products_sequence", sequenceName = "pc.products_sequence", allocationSize = 1)
    private Long productId;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="price", nullable = false)
    private BigDecimal price;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="fulfilment_type")
    private String fulfilmentType;
}