package za.co.entelect.devcamp.productcatalog.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String role;
    private Long customerTypeId;
}