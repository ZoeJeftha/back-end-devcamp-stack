package za.co.entelect.devcamp.authenticationservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterOrLoginResponse {
    public Boolean success;
    public String message;
    public String token;
}
