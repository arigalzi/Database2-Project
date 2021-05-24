package it.polimi.db2_project.services;


import it.polimi.db2_project.entities.Evaluation;
import it.polimi.db2_project.entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.InvalidParameterException;
import java.util.ArrayList;
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
            throw new InvalidParameterException("No leaderboard because no one has completed the questionnaire or no product of the day");
        } else {
            return leaderboard;
        }
    }

    public List<String> convertToString(List<Evaluation> evaluations){
        ArrayList<String> texts = new ArrayList<>();
        if(evaluations!=null){
        for (Evaluation e : evaluations) {
            texts.add(String.valueOf(e.getUser().getUsername()).concat(" - ").concat(String.valueOf(e.getTotalPoints())));
        }
        }else{
            texts.add("Leaderboard is empty!");
        }

        return (List<String>)texts;

    }
}
