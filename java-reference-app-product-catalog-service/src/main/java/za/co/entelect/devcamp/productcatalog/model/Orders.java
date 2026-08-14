package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name= "orders", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    @Column(name="order_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_sequence")
    @SequenceGenerator(name = "app_user_sequence", sequenceName = "app_user_sequence", allocationSize = 1)
    private Long orderId;

    @Column(name="customer_id", nullable = false)
    private Long customerId;

    @Column(name="created_at")
    private LocalDateTime  createdAt;

    @Column(name="status", nullable = false)
    private String status;

    @Column(name="contract_url")
    private String contractUrl;
}