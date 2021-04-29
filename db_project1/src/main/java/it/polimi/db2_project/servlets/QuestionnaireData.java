package it.polimi.db2_project.servlets;

import com.google.gson.Gson;
import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.entities.Question;
import it.polimi.db2_project.services.AnswerService;
import it.polimi.db2_project.services.ProductService;
import it.polimi.db2_project.services.QuestionnaireService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/QuestionnaireData")
public class QuestionnaireData extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/QuestionnaireService")
    private QuestionnaireService questionnaireService;

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    @EJB(name = "it.polimi.db2.entities.services/AnswerService")
    private AnswerService answerService;

    public QuestionnaireData() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    //Questionnaire is filled only if User has not completed it already
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String)request.getSession().getAttribute("user");
        Product productOfDay = productService.getProductOfTheDay();
        if(answerService.alreadyFilled(username,productOfDay)){
            response.setStatus(400);
        }
        else {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            List<Question> questionList = questionnaireService.getQuestionsOfTheDay();
            questionnaireService.orderByQuestionNumber(questionList);
            List<String> textResponse = questionnaireService.convertToString(questionList);
            String var = (new Gson()).toJson(textResponse);
            System.out.println((new Gson()).toJson(textResponse));
            out.print((new Gson()).toJson(textResponse));
        }

    }
}
