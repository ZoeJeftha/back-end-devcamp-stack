package za.co.entelect.devcamp.customerinformationstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table( name = "customerDocuments", schema = "cis")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_documents_sequence")
    @SequenceGenerator(name = "customer_documents_sequence", sequenceName = "cis.customer_documents_sequence", allocationSize = 1)
    private Long customerDocumentsId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "documentId")
    private Document document;

    public CustomerDocuments(Customer customer, Document document) {
        this.customer = customer;
        this.document = document;
    }
}
