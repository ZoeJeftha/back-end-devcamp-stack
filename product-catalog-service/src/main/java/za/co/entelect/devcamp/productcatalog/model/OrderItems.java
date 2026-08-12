package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name= "order_items", shcema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItems {

    @Column(name="product_id")
    private Long productId;

    @Column(name="order_id" nullable = false)
    private Long orderId;
}