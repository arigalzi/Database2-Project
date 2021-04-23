package it.polimi.db2_project.entities.ids;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;

@Embeddable
public class AnswerKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column (name = "userId")
    private int userId;

    private QuestionKey questionKey;


    public QuestionKey getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(QuestionKey questionKey) {
        this.questionKey = questionKey;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerKey answerKey = (AnswerKey) o;
        return userId == answerKey.userId && Objects.equals(questionKey, answerKey.questionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, questionKey);
    }
}
