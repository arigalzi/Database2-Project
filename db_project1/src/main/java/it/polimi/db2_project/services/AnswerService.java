package it.polimi.db2_project.services;

import it.polimi.db2_project.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class AnswerService {

    @PersistenceContext(unitName = "db2_app")
    private EntityManager em;

    public AnswerService() {
    }

    /**
     * Method used by AnswerData servlet to check if an answer given contains bad words
     * @param answers
     * @return
     */
    public boolean hasDirtyWord(String[] answers) {

        for (String answer : answers) {
            String[] words = answer.split(" ");
            for (String s : words) {
                DirtyWord word;
                try {
                    word = em.createNamedQuery("DirtyWord.checkSentence", DirtyWord.class).setParameter(1, s).getSingleResult();
                } catch (NoResultException e) {
                    word = null;
                }
                if (word != null) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Converting each answer to lowercase before persisting the data into DB
     * @param answers
     * @return
     */
    public String[] correctAnswerFormat(String[] answers) {

        for(int i = 0; i < answers.length; ++i) {
            answers[i] = answers[i].toLowerCase(Locale.ROOT);
        }

        return answers;
    }

    /** Used in AnswerData servlet
     * Create an object of class Answer and persist in the DB
     * @param response
     * @param user
     * @param question
     */
    public void addAnswer(String response, User user, Question question){

        if(response.equals(""))
            return;
        Answer answer = new Answer();
        answer.setAttributes(user,response,question);
        //Points are set by the triggers
        em.persist(answer);

    }

    /**
     * Needed by QuestionnaireData servlet to see if the given user has already filled the Questionnaire of the day
     * @param username
     * @param product
     * @return
     */
    public boolean alreadyFilled(String username, Product product){

        List<Answer> answers;
        try {
            answers =  em.createNamedQuery("Answer.getUserAnswers", Answer.class).setParameter(1, username).setParameter(2, product.getProductId()).getResultList();
        }
        catch(NoResultException e){
            answers = null;
        }
        return answers.size() != 0;
    }

    /**
     * Method to retrieve all the answers of a user for a specific product
     * @param product
     * @param s
     * @return
     */
    public List<Answer> getUserAnswers(Product product, String s) {

        List<Answer> answers;
        try {
            answers =  em.createNamedQuery("Answer.getUserAnswers", Answer.class).setParameter(1, s).setParameter(2, product.getProductId()).getResultList();
        }
        catch(NoResultException e){
            answers = null;
        }
        return answers;
    }

    /**
     * Utility method to convert a list of type Answer into type String
     * @param answersFromUser
     * @return
     */
    public List<String> getAnswerText(List<Answer> answersFromUser) {
        List<String> results = new ArrayList<String>();
        answersFromUser.stream().forEach(q -> results.add(q.getAnswer()));
        return results;
    }

    /**
     * Check in AnswerData servlet if all mandatory question-fields are filled properly
     * @param mandatory_answers
     * @return
     */
    public boolean checkMandatoryOK(String[] mandatory_answers){

        for (String mandatory_answer : mandatory_answers) {
            if (mandatory_answer.equals(""))
                return false;
        }
        return true;
    }
}