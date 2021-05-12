package it.polimi.db2_project.services;


import it.polimi.db2_project.entities.Evaluation;
import it.polimi.db2_project.entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class EvaluationService {
    @PersistenceContext(
            unitName = "db2_app"
    )
    private EntityManager em;

    public EvaluationService() {
    }

    /**
     * Method to retrieve all the questionnaires related to a specific product
     *
     * @param product the product you want to retrieve
     * @return the list of entries of the leaderboard
     * @throws InvalidParameterException if the product does not exist or there is more than 1 product
     */
    public List<Evaluation> getLeaderboard(Product product) throws InvalidParameterException {
        List<Evaluation> leaderboard = em.createNamedQuery("Evaluation.getLeaderboard", Evaluation.class).setParameter(1, product).getResultList();
        if (leaderboard == null) {
            throw new InvalidParameterException("No questionnaires available for this product");
        } else {
            return leaderboard;
        }
    }

    public List<String> convertToString(List<Evaluation> evaluations){
        ArrayList<String> texts = new ArrayList<>();
        for (Evaluation e : evaluations) {
            texts.add(String.valueOf(e.getUser().getUsername()).concat(" - ").concat(String.valueOf(e.getTotalPoints())));
        }
        return (List<String>)texts;

    }

    public HashMap<Integer,Integer> convertToHash(List<Evaluation> evaluations){
        HashMap<Integer,Integer> hm = new HashMap<>();
        for (Evaluation e : evaluations) {
            hm.put(e.getUser().getUserID(), e.getTotalPoints());
        }
        return hm;

    }
}
