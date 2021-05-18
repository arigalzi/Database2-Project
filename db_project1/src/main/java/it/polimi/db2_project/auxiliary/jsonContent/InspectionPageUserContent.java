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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(product!=null) {
            this.prodDate = formatter.format(product.getDate());
            this.prodName = product.getName();
            this.prodDescription = product.getDescription();
            this.username = null;
            this.canceled = false;
            this.answers = null;
            this.questions = null;
        }
        else{
            this.prodDate = null;
            this.prodName = null;
            this.prodDescription = null;
            this.username = username;
            this.canceled = canceled;
            this.answers = answers;
            this.questions = questions;
        }
    }
}
