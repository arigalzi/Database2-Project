package it.polimi.db2_project.servlets.Admin;

import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.services.ProductService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@WebServlet("/Deletion")
@MultipartConfig
public class Deletion extends HttpServlet {

    @EJB(name = "it.polimi.db2_project.entities.services/ProductService")
    private ProductService productService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sDate = request.getParameter("date");
        Product product = null;
        Date date = null;

        if(sDate.equals("")) {
            return;
        }
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(400);
        }

        if(checkDate(date)) {
            try {
                product = productService.getProductOfTheDay(date);
                productService.deleteProduct(product);
            }catch (InvalidParameterException e) {
                sendError(request, response, "Deletion Error", e.getCause().getMessage());
            }
        }
        else {
            response.setStatus(400);
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    /**
     * Method to check date validity
     * @param date of a specific day
     * @return true if date is previous, false otherwise
     */
    boolean checkDate (Date date) {
        return java.sql.Date.valueOf(LocalDate.now()).after(date);
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
