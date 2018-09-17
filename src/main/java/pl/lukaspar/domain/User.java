package pl.lukaspar.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    // Gettery & Settery

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", active=" + active +
                ", dateOfRegistration=" + dateOfRegistration +
                ", score=" + score +
                ", roles=" + roles +
                '}';
    }

}
