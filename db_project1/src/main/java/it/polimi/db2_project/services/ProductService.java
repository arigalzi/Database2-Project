package it.polimi.db2_project.services;

import it.polimi.db2_project.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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


    public Product getProductOfTheDay(){
        Date date = java.sql.Date.valueOf(LocalDate.now());
        List<Product> products = em.createNamedQuery("Product.getProductOfTheDay", Product.class).setParameter(1, date).getResultList();
        if (products == null || products.isEmpty()) {
            throw new InvalidParameterException("No product of the Day");

        }
        else if(products.size()==1) {
            return products.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    public HashMap<Integer,Integer> convertToHash(List<Evaluation> evaluations){
        HashMap<Integer,Integer> hm = new HashMap<>();
        for (Evaluation e : evaluations) {
            hm.put(e.getUser().getUserID(), e.getTotalPoints());
        }
        return hm;

    }

    public List<String> convertToString(List<Evaluation> evaluations){
        ArrayList<String> texts = new ArrayList<>();
        for (Evaluation e : evaluations) {
            texts.add(String.valueOf(e.getUser().getUserID()));
            texts.add(String.valueOf(e.getTotalPoints()));
        }
        return (List<String>)texts;

    }

}
