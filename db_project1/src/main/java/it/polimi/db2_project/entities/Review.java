package it.polimi.db2_project.entities;

import javax.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @Id
    private int productId;


    @ManyToOne
    @JoinColumn(name = "productId")
    private Product reviewedProduct;

    private String text;

    public Review(){

    }

    public int getReviewId() {
        return reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public Product getReviewedProduct() {
        return reviewedProduct;
    }

    public String getText() {
        return text;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setReviewedProduct(Product reviewedProduct) {
        this.reviewedProduct = reviewedProduct;
    }

    public void setText(String text) {
        this.text = text;
    }
}