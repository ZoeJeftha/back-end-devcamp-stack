
package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.UserDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.requests.CreateUserRequest;
import za.co.entelect.devcamp.productcatalog.requests.LoginRequest;
import za.co.entelect.devcamp.productcatalog.requests.RegisterRequest;
import za.co.entelect.devcamp.productcatalog.responses.CreateUserResponse;
import za.co.entelect.devcamp.productcatalog.responses.ValidationResult;

public interface IUserService
{
    UserDto CreateUser(CreateUserRequest request) throws Exception;

    UserDto LoadUserByUsername(String username) throws NotFoundException;

    CreateUserResponse RegisterUser(RegisterRequest request) throws Exception;

    String GetToken(LoginRequest loginRequest) throws BadCredentialsException, Exception;
}

