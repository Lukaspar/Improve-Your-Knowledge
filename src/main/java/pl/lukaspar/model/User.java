package pl.lukaspar.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotEmpty(message = "*Nazwa użytkownika nie może być pusta!")
    @Length(min = 5, message = "*Nazwa użytkownika musi posiadać minimum 5 znaków.")
    private String username;

    @NotEmpty(message = "*Hasło nie może być puste!")
    @Length(min = 5, message = "*Hasło musi posiadać minimum 5 znaków.")
    private String password;

    @Transient // nie zapisuje do bazy
    private String confirmPassword;

    @Column(name = "active")
    private Integer active;

    @Column(name = "date_of_registration")
    private LocalDate dateOfRegistration;

    @Column(name = "score")
    private Integer score;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public void addScore(int scoreToAdd) {
        this.score += scoreToAdd;
    }

}
