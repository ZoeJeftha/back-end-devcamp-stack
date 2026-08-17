package za.co.entelect.devcamp.customerinformationstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "customer", schema = "cis")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    @SequenceGenerator(name = "customer_sequence", sequenceName = "cis.customer_sequence", allocationSize = 1)
    private Long customerId;

    @Column(nullable = false)
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String idNumber;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customerTypesId")
    private CustomerTypes customerTypes;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<CustomerDocuments> customerDocuments;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<CustomerAccounts> customerAccounts;

    public Customer(CustomerDto customerDto) {
        this.email = customerDto.getUsername();
        this.firstName = customerDto.getFirstName();
        this.lastName = customerDto.getLastName();
        this.idNumber = customerDto.getIdNumber();
    }

    public Customer(String email, String firstName, String lastName, String idNumber, CustomerTypes customerTypes) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerTypes = customerTypes;
        this.idNumber = idNumber;
    }

    public CustomerDto toCustomerDto() {
        CustomerDto customerDto = new CustomerDto(this.email, this.firstName, this.lastName, this.idNumber);
        customerDto.setCustomerType(this.customerTypes != null ? this.customerTypes.toCustomerTypesDto() : null);
        if (this.customerAccounts != null) {
            List<AccountTypeDto> accountTypes = new ArrayList<>();
            for (CustomerAccounts customerAccount : this.customerAccounts) {
                AccountTypeDto accountTypeDto = customerAccount.getAccountType().toAccountTypeDto();
                accountTypes.add(accountTypeDto);
            }
            customerDto.setCustomerAccounts(accountTypes);
        }
        customerDto.setId(this.customerId.intValue());
        return customerDto;
    }
}
