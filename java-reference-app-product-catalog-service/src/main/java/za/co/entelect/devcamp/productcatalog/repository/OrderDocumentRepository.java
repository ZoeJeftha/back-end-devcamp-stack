package za.co.entelect.devcamp.productcatalog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.productcatalog.model.OrderDocument;

@Repository
public interface OrderDocumentRepository extends JpaRepository<OrderDocument, Long>
{
    Optional<OrderDocument> findByCustomerId(Long orderId);
}
