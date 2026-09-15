package za.co.entelect.devcamp.productcatalog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.productcatalog.model.OrderCustomerChecks;

@Repository
public interface OrderCustomerChecksRepository extends JpaRepository<OrderCustomerChecks, Long>
{
    List<OrderCustomerChecks> findByOrderId(Long orderId);
}
