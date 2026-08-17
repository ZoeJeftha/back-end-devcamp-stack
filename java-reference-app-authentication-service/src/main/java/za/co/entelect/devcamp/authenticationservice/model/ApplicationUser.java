package za.co.entelect.devcamp.authenticationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name = "applicationUser", schema = "auth")
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_sequence")
    @SequenceGenerator(name = "app_user_sequence", sequenceName = "app_user_sequence", allocationSize = 1)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}
