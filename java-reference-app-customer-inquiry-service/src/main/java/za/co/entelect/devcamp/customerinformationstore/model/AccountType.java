package za.co.entelect.devcamp.customerinformationstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table( name = "accountType", schema = "cis")
@AllArgsConstructor
@NoArgsConstructor
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_type_sequence")
    @SequenceGenerator(name = "account_type_sequence", sequenceName = "cis.account_type_sequence", allocationSize = 1)
    private Long accountTypeId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "accountType", fetch = FetchType.LAZY)
    private List<CustomerAccounts> customerAccounts;

    public AccountType(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public AccountTypeDto toAccountTypeDto() {
        return new AccountTypeDto(BigDecimal.valueOf(this.accountTypeId), this.name, this.description);
    }

}
