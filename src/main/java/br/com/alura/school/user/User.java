package br.com.alura.school.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.alura.school.enrollment.Enrollment;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

@Entity
class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(max=20)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<Enrollment> enrollment = new ArrayList<>();

    @Deprecated
    protected User() {}

    User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    String getUsername() {
        return username;
    }

    String getEmail() {
        return email;
    }

}
