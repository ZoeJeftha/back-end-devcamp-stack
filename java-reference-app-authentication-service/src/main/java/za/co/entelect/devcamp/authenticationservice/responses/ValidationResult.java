package za.co.entelect.devcamp.authenticationservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResult {
    private boolean valid;
    private String role;

    public boolean getValid()
    {
        return valid;
    }

    public String getRole()
    {
        return role;
    }
}