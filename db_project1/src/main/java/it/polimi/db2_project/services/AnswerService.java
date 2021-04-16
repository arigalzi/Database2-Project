package it.polimi.db2_project.services;

import it.polimi.db2_project.entities.DirtyWord;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

            for(byte j = 0; j < words.length; ++i) {
                DirtyWord word = (DirtyWord)this.em.createNamedQuery("DirtyWord.checkSentence", DirtyWord.class).setParameter(1, words[i]).getSingleResult();
                if (word != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public String[] correctAnswerFormat(String[] answers) {
        for(int i = 0; i < answers.length; ++i) {
            answers[i] = answers[i].toLowerCase(Locale.ROOT);
        }

        return answers;
    }
}
