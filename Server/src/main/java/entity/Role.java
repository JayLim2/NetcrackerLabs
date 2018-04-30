package entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"role\"", schema = "public", catalog = "postgres")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_role")
    @SequenceGenerator(name = "auto_increment_role", sequenceName = "\"auto_increment_role\"", allocationSize = 1)

    @Column(name = "\"role_id\"")
    private int id;
    @Column(name = "\"role\"")
    private String role;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy="roles")
    private List<User> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
