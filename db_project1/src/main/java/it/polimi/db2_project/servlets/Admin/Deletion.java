package it.polimi.db2_project.servlets.Admin;


import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.services.ProductService;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Deletion")
@MultipartConfig
public class Deletion extends HttpServlet {

    private ProductService productService;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the id of the product to be deleted
        int productId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("prodId")));
        //Product product = productService.getProduct(productId);

        //delete the product and delete it also from the users' list
        //productService.deleteProduct(product);

        response.sendRedirect("Admin/past.html?");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
