package za.co.entelect.devcamp.productcatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.productcatalog.model.Products;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
}
