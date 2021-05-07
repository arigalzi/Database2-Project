package it.polimi.db2_project.servlets;

import it.polimi.db2_project.entities.Question;
import it.polimi.db2_project.entities.User;
import it.polimi.db2_project.services.AnswerService;
import it.polimi.db2_project.services.ProductService;
import it.polimi.db2_project.services.QuestionnaireService;
import it.polimi.db2_project.services.UserService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/CreationData")
@MultipartConfig
public class CreationData extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;

    @EJB(name = "it.polimi.db2.entities.services/QuestionnaireService")
    private QuestionnaireService questionnaireService;
    public CreationData() {
    }





    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
        }
        //Manage the responses
        String productName = null;
        String productDate = null;
        String productDescription = null;
        List<String> productQuestions = new ArrayList<>();
        List<String> productReviews = new ArrayList<>();
        Part productImage;

        //Reading the HTTP request for the Form
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            switch (paramName){
                case "productQuestion": productQuestions = Arrays.asList(request.getParameterValues(paramName)); break;
                case "productReview": productReviews = Arrays.asList(request.getParameterValues(paramName)); break;
                case "productName": productName = request.getParameter(paramName); break;
                case "productDate": productDate = request.getParameter(paramName); break;
                case "productDescription": productDescription = request.getParameter(paramName); break;
            }
            System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }

        productImage = request.getPart("productImage");
        InputStream fileContent = productImage.getInputStream();

        System.out.println(productName);
        System.out.println(productDate);
        System.out.println(productDescription);
        System.out.println(productQuestions);
        System.out.println(fileContent);
        System.out.println(productReviews);

        Date date =null;
        try {
            date =new SimpleDateFormat("yyyy-MM-dd").parse(productDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Date " + date);

        byte[] image = ProductService.readImage(fileContent);

        if(productService.checkDateAvailability(date) == null) {
            productService.createNewProduct(productName, productDescription, date, productQuestions, image,productReviews);
        }
        else{
            response.setStatus(400);
        }

        System.out.println("Request managed by CreationData");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}


