
package it.polimi.db2_project.servlets;

import it.polimi.db2_project.entities.Question;
import it.polimi.db2_project.entities.User;
import it.polimi.db2_project.services.AnswerService;
import it.polimi.db2_project.services.ProductService;
import it.polimi.db2_project.services.QuestionnaireService;
import it.polimi.db2_project.services.UserService;
import java.io.IOException;
import java.util.*;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AnswerData")
@MultipartConfig
public class AnswerData extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/AnswerService")
    private AnswerService answerService;
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;

    @EJB(name = "it.polimi.db2.entities.services/QuestionnaireService")
    private QuestionnaireService questionnaireService;


    public AnswerData() {
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response){

        String username = (String)request.getSession().getAttribute("user");
        User user = userService.getUser(username);
        List<Question> questionList = questionnaireService.getQuestionsOfTheDay();
        List<Question> optionalQuestions = questionnaireService.getOptionalQuestions();

        //Manage the responses
        String[] mandatory_answers = new String[questionList.size()];
        String age=null;
        String gender = null;
        String expertiseLevel = null;

        //Reading the HTTP request for the Form
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            switch (paramName){
                case "mandatory": mandatory_answers = request.getParameterValues(paramName); break;
                case "age": age = request.getParameter(paramName); break;
                case "selectSex": gender = request.getParameter(paramName); break;
                case "expertiseLevel": expertiseLevel = request.getParameter(paramName); break;
            }
            System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }

        //Manage cancelled FORM
        if(request.getHeader("submitted").equals("false")){
            //No respondes are stored but we set the current Log with questionnaireCancelled a true
            userService.cancelForm(user);
            return;

        }

        //Check if USER must reDo the Form because Mandatory questions are not filled
        if(!answerService.checkMandatoryOK(answerService.correctAnswerFormat(mandatory_answers))){
            response.setStatus(406);
            return;

        }

        //Check if USER needs to be banned
        if (answerService.hasDirtyWord(answerService.correctAnswerFormat(mandatory_answers))) {
            this.userService.banUser(username);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }


        //Manage submitted FORM
        else {

            //Manage the mandatory responses
            for (int i = 0; i < mandatory_answers.length; i++)
                answerService.addAnswer(mandatory_answers[i], user, questionList.get(i));

            //Manage the optional responses
            if(age != null)
             answerService.addAnswer(age, user, optionalQuestions.get(0));
            if(gender != null)
             answerService.addAnswer(gender, user, optionalQuestions.get(1));
            if(expertiseLevel != null)
             answerService.addAnswer(expertiseLevel, user, optionalQuestions.get(2));

        }

        System.out.println("Request managed by AnswerData");


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

}


