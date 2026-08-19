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
    public String token(@RequestBody LoginRequest loginRequest) {
        log.info("Log in request recieved for username: " + loginRequest.getUsername());
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
                return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            }
            else
            {
                return "Username or password is incorrect";
            }
        }
        catch(UsernameNotFoundException e)
        {
            return "Username does not exist";
        }
        catch(BadCredentialsException e)
        {
            return "Password incorrect";
        }
        catch(Exception e)
        {
            return "Error occured: "+ e.getMessage();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try
        {
            log.info("Register client request received");
            applicationUserDetailsService.register(request);
            log.info("Client registered");
            return ResponseEntity.ok("User registered successfully");
        }
        catch(RuntimeException e)
        {
            log.info("Register client request failed");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
