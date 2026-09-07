package za.co.entelect.devcamp.productcatalog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.productcatalog.model.OrderItems;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Long>
{
    Optional<OrderItems> findByOrderId(Long orderId);
}
