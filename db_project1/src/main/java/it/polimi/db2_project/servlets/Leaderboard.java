package it.polimi.db2_project.servlets;


import com.google.gson.Gson;
//import it.polimi.db2_project.auxiliary.json.LeaderboardContent;
import it.polimi.db2_project.entities.Evaluation;
import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.entities.Question;
import it.polimi.db2_project.services.AnswerService;
import it.polimi.db2_project.services.EvaluationService;
import it.polimi.db2_project.services.ProductService;
//import it.polimi.db2_project.services.ProductService;
//import it.polimi.db2_project.services.RewardService;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;


@WebServlet("/Leaderboard")
public class Leaderboard extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/EvaluationService")
    private EvaluationService evaluationService;
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    public Leaderboard() {

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Product prodOfTheDay = null;
        try {
            prodOfTheDay = productService.getProductOfTheDay(null);
            List<Evaluation> leaderboard = evaluationService.getLeaderboard(prodOfTheDay);

            List<String> textResponse = evaluationService.convertToString(leaderboard);
            String jsonLeaderboard = new Gson().toJson(textResponse);
            out.write(jsonLeaderboard);
        }catch (InvalidParameterException | EJBException e) {
            System.out.println(e.getMessage());
            if (e.getCause().getMessage().equals("No leaderboard because no one has completed the questionnaire or no product of the day")) {
                List<String> textResponse = evaluationService.convertToString(null);
                String jsonLeaderboard = new Gson().toJson(textResponse);
                out.write(jsonLeaderboard);
            } else {
                sendError(request, response, "Database Error", e.getMessage());
            }
        }
    }


}
