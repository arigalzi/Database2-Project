package it.polimi.db2_project.entities.ids;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class ReviewKey {

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    @Column(name = "reviewId")
    private int reviewId;

    @Column(name = "productId")
    private int productId;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewKey reviewKey = (ReviewKey) o;
        return reviewId == reviewKey.reviewId &&
                Objects.equals(productId, reviewKey.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, productId);
    }
}
