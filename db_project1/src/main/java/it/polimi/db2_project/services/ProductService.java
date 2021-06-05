package it.polimi.db2_project.services;

import it.polimi.db2_project.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Stateless
public class ProductService {

    @PersistenceContext(unitName = "db2_app")
    private EntityManager em;

    public ProductService(){

    }

    /**
     * Method used to return the product of the day in two possible cases: product of the current
     * date to display it in the homepage of the users or product of a given date to display it in
     * the inspection page of the admin
     * @param date date of the product that has to be found
     * @return product of the given date
     * @throws InvalidParameterException if there is no product for the give date
     */
    public Product getProductOfTheDay(Date date) throws InvalidParameterException{
        if(date==null)
        date = java.sql.Date.valueOf(LocalDate.now());

        List<Product> products = em.createNamedQuery("Product.getProductOfTheDay", Product.class).setParameter(1, date).getResultList();
        if (products == null || products.isEmpty()) {
            throw new InvalidParameterException("No product of the Day");

        } else if (products.size() == 1) {
            return products.get(0);
        } else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to check if a given date has already a product of the day
     * @param date date at which the availability has to be checked
     * @return product of that date or null if there is no product
     */
    public Product checkDateAvailability(Date date){
        Product product;
        try {
            product = em.createNamedQuery("Product.getProductOfTheDay", Product.class).setParameter(1, date).getSingleResult();
        }
        catch (NoResultException e){
            product = null;
        }
        return product;
    }

    /**
     * Method that creates a new product and adds all the attributes, included all the attributes of the related
     * questions and reviews (it is used when an admin creates a product)
     * @param name is the name of the product
     * @param description is the description related to the product
     * @param date is the date associated to the product
     * @param questions are the list of questions related to the product
     * @param image is the image that shows the product
     * @param productReviews is the list of reviews related to the product
     */
    public void createNewProduct(String name,String description,Date date,List<String> questions,byte[] image,List<String> productReviews){
        Product createdProduct = new Product();
        createdProduct.setProductAttributes(date,description,name,image);

        //ADD MANDATORY QUESTIONS FOR THE PRODUCT
        for (int i = 0; i <questions.size() ; i++) {
            Question q = new Question();
            q.setQuestionAttributes(true,createdProduct,questions.get(i),i+1);
            createdProduct.addQuestion(q);
        }

        //ADD OPTIONAL QUESTIONS FOR THE PRODUCT
        String[] optional = {"Age","Gender","ExpertiseLevel"};
        for (int i = 0; i <3 ; i++) {
            Question q = new Question();
            q.setQuestionAttributes(false,createdProduct,optional[i],i+1);
            createdProduct.addQuestion(q);
        }

        //ADD REVIEWS
        for (int i = 0; i <productReviews.size(); i++) {
            Review r = new Review();
            r.setReviewAttributes(createdProduct, productReviews.get(i));
            createdProduct.addReview(r);
        }

        em.persist(createdProduct);
    }

    /**
     * Method that reads the image correctly in order to display it
     * @param imageInputStream is the image
     * @return array of bytes
     * @throws IOException
     */
    public static byte[] readImage(InputStream imageInputStream) throws IOException {
        int FILE_SIZE = 2000 * 2000 * 3; // Images of size max 12MB (RGB has 3 bytes per pixel)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[FILE_SIZE];// image can be maximum of 4MB
        int bytesRead = -1;
        try {
            while ((bytesRead = imageInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Method that deletes a given product
     * @param product is the product that has to be deleted
     */
    public void deleteProduct(Product product) {
        if (!em.contains(product)) {
            product = em.merge(product);
        }
        em.remove(product);
    }
}
