package za.co.entelect.devcamp.customerinformationstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table( name = "customerAccounts", schema = "cis")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_accounts_sequence")
    @SequenceGenerator(name = "customer_accounts_sequence", sequenceName = "cis.customer_accounts_sequence", allocationSize = 1)
    private Long customerAccountsId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountTypeId")
    private AccountType accountType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId")
    private Customer customer;

    public CustomerAccounts(AccountType accountType, Customer customer) {
        this.accountType = accountType;
        this.customer = customer;
    }
}
