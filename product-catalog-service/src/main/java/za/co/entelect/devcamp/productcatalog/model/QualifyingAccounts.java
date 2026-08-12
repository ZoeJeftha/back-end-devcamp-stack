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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_sequence")
    @SequenceGenerator(name = "app_user_sequence", sequenceName = "app_user_sequence", allocationSize = 1)
    private Long qualifyingAccountsId;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="account_id", nullable = false)
    private Long accountId;
}