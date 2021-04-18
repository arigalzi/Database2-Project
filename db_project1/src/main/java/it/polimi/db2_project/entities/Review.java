package it.polimi.db2_project.entities;

import it.polimi.db2_project.entities.ids.ReviewKey;

import javax.persistence.*;
import java.io.*;

@Entity
@IdClass(ReviewKey.class)
@Table(name = "review",schema = "db2_app")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;


    @EmbeddedId
    private ReviewKey id;


    @MapsId("reviewId")
    @Column(name="reviewID")
    private int reviewId;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productID",referencedColumnName = "productID")
    private Product reviewedProduct;

    private String text;

    public Review(){

    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }


    public ReviewKey getId() {
        return id;
    }

    public void setId(ReviewKey id) {
        this.id = id;
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