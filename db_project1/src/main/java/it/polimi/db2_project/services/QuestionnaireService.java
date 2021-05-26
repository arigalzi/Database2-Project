package it.polimi.db2_project.services;

import it.polimi.db2_project.entities.Question;
import java.time.LocalDate;
import java.util.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class QuestionnaireService {
    @PersistenceContext(
            unitName = "db2_app"
    )
    private EntityManager em;

    public QuestionnaireService() {
    }

    /**
     * Method that gets the list of question of the current day to display it in the homepage of users
     * @return a list of questions
     */
    public List<Question> getQuestionsOfTheDay() {
        Date date = java.sql.Date.valueOf(LocalDate.now());
        System.out.println(date);
        return this.em.createNamedQuery("Question.getQuestionsOfTheDay", Question.class).setParameter(1, date).getResultList();
    }

    /**
     * Method that gets the optional questions of the current date to diplay them
     * @return a list of questions
     */
    public List<Question> getOptionalQuestions() {
        Date date = java.sql.Date.valueOf(LocalDate.now());
        return this.em.createNamedQuery("Question.getOptionalQuestions", Question.class).setParameter(1,date).getResultList();
    }

    /**
     * method to convert a list of questions into a list of strings for sending purposes on json
     * @param questions is the list of questions that have to be converted
     * @return list of strings that contain the questions context
     */
    public List<String> convertToString(List<Question> questions){
        ArrayList<String> texts = new ArrayList<>();
        for (Question q: questions) {
            texts.add(q.getText());
        }
        return (List<String>)texts;
    }

}
