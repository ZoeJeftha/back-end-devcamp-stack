package za.co.entelect.devcamp.customerinformationstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.customerinformationstore.model.CustomerDocuments;

import java.util.List;

@Repository
public interface CustomerDocumentRepository extends JpaRepository<CustomerDocuments, Long> {

    List<CustomerDocuments> findCustomerDocumentsByCustomer_CustomerId(Long customerId);
    CustomerDocuments findCustomerDocumentsByCustomer_CustomerIdAndDocument_DocumentId(Long customerId, Long documentId);

}
