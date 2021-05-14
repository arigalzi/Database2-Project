package it.polimi.db2_project.auxiliary.jsonContent;

import it.polimi.db2_project.entities.Product;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;


public class InspectionPageUserContent implements Serializable{

    private final String username;
    private final List<String> answers;
    private final List<String> questions;
    private final String prodDate;
    private final String prodName;
    private final String prodDescription;
    private boolean canceled;

    public InspectionPageUserContent(String username, Boolean canceled, List<String> answers, List<String> questions, Product product) {
        this.username = username;
        this.canceled = canceled;
        this.answers = answers;
        this.questions = questions;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.prodDate = formatter.format(product.getDate());
        this.prodName = product.getName();
        this.prodDescription = product.getDescription();
    }
}
