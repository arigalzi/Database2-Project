package it.polimi.db2_project.servlets;

import com.google.gson.Gson;
import it.polimi.db2_project.auxiliary.UserStatus;
import it.polimi.db2_project.auxiliary.jsonContent.HomepageContent;
import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.entities.User;
import it.polimi.db2_project.services.ProductService;
import it.polimi.db2_project.services.ReviewService;
import it.polimi.db2_project.services.UserService;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToHomepage")
public class GoToHomePage extends HttpServlet {
    @EJB(name = "it.polimi.db2_project.entities.services/UserService")
    private UserService userService;
    @EJB(name = "it.polimi.db2_project.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2_project.entities.services/ReviewService")
    private ReviewService reviewService;

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = (String) request.getSession().getAttribute("user");
        User user = userService.getUser(username);

        UserStatus userStatus = null;
        Product prodOfTheDay = null;

        try {
            prodOfTheDay = productService.getProductOfTheDay();
        }
        catch (InvalidParameterException | EJBException e){
            userStatus = userService.checkUserStatus(user, prodOfTheDay);
            HomepageContent homepageContent = new HomepageContent(username, user.isAdmin(),
                    null, null, null,
                    null, userStatus);
            String jsonHomepage = new Gson().toJson(homepageContent);
            out.write(jsonHomepage);
            return;

        }


        ArrayList<String> reviews = null;
        userStatus = userService.checkUserStatus(user, prodOfTheDay);

        try {
            if(prodOfTheDay!= null)
                reviews = reviewService.getReviews(prodOfTheDay.getProductId());
        }
        catch (InvalidParameterException | EJBException e){
            System.out.println(e.getMessage());
        }
        String encoded = null;

        if (prodOfTheDay.getImage()!= null) encoded = Base64.getEncoder().encodeToString(prodOfTheDay.getImage());

        HomepageContent homepageContent = new HomepageContent(username, user.isAdmin(),
                prodOfTheDay.getName(), prodOfTheDay.getDescription(), encoded, reviews, userStatus);
        String jsonHomepage = new Gson().toJson(homepageContent);
        System.out.println(jsonHomepage);
        out.write(jsonHomepage);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setStatus(200);
        return;
    }
}