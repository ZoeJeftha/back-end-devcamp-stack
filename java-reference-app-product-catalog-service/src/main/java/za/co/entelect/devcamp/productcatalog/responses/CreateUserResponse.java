package za.co.entelect.devcamp.productcatalog.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    public UserDto user;
    public CustomerDto customer;
}
