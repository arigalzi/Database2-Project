package it.polimi.db2_project.entities;

import it.polimi.db2_project.entities.ids.ReviewKey;

import javax.persistence.*;
import java.io.*;

@Entity
@Table(name = "review",schema = "db2_app")
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;
    public ReviewKey getId() {
        return id;
    }

    public void setId(ReviewKey id) {
        this.id = id;
    }

    @EmbeddedId
    private ReviewKey id;


    @ManyToOne
    @JoinColumn(name = "productId")
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