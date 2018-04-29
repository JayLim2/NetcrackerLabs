package entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"userList\"", schema = "public", catalog = "postgres")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_user")
    @SequenceGenerator(name = "auto_increment_user", sequenceName = "\"auto_increment_publisher\"", allocationSize = 1)
    @Column (name = "\"userID\"")
    private int userID;

    @Column (name = "\"userLogin\"",nullable=false, unique = true, length = 30)
    private String userLogin;

    @Column (name = "\"userPass\"",nullable=false, unique = true, length = 30)
    private String userPass;

    @Column (name = "\"cart\"")
    private int cart;

    @Column (name = "\role\"")
    private int role;

    public User() {}

    public User(String userLogin, String userPass, int cart, int role) {
        this.userLogin = userLogin;
        this.userPass = userPass;
        this.cart = cart;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public int getCart() {
        return cart;
    }

    public void setCart(int cart) {
        this.cart = cart;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userLogin='" + userLogin + '\'' +
                ", userPass='" + userPass + '\'' +
                ", cart=" + cart +
                ", role=" + role +
                '}';
    }
}
