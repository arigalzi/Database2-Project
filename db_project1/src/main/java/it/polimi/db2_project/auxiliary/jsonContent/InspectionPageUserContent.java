package it.polimi.db2_project.auxiliary.jsonContent;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InspectionPageUserContent implements Serializable{

    private final String username;
    private final List<String> answers;
    private final List<String> questions;
    private boolean canceled;

    public InspectionPageUserContent(String username, Boolean canceled, List<String> answers, List<String> questions) {
        this.username = username;
        this.canceled = canceled;
        this.answers = answers;
        this.questions = questions;
    }
}
