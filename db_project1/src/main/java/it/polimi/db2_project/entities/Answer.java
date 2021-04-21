package it.polimi.db2_project.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

import it.polimi.db2_project.entities.ids.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "answer", schema = "db2_app")
@NamedQuery(name = "Answer.getUserFill", query = "SELECT distinct a.user FROM Answer a WHERE a.id.questionKey.productId = ?1")
@NamedQuery(name = "Answer.getUserAnswers", query = "SELECT a FROM Answer a WHERE a.user.username = ?1 AND a.id.questionKey.productId = ?2")
@NamedQuery(name = "Answer.getSpecificAnswer", query = "SELECT a FROM Answer a WHERE a.user.userID = ?1 AND a.question.id.questionId = ?2 AND a.question.product.productId = ?3")
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AnswerKey id;

    @NotNull
    private int point;

    @NotNull
    private boolean isSubmitted;

    @NotNull
    private String answer;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @MapsId("questionKey")
    @JoinColumns(
            {  @JoinColumn(name = "questionID", referencedColumnName = "questionID"),
                    @JoinColumn(name = "productID", referencedColumnName = "productID")
            })
    private Question question;

    public void setId(AnswerKey id) {
        this.id = id;
    }

    public AnswerKey getId() {
        return id;
    }

    public void setPoint(int points) {
        this.point = points;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPoint() {
        return point;
    }

    public User getUser() {
        return user;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}