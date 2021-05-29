package it.polimi.db2_project.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user", schema = "db2_app")
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2")
@NamedQuery(name = "User.getUser", query = "SELECT r FROM User r  WHERE r.username = ?1")
@NamedQuery(name = "User.getUsersSubmits", query = "SELECT distinct r FROM User r , Answer a WHERE a.user.userID= r.userID AND a.question.product.productId = ?1")
@NamedQuery(name = "User.getUsersCanceled", query = "SELECT distinct r FROM User r , Log l WHERE r.userID = l.userId AND l.timestamp  > ?1 AND l.timestamp  < ?2 AND l.isFormCancelled = true")

public class User implements Serializable {
    public User() { }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private boolean isBanned;

    @NotNull
    private boolean isAdmin;

    @OneToMany(mappedBy = "user", cascade= CascadeType.REMOVE)
    private List<Log> logs;

    @ManyToMany
    @JoinTable(name="evaluation",
            joinColumns={@JoinColumn(name="userId")},
            inverseJoinColumns={@JoinColumn(name="productId")})
    private List<Product> products;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        this.isBanned = banned;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
