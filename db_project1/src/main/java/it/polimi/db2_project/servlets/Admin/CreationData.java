package it.polimi.db2_project.servlets.Admin;
import it.polimi.db2_project.services.ProductService;

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
import java.time.LocalDate;
import java.util.*;

@WebServlet("/CreationData")
@MultipartConfig
public class CreationData extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    public CreationData() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        //Get the image
        productImage = request.getPart("productImage");
        InputStream fileContent = productImage.getInputStream();

        //UTILITY PRINTS
        System.out.println(productName);
        System.out.println(productDate);
        System.out.println(productDescription);
        System.out.println(productQuestions);
        System.out.println(fileContent);
        System.out.println(productReviews);

        //Check whether input fields are empty
        if(checkPostValidity(productName,productDate,productDescription)) {
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(productDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println("Date " + date);

            byte[] image = ProductService.readImage(fileContent);

            //Check whether the date is present or posterior
            if(date.before(java.sql.Date.valueOf(LocalDate.now()))) {
                response.setStatus(403); // FORBIDDEN INPUT DATE
                return;
            }
            if (productService.checkDateAvailability(date) == null) {
                productService.createNewProduct(productName, productDescription, date, productQuestions, image, productReviews);
                response.setStatus(200);
            } else {
                //Conflict, date already existing
                response.setStatus(409);
            }

            System.out.println("Request managed by CreationData");
        }
        else{
            //Bad request, input is empty
            response.setStatus(400);
            System.out.println("One of the required fields was not filled...Request was not managed");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }


    /**
     * Utility method to check whether the mandatory fields parameters are non-empty
     * @param productName
     * @param date
     * @param description
     * @return
     */
    private boolean checkPostValidity(String productName,String date,String description){
        return !productName.equals("") && !date.equals("") && !description.equals("");
    }
}


