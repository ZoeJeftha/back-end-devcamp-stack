package za.co.entelect.devcamp.authenticationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.authenticationservice.model.ApplicationUser;
import za.co.entelect.devcamp.authenticationservice.repository.ApplicationUserRepository;

import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationUserDetailsService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
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

}
