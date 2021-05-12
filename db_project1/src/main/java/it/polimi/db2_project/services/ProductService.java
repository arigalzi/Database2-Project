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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Stateless
public class ProductService {

    @PersistenceContext(unitName = "db2_app")
    private EntityManager em;

    public ProductService(){

    }


    public Product getProductOfTheDay() throws InvalidParameterException{

            Date date = java.sql.Date.valueOf(LocalDate.now());
            List<Product> products = em.createNamedQuery("Product.getProductOfTheDay", Product.class).setParameter(1, date).getResultList();
            if (products == null || products.isEmpty()) {
                throw new InvalidParameterException("No product of the Day");

            } else if (products.size() == 1) {
                return products.get(0);
            } else {
                throw new InvalidParameterException("internal database error");
            }
    }

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

    public void createNewProduct(String name,String description,Date date,List<String> questions,byte[] image,List<String> productReviews){
        Product createdProduct = new Product();
        createdProduct.setDate(date);
        createdProduct.setDescription(description);
        createdProduct.setName(name);
        createdProduct.setImage(image);

        //ADD MANDATORY QUESTIONS FOR THE PRODUCT
        for (int i = 0; i <questions.size() ; i++) {
            Question q = new Question();
            q.setMandatory(true);
            q.setProduct(createdProduct);
            q.setText(questions.get(i));
            q.setQuestionNumber(i+1);
            createdProduct.addQuestion(q);
        }

        //ADD OPTIONAL QUESTIONS FOR THE PRODUCT
        String[] optional = {"Age","Gender","ExpertiseLevel"};
        for (int i = 0; i <3 ; i++) {
            Question q = new Question();
            q.setMandatory(false);
            q.setProduct(createdProduct);
            q.setText(optional[i]);
            q.setQuestionNumber(i+1);
            createdProduct.addQuestion(q);
        }

        //ADD REVIEWS
        for (int i = 0; i <productReviews.size(); i++) {
            Review r = new Review();
            r.setReviewedProduct(createdProduct);
            r.setText(productReviews.get(i));
            createdProduct.addReview(r);
        }
        em.persist(createdProduct);
    }


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


}
