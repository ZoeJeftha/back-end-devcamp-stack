package za.co.entelect.devcamp.customerinformationstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Base64;
import java.util.List;

@Data
@Entity
@Table( name = "document", schema = "cis")
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_sequence")
    @SequenceGenerator(name = "document_sequence", sequenceName = "cis.document_sequence", allocationSize = 1)
    private Long documentId;

    @Column (nullable = false)
    private byte[] document;

    @Column (nullable = false)
    private String name;

    @OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
    private List<CustomerDocuments> customerDocumentsList;

    public DocumentDto toDocumentDto() {
        return new DocumentDto(documentId.intValue(), name,  Base64.getEncoder().encodeToString(document));
    }
    public DocumentsDtoInner toDocumentsDto() {
        return new DocumentsDtoInner(documentId.intValue(), name);
    }

    public Document(DocumentDto documentDto) {
        this.document = documentDto.getDocument().getBytes();
        this.name = documentDto.getName();
    }
}
