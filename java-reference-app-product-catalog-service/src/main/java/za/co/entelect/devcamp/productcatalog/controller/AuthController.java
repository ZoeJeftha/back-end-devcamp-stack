package za.co.entelect.devcamp.productcatalog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import za.co.entelect.devcamp.productcatalog.dto.CustomerDto;
import za.co.entelect.devcamp.productcatalog.service.IUserService;
import za.co.entelect.devcamp.productcatalog.requests.LoginRequest;
import za.co.entelect.devcamp.productcatalog.requests.RegisterRequest;
import za.co.entelect.devcamp.productcatalog.responses.ApiResponse;
import za.co.entelect.devcamp.productcatalog.responses.CreateUserResponse;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    public final IUserService userService;


    public AuthController(IUserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CreateUserResponse>> Register(@RequestBody RegisterRequest request)
    {
        log.info("Registering user");
        try {
            CreateUserResponse createUserResponse = userService.RegisterUser(request);

            ApiResponse<CreateUserResponse> response = new ApiResponse<CreateUserResponse>(true, "User registered successfully",createUserResponse);
            return ResponseEntity.ok(response);
        }
        catch(Exception e)
        {
            log.info("Failed to register user" + e.getMessage());
            ApiResponse<CreateUserResponse> response = new ApiResponse<CreateUserResponse>(false, "Failed to register user: "+ e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody LoginRequest loginRequest) {
        log.info("Log in request recieved");
        try
        {
            String token = userService.GetToken(loginRequest);
            return ResponseEntity.ok(token);
        }
        catch(BadCredentialsException e)
        {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            false,
                            "Invalid username or password",
                            null
                    ));
        }
        catch(Exception e)
        {
            ApiResponse<CustomerDto> response = new ApiResponse<CustomerDto>(false, e.getMessage(), null);
            return ResponseEntity.internalServerError()
                    .body(response);
        }
    }
}
