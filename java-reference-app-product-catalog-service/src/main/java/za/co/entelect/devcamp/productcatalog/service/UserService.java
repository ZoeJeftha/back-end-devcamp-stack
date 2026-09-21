package za.co.entelect.devcamp.productcatalog.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.productcatalog.client.IAuthApiClient;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.dto.UserDto;
import za.co.entelect.devcamp.productcatalog.exception.NotFoundException;
import za.co.entelect.devcamp.productcatalog.model.User;
import za.co.entelect.devcamp.productcatalog.repository.UserRepository;
import za.co.entelect.devcamp.productcatalog.requests.CreateUserRequest;
import za.co.entelect.devcamp.productcatalog.requests.LoginRequest;
import za.co.entelect.devcamp.productcatalog.requests.RegisterRequest;
import za.co.entelect.devcamp.productcatalog.responses.CreateUserResponse;
import za.co.entelect.devcamp.productcatalog.responses.ValidationResult;

@Slf4j
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public final ICustomerService customerService;
    public final JwtEncoder jwtEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ICustomerService customerService,
                       JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public UserDto CreateUser(CreateUserRequest request) throws Exception {
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
            log.info("Exception create user service: " + e.getMessage());
            throw new Exception("Failed to create user: "+ e.getMessage());
        }
    }

    @Override
    public UserDto LoadUserByUsername(String username) throws NotFoundException {
        User user = userRepository
                        .findFirstByEmailIgnoreCase(username)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User not found"));
        return toUserDto(user);

    }

    @Override
    public CreateUserResponse RegisterUser(RegisterRequest request) throws Exception {
        try {
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setEmail(request.getUsername());
            createUserRequest.setPassword(request.getPassword());
            createUserRequest.setRole(request.getRole());

            UserDto createdUser = CreateUser(createUserRequest);

            CustomerDto customerDto = new CustomerDto();
            customerDto.setUsername(request.getUsername());
            customerDto.setFirstName(request.getFirstName());
            customerDto.setLastName(request.getLastName());
            customerDto.setIdNumber(request.getIdNumber());
            customerDto.setCustomerTypeId(request.getCustomerTypeId());

            CustomerDto createdCustomer = customerService.CreateCustomer(customerDto);

            CreateUserResponse createUserResponse = new CreateUserResponse();
            createUserResponse.setUser(createdUser);
            createUserResponse.setCustomer(createdCustomer);

            return createUserResponse;
        }
        catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String GetToken(LoginRequest loginRequest) throws BadCredentialsException, Exception {
        try {
            log.info("Validating username and password");
            ValidationResult validationResult = validateUsernameAndPassword(loginRequest);

            if(validationResult.getValid()) {
                Instant now = Instant.now();
                Long expiry = 3600L;
                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(expiry))
                        .subject(loginRequest.getUsername())
                        .claim("role", validationResult.getRole())
                        .build();
                String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

                return token;
            }
            else {
                throw new BadCredentialsException("Invalid username or password");
            }
        }
        catch(Exception e) {
            throw new Exception("Failed to retrieve token");
        }
    }


    private UserDto toUserDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getEmail(),
                user.getRole()
        );
    }

    private ValidationResult validateUsernameAndPassword(LoginRequest request) throws Exception, BadCredentialsException {
        User user = userRepository
                .findFirstByEmailIgnoreCase(request.getUsername())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Incorrect username or password"));

        String enteredPassword = request.getPassword();
        String storedPassword = user.getPassword();

        if (passwordEncoder.matches(enteredPassword, storedPassword)) {
            return new ValidationResult(true, user.getRole());
        }
        else {
            log.info("Incorrect username or password");
            throw new BadCredentialsException("Incorrect username or password");
        }
    }
}