package it.polimi.db2_project.services;

import it.polimi.db2_project.entities.Question;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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

    public List<Question> getQuestionsOfTheDay() {
        Date date = java.sql.Date.valueOf(LocalDate.now());
        return this.em.createNamedQuery("Question.getQuestionsOfTheDay", Question.class).setParameter(1, date).getResultList();
    }

    public void orderByQuestionNumber(List<Question> questions) {
        questions.sort((o1, o2) -> {
            return o1.compareTo(o2);
        });
    }
}
