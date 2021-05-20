package it.polimi.db2_project.services;


import it.polimi.db2_project.entities.Review;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ReviewService {
    @PersistenceContext(unitName = "db2_app")
    private EntityManager em;

    public ReviewService(){
    }

    /**
     * Method to get reviews of a specific product
     * @param productID specific id of a product
     * @return list of reviews
     * @throws InvalidParameterException if there are no review
     */
    public ArrayList<String> getReviews(int productID) throws InvalidParameterException {
        ArrayList<String> result = new ArrayList<>();
        List<Review> reviews;
        int i=0;

        reviews = em.createNamedQuery("Review.getReview", Review.class).setParameter(1, productID).getResultList();

        if(reviews.size() == 0 || reviews.isEmpty()){
            throw new InvalidParameterException("No review for this Product");
        }
        else {

            while (i < reviews.size()) {
                result.add(reviews.get(i).getText());
                i++;
            }
            return result;

        }
    }

}
