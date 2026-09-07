package za.co.entelect.devcamp.productcatalog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.productcatalog.model.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long>
{
    public Optional<List<Orders>> findByCustomerId(Long customerId);
}
