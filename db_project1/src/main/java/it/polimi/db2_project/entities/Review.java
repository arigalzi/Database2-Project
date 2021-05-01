package it.polimi.db2_project.entities;

import it.polimi.db2_project.entities.ids.ReviewKey;

import javax.persistence.*;
import java.io.*;

@Entity

@Table(name = "review",schema = "db2_app")
@NamedQuery(name = "Review.getReview",query = "SELECT r FROM Review r WHERE r.reviewedProduct.productId = ?1 ")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ReviewKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name="productID", referencedColumnName="productID")
    private Product reviewedProduct;

    private String text;

    public Review(){

    }

    public Product getReviewedProduct() {
        return reviewedProduct;
    }

    public String getText() {
        return text;
    }

    public void setReviewedProduct(Product reviewedProduct) {
        this.reviewedProduct = reviewedProduct;
    }

    public void setText(String text) {
        this.text = text;
    }
}