package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name= "qualifying_accounts", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class QualifyingAccounts {
    @Id
    @Column(name="qualifying_accounts_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qualifying_accounts_sequence")
    @SequenceGenerator(name = "qualifying_accounts_sequence", sequenceName = "qualifying_accounts_sequence", schema="pc", allocationSize = 1)
    private Long qualifyingAccountsId;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="account_id", nullable = false)
    private Long accountId;
}