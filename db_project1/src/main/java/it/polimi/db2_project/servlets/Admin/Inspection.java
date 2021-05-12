package it.polimi.db2_project.servlets.Admin;

import com.google.gson.Gson;
import it.polimi.db2_project.auxiliary.jsonContent.InspectionPageContent;
import it.polimi.db2_project.entities.Answer;
import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.services.AnswerService;
import it.polimi.db2_project.services.ProductService;
import it.polimi.db2_project.services.UserService;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/Inspection")
public class Inspection extends HttpServlet {
    @EJB(name = "it.polimi.db2_project.entities.services/ProductService")
    private ProductService productService;

    @EJB(name = "it.polimi.db2_project.entities.services/AnswerService")
    private AnswerService answerService;

    @EJB(name = "it.polimi.db2_project.entities.services/UserService")
    private UserService userService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    boolean checkDate (Date date) {
        return java.sql.Date.valueOf(LocalDate.now()).after(date)  || java.sql.Date.valueOf(LocalDate.now()).equals(date);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sDate = StringEscapeUtils.escapeJava(request.getParameter("date"));

        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        if(checkDate(date)) {
            try {

                Product product;
                List<String> usersWhoSubmitted, usersWhoCanceled;
                Map<String, List<String>> answersForEachUser = new HashMap<>();
                List <Answer> answersFromUser;

                usersWhoSubmitted = new LinkedList<>();
                usersWhoCanceled = new LinkedList<>();
                List<String> questions = null;
                String encoded = null;

                product = productService.checkDateAvailability(date);

                if(product!= null){
                    questions = product.getQuestionsText();
                    usersWhoCanceled = userService.getUsersWhoCanceled(product);

                    usersWhoSubmitted = userService.getUsersWhoSubmits(product);
                    if(!(usersWhoSubmitted== null || usersWhoSubmitted.isEmpty() )){
                        for (String s : usersWhoSubmitted) {
                            answersFromUser = answerService.getUserAnswers(product, s);
                            answersForEachUser.put(s, answerService.getAnswerText(answersFromUser));
                        }
                    }
                    if (product.getImage()!= null) encoded = Base64.getEncoder().encodeToString(product.getImage());
                }

                InspectionPageContent content;
                content = new InspectionPageContent(usersWhoSubmitted, usersWhoCanceled, answersForEachUser, questions,
                        product.getName(), product.getDescription(), encoded, product.getDate());

                String jsonResponse = new Gson().toJson(content);

                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);

                out.write(jsonResponse);
            } catch (Exception e) {
                sendError(request, response, "Inspection Error", e.getCause().getMessage());
            }
        }
        else{
            response.setStatus(400);
        }
    }

    /**
     * Method to handle errors, redirects to an error page
     * @param request request
     * @param response response
     * @param errorType type of error
     * @param errorInfo information about the error
     * @throws IOException if there are problems redirecting
     */
    protected void sendError(HttpServletRequest request, HttpServletResponse response, String errorType, String errorInfo) throws IOException {
        request.getSession().setAttribute ("errorType", errorType);
        request.getSession().setAttribute ("errorInfo", errorInfo);
        try {
            getServletConfig().getServletContext().getRequestDispatcher("/error.html").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
