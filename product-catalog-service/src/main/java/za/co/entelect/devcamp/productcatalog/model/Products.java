package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name= "products", shcema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Id
    @Column(name="product_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_sequence")
    @SequenceGenerator(name = "app_user_sequence", sequenceName = "app_user_sequence", allocationSize = 1)
    private Long productId;

    @Column(name="name" nullable = false)
    private String name;

    @Column(name="description" nullable = false)
    private String description;

    @Column(name="price" nullable = false)
    private BigDecimal price;

    @Column(name="image_url" nullable = false)
    private String imageUrl;
}