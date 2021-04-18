package it.polimi.db2_project.entities.ids;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;

@Embeddable
public class AnswerKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column (name = "userId",insertable = false,updatable = false)
    private int userId;

    @Column(name = "questionId",insertable = false,updatable = false)
    private int questionId;

    @Column(name = "productId",insertable = false,updatable = false)
    private int productId;

    @Column(name = "date",insertable = false,updatable = false)
    private Date date;


    public void setQuestionKey(QuestionKey questionKey) {
        this.questionKey = questionKey;
    }

    private QuestionKey questionKey;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerKey answerKey = (AnswerKey) o;
        QuestionKey questionKey = new QuestionKey(questionId,date,productId);
        return Objects.equals(userId, answerKey.userId) && Objects.equals(questionKey, answerKey.questionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, questionKey);
    }
}
