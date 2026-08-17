package za.co.entelect.devcamp.customerinformationstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.customerinformationstore.model.AccountType;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerAccounts;

import java.util.List;

@Repository
public interface CustomerAccountsRepository extends JpaRepository<CustomerAccounts, Long> {

    List<CustomerAccounts> findCustomerAccountsByCustomer_CustomerId(Long customerId);
}
