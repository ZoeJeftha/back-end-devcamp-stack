package za.co.entelect.devcamp.productcatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.productcatalog.model.QualifyingCustomerTypes;

import java.util.List;

@Repository
public interface QualifyingCustomerTypesRepository extends JpaRepository<QualifyingCustomerTypes, Long> {

    boolean existsByProductIdAndCustomerTypesId(
            Long productId,
            Long customerTypesId
    );
}
