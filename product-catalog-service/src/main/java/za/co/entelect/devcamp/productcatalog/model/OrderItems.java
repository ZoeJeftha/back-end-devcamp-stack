package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name= "order_items", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItems {

    @Id
    @Column(name="order_items_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_sequence")
    @SequenceGenerator(name = "app_user_sequence", sequenceName = "app_user_sequence", allocationSize = 1)
    private Long orderItemsId;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="order_id", nullable = false)
    private Long orderId;
}