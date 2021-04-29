package it.polimi.db2_project.services;

import it.polimi.db2_project.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Date;

@Stateless
public class ProductService {

    @PersistenceContext(unitName = "db2_app")
    private EntityManager em;

    public ProductService(){

    }


    public Product getProductOfTheDay(){
        Date date = java.sql.Date.valueOf(LocalDate.now());
        return em.createNamedQuery("Product.getProductOfTheDay",Product.class).setParameter(1,date).getSingleResult();
    }
}
