package it.polimi.db2_project.entities;

import it.polimi.db2_project.entities.ids.ReviewKey;

import javax.persistence.*;
import java.io.*;

@Entity
@Table(name = "review",schema = "db2_app")
@NamedQuery(name = "Review.getReview",query = "SELECT r FROM Review r WHERE r.reviewedProduct.productId = ?1 ")
public class Review implements Serializable {
    public Review(){ }

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ReviewKey id;

    private String text;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name="productID", referencedColumnName="productID")
    private Product reviewedProduct;


    public String getText() {
        return text;
    }

    public void setReviewedProduct(Product reviewedProduct) {
        this.reviewedProduct = reviewedProduct;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Product getReviewedProduct() {
        return reviewedProduct;
    }

    /**
     * Method that sets all the attributes of a review
     * @param product of the Review
     * @param text of the Review
     */
    public void setReviewAttributes(Product product, String text){
        setReviewedProduct(product);
        setText(text);
    }
}