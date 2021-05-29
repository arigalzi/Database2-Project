package it.polimi.db2_project.entities;

import it.polimi.db2_project.entities.ids.EvaluationKey;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "evaluation", schema = "db2_app")
@NamedQuery(name = "Evaluation.getLeaderboard", query = "SELECT distinct e  FROM Evaluation e WHERE e.product= ?1 GROUP BY e.user order by e.totalPoints desc")
public class Evaluation implements Serializable {

    public Evaluation() {
    }

    private static final long serialVersionUID = 1L;


    @EmbeddedId
    private EvaluationKey id;

    @NotNull
    private int totalPoints;


    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productID")
    private Product product;


    public int getTotalPoints() {
        return totalPoints;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setId(EvaluationKey id) {
        this.id = id;
    }

    public EvaluationKey getId() {
        return id;
    }
}