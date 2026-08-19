package za.co.entelect.devcamp.authenticationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.authenticationservice.model.ApplicationUser;
import za.co.entelect.devcamp.authenticationservice.repository.ApplicationUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.co.entelect.devcamp.authenticationservice.client.CustomerApiClient;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import za.co.entelect.devcamp.authenticationservice.requests.RegisterRequest;
import  za.co.entelect.devcamp.authenticationservice.requests.CreateCustomerRequest;

@Slf4j
@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerApiClient customerApiClient;

    @Autowired
    public ApplicationUserDetailsService(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder, CustomerApiClient customerApiClient) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerApiClient = customerApiClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ApplicationUser> applicationUserByEmail = applicationUserRepository.findFirstByEmailIgnoreCase(username);

        if (applicationUserByEmail.isPresent()) {
            ApplicationUser applicationUser = applicationUserByEmail.get();
            return User.builder()
                    .username(applicationUser.getEmail())
                    .password(applicationUser.getPassword())
                    .roles(applicationUser.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public ApplicationUser register(RegisterRequest request)
    {
        Optional<ApplicationUser> applicationUserByEmail = applicationUserRepository.findFirstByEmailIgnoreCase(request.getUsername());

        if(applicationUserByEmail.isPresent()) {
            log.info("Register client: username already exists");
            throw new RuntimeException("Username already exists");
        }
        log.info("Registering client client");
        ApplicationUser user = new ApplicationUser();

        user.setEmail(request.getUsername());
        user.setRole(request.getRole());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        customerApiClient.createCustomer(
                new CreateCustomerRequest(
                        request.getUsername(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getIdNumber()));

        return applicationUserRepository.save(user);
    }


}
