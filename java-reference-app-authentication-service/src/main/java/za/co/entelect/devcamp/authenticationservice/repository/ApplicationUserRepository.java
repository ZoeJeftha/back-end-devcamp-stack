package za.co.entelect.devcamp.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.devcamp.authenticationservice.model.ApplicationUser;
import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findFirstByEmailIgnoreCase(String email);
}
