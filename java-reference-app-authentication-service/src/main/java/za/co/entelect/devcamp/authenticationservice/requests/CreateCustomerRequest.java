package za.co.entelect.devcamp.authenticationservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String idNumber;
    private int customerTypeId;
}