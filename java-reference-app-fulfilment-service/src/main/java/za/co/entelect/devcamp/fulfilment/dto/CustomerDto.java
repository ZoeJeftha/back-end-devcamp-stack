package za.co.entelect.devcamp.fulfilment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String idNumber;
    private Long customerTypeId;
    private CustomerTypesDto customerType;
    private List<AccountsDto> customerAccounts;
}