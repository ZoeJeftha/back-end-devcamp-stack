package za.co.entelect.devcamp.authenticationservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import za.co.entelect.devcamp.authenticationservice.requests.RegisterRequest;
import za.co.entelect.devcamp.authenticationservice.service.ApplicationUserDetailsService;
import org.springframework.http.HttpStatus;
import za.co.entelect.devcamp.authenticationservice.requests.LoginRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import za.co.entelect.devcamp.authenticationservice.responses.RegisterOrLoginResponse;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/")
public class AuthenticationController {

    public final JwtEncoder jwtEncoder;
    public final ApplicationUserDetailsService applicationUserDetailsService;

    @Autowired
    public AuthenticationController(JwtEncoder jwtEncoder, ApplicationUserDetailsService applicationUserDetailsService) {
        this.jwtEncoder = jwtEncoder;
        this.applicationUserDetailsService = applicationUserDetailsService;
    }

    @PostMapping("/token")
    public ResponseEntity<RegisterOrLoginResponse> token(@RequestBody LoginRequest loginRequest) {
        log.info("Log in request recieved for username: " + loginRequest.getUsername());
        RegisterOrLoginResponse response = null;
        try {
            log.info("Validating username and password");
            boolean passwordValidated = applicationUserDetailsService.validateUsernameAndPassword(loginRequest);

            if(passwordValidated) {
                Instant now = Instant.now();
                Long expiry = 3600L;
                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(expiry))
                        .subject(loginRequest.getUsername())
                        .build();
                String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

                response = new RegisterOrLoginResponse(true, "Logged in successfully", token);
                return ResponseEntity.ok(response);
            }
            else
            {
                response = new RegisterOrLoginResponse(false, "Incorrect username or password", "");
                return ResponseEntity.ok(response);
            }
        }
        catch(UsernameNotFoundException e)
        {
            response = new RegisterOrLoginResponse(false, "Username does not exist", "");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(response);
        }
        catch(BadCredentialsException e)
        {
            response = new RegisterOrLoginResponse(false, "Password incorrect", "");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
        catch(Exception e)
        {
            response = new RegisterOrLoginResponse(false, "Login failed. Error occurred: " + e.getMessage(), "");
            return ResponseEntity.internalServerError()
                    .body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterOrLoginResponse> register(@RequestBody RegisterRequest request) {
        try
        {
            log.info("Register client request received");
            applicationUserDetailsService.register(request);
            log.info("Client registered");

            Instant now = Instant.now();
            Long expiry = 3600L;
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(request.getUsername())
                    .build();
            String token =  jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            RegisterOrLoginResponse response = new RegisterOrLoginResponse(true, "User registered successfully", token);
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e)
        {
            RegisterOrLoginResponse response = new RegisterOrLoginResponse(false, "Registration failed: " + e.getMessage(), "");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
