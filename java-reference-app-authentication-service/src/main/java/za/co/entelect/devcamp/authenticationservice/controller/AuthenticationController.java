package za.co.entelect.devcamp.authenticationservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import za.co.entelect.devcamp.authenticationservice.service.ApplicationUserDetailsService;
import za.co.entelect.devcamp.authenticationservice.requests.LoginRequest;
import za.co.entelect.devcamp.authenticationservice.responses.RegisterOrLoginResponse;
import za.co.entelect.devcamp.authenticationservice.requests.RegisterRequest;
import za.co.entelect.devcamp.authenticationservice.responses.ValidationResult;

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
    public String token(Authentication authentication) {
        Instant now = Instant.now();
        Long expiry = 3600L;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterOrLoginResponse> register(@RequestBody RegisterRequest request)
    {
        try
        {
            log.info("Register client request received");
            applicationUserDetailsService.register(request);
            log.info("Client registered");

            RegisterOrLoginResponse response = new RegisterOrLoginResponse(true, "User registered successfully", null);
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e)
        {
            RegisterOrLoginResponse response = new RegisterOrLoginResponse(false, "Registration failed: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
