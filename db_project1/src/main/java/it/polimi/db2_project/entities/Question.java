package it.polimi.db2_project.entities;

import it.polimi.db2_project.entities.ids.QuestionKey;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "question", schema = "db2_app")
@NamedQuery(name = "Question.getQuestionsOfTheDay",query = "SELECT q FROM Question q WHERE q.product.date = ?1 AND q.isMandatory=true ORDER BY q.questionNumber DESC")
@NamedQuery(name = "Question.getOptionalQuestions",query = "SELECT q FROM Question q WHERE q.product.date = ?1 AND q.isMandatory=false")
public class Question implements Serializable{
    public Question() {
    }

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private QuestionKey id;

    private String text;

    @NotNull
    private boolean isMandatory;

    private int questionNumber;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("productId")
    @JoinColumn(name = "productID")
    private Product product;


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

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getText() {
        return text;
    }


    /**
     * Method that sets all together the attributes of a question
     * @param isMandatory is to know if question is mandatory or not
     * @param product is the product the question is related to
     * @param text is the text of the question
     * @param questionNumber is the number of the question
     */
    public void setQuestionAttributes(boolean isMandatory, Product product, String text, int questionNumber){
        setMandatory(isMandatory);
        setProduct(product);
        setText(text);
        setQuestionNumber(questionNumber+1);
    }


}