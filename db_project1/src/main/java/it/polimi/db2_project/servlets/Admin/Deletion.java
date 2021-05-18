package it.polimi.db2_project.servlets.Admin;


import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.services.ProductService;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        if(sDate.equals("")) {
            return;
        }
        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(400);
        }

        Product product= null;

        if(checkDate(date)) {
            try {
                product =productService.getProductOfTheDay(date);
                if(product!= null)
                    productService.deleteProduct(product);

            }catch (Exception e) {
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

    boolean checkDate (Date date) {
        return java.sql.Date.valueOf(LocalDate.now()).after(date);
    }


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
