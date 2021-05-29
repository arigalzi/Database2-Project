package it.polimi.db2_project.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "product", schema = "db2_app")
@NamedQuery(name = "Product.getProductDummy", query = "SELECT p FROM Product p WHERE p.name = ?1")
@NamedQuery(name = "Product.getProductOfTheDay", query = "SELECT p FROM Product p WHERE p.date = ?1")
@NamedQuery(name = "Product.getProduct", query = "SELECT p FROM Product p  WHERE p.productId = ?1")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    //auto-incremented id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @NotNull
    private String name;

    private String description;

    @Basic(fetch=FetchType.LAZY)
    @Lob
    private byte[] image;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    public Product(){
         questions = new ArrayList<>();
         reviews = new ArrayList<>();
    }

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private final List<Question> questions;

    @OneToMany(mappedBy = "reviewedProduct", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private final List<Review> reviews;

    @ManyToMany(mappedBy = "products")
    private List<User> users;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question q){
        questions.add(q);
    }

    public void removeQuestion(Question q){
        questions.remove(q);
    }

    public void addReview(Review r){
        reviews.add(r);
    }

    public void removeReview(Review r){
        reviews.remove(r);
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    /**
     * Method that sets all together the attributes of a given product
     * @param date is the date that has to be set for the product
     * @param description is the description that has to be added for the product
     * @param name is the name of the product
     * @param image is the image to display with the product
     */
    public void setProductAttributes(Date date, String description,String name, byte[] image){
            setDate(date);
            setDescription(description);
            setName(name);
            setImage(image);
    }

}