package za.co.entelect.devcamp.productcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name= "user", schema="pc")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name="user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "pc.user_sequence", allocationSize = 1)
    private Long userId;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="role", nullable = false)
    private String role;
}