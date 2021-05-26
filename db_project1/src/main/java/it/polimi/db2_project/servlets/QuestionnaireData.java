package it.polimi.db2_project.servlets;

import com.google.gson.Gson;
import it.polimi.db2_project.auxiliary.UserStatus;
import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.entities.Question;
import it.polimi.db2_project.services.AnswerService;
import it.polimi.db2_project.services.ProductService;
import it.polimi.db2_project.services.QuestionnaireService;
import it.polimi.db2_project.services.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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

    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;

    public QuestionnaireData() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response){

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = (String)request.getSession().getAttribute("user");
        Product productOfDay;

        try {
            productOfDay = productService.getProductOfTheDay(null);
        }catch (InvalidParameterException | EJBException e){
            if(e.getCause().getMessage().equals("No product of the Day")) {
                System.out.println("No product of the day");
            }
            productOfDay = null;
        }

        //There is no product of the day so unacceptable request, thus return
        if(productOfDay == null) {
            response.setStatus(406); // Not acceptable request, product was not created and so no questionnaire to show
            return;
        }

        // There is a product, but questionnaire was already filled by the user, thus bad request status
        if(answerService.alreadyFilled(username,productOfDay)){
            response.setStatus(400);
        }
        // There is a product, but check user status
        else if (userService.checkUserStatus(userService.getUser(username), productOfDay)== UserStatus.BANNED){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        }
        // There is a product, manage the correct request
        else {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(200);
                List<Question> questionList = questionnaireService.getQuestionsOfTheDay();
                List<String> textResponse = questionnaireService.convertToString(questionList);
                String var = (new Gson()).toJson(textResponse);
                System.out.println((new Gson()).toJson(textResponse));
                out.print((new Gson()).toJson(textResponse));

        }

    }
}
