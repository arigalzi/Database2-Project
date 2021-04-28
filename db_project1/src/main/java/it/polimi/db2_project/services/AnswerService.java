package it.polimi.db2_project.services;

import it.polimi.db2_project.entities.Answer;
import it.polimi.db2_project.entities.DirtyWord;
import it.polimi.db2_project.entities.Question;
import it.polimi.db2_project.entities.User;

import java.util.Locale;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class AnswerService {
    @PersistenceContext(
            unitName = "db2_app"
    )
    private EntityManager em;

    public AnswerService() {
    }

    public boolean hasDirtyWord(String[] answers) {
        for(int i = 0; i < answers.length; ++i) {
            String[] words = answers[i].split(" ");
            for(int j = 0; j < words.length; j++) {
                DirtyWord word;
                try {
                    word = em.createNamedQuery("DirtyWord.checkSentence", DirtyWord.class).setParameter(1, words[j]).getSingleResult();
                }
                catch (NoResultException e){
                    word = null;
                }
                if (word != null) {
                    return true;
                }
            }
        }

        return false;
    }

    public String[] correctAnswerFormat(String[] answers) {
        for(int i = 0; i < answers.length; ++i) {
            answers[i] = answers[i].toLowerCase(Locale.ROOT);
        }

        return answers;
    }

    public void addAnswer(String response, boolean isSubmitted, User user, Question question){
        Answer answer = new Answer();
        answer.setAnswer(response);
        answer.setSubmitted(isSubmitted);
        answer.setUser(user);
        answer.setQuestion(question);
        //Points are set by the triggers
        em.persist(answer);
    }
}
