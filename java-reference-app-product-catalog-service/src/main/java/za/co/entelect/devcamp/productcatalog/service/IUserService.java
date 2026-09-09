
package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.productcatalog.dto.UserDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.requests.CreateUserRequest;

public interface IUserService
{
    UserDto CreateUser(CreateUserRequest request) throws Exception;

    UserDto LoadUserByUsername(String username) throws NotFoundException;
}

