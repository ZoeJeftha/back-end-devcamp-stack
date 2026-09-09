package za.co.entelect.devcamp.productcatalog.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.client.IAuthApiClient;
import za.co.entelect.devcamp.productcatalog.dto.UserDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.model.User;
import za.co.entelect.devcamp.productcatalog.repository.UserRepository;
import za.co.entelect.devcamp.productcatalog.requests.CreateUserRequest;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto CreateUser(CreateUserRequest request) throws Exception
    {
        try
        {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());

            User savedUser = userRepository.save(user);
            return toUserDto(savedUser);
        }
        catch(Exception e)
        {
            System.out.println("--------------------Exception create user service: " + e.getMessage());
            throw new Exception("Failed to create user: "+ e.getMessage());
        }
    }

    @Override
    public UserDto LoadUserByUsername(String username)
            throws NotFoundException {

        User user = userRepository
                        .findFirstByEmailIgnoreCase(username)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User not found"));
        return toUserDto(user);

    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getEmail(),
                user.getRole()
        );
    }
}