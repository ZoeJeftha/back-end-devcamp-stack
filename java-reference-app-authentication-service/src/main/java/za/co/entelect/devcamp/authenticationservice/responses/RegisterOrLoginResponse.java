package za.co.entelect.devcamp.authenticationservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterOrLoginResponse {
    public Boolean success;
    public String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String token;
}
