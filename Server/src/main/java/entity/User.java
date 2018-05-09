package entity;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "\"users\"", schema = "public", catalog = "postgres")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_user")
    @SequenceGenerator(name = "auto_increment_user", sequenceName = "\"auto_increment_user\"", allocationSize = 1)
    @Column(name = "\"user_id\"")
    private int id;
    @Column(name = "\"email\"")
    @Length(max = 50, message = "* Ваш Email не должен быть длиннее 50 символов")
    @Email(message = "* Введите корректный Email")
    @NotEmpty(message = "* Email не может быть пустым")
    private String email;
    @Column(name = "\"password\"")
    @Length(min = 5, message = "* Ваш пароль должен быть длиной не менее 5 символов и не более 20")
    @NotEmpty(message = "* Пароль не может быть пустым")
    @Transient
    private String password;
    @Column(name = "\"name\"")
    @Length(max = 50, message = "*Имя не должно быть длиннее 50 символов")
    @NotEmpty(message = "* Введите имя")
    private String name;
    @Column(name = "\"last_name\"")
    @Length(max = 50, message = "* Фамилия не должна быть длиннее 50 символов")
    @NotEmpty(message = "* Введите фамилию")
    private String lastName;
    @Column(name = "\"active\"")
    private int active;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name="\"user_role\"",
            joinColumns = @JoinColumn(name= "\"user_id\""),
            inverseJoinColumns = @JoinColumn(name="\"role_id\""))
    private Set<Role> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
