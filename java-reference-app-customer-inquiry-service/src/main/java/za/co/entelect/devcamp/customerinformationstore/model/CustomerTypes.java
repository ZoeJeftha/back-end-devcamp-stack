package za.co.entelect.devcamp.customerinformationstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table( name = "customerTypes", schema = "cis")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypes {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "customer_types_sequence")
    @SequenceGenerator(name = "customer_types_sequence", sequenceName = "cis.customer_types_sequence", allocationSize = 1)
    private Long customerTypesId;

    @Column( nullable = false)
    private String name;

    @Column ( nullable = false)
    private String description;

    @OneToMany(mappedBy = "customerTypes", fetch = FetchType.LAZY)
    private List<Customer> customer;

    public CustomerTypes(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CustomerTypes(CustomerTypesDto customerTypesDto) {
        this.name = customerTypesDto.getName();
        this.description = customerTypesDto.getDescription();
    }

    public CustomerTypesDto toCustomerTypesDto() {
        CustomerTypesDto customerTypesDto = new CustomerTypesDto(this.name, this.description);
        customerTypesDto.setId(this.customerTypesId.intValue());
        return customerTypesDto;

    }

}
