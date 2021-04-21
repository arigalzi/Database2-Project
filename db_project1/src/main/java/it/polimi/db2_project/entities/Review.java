package it.polimi.db2_project.entities;

import it.polimi.db2_project.entities.ids.ReviewKey;

import javax.persistence.*;
import java.io.*;

@Entity

@Table(name = "review",schema = "db2_app")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ReviewKey id;

    @ManyToOne
    @MapsId("productId")
    @PrimaryKeyJoinColumn(name="productID", referencedColumnName="productID")
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