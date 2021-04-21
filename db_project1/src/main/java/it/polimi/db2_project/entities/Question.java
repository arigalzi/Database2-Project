package it.polimi.db2_project.entities;

import it.polimi.db2_project.entities.ids.QuestionKey;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "question", schema = "db2_app")
@NamedQuery(name = "Question.getQuestionsOfTheDay",query = "SELECT q FROM Question q WHERE q.date = ?1")
public class Question implements Serializable,Comparable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private QuestionKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productID")
    private Product product;

    private String text;

    @Temporal(TemporalType.DATE)
    private Date date;

    @NotNull
    private boolean isMandatory;

    private int questionNumber;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }


    public void setText(String text) {
        this.text = text;
    }



    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public QuestionKey getQuestionKey() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }


    @Override
    public int compareTo(Object o) {
        Question q = (Question) o;
       if(this.getQuestionNumber() < q.getQuestionNumber())
           return -1;
       else if(this.getQuestionNumber() == q.getQuestionNumber())
           return 0;
       else
           return 1;
    }
}