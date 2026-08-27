package za.co.entelect.devcamp.productcatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.productcatalog.model.QualifyingAccounts;

import java.util.List;

@Repository
public interface QualifyingAccountsRepository extends JpaRepository<QualifyingAccounts, Long> {

    boolean existsByProductIdAndAccountId(
            Long productId,
            Long accountId
    );
}
