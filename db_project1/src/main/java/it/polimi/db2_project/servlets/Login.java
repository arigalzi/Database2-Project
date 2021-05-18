package it.polimi.db2_project.servlets;

import com.google.gson.Gson;
import it.polimi.db2_project.entities.User;
import it.polimi.db2_project.services.UserService;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;


@WebServlet
public class Login extends HttpServlet {
    @EJB(name = "it.polimi.db2_project.entities.services/UserService")
    private UserService userService;

    /**
     * Method to handle errors, redirects to an error page
     * @param request request
     * @param response response
     * @param errorType type of error
     * @param errorInfo information about the error
     * @throws IOException if there are problems redirecting
     */
    protected void sendError(HttpServletResponse response, HttpServletRequest request, String errorType, String errorInfo) throws IOException {
        request.getSession().setAttribute ("errorType", errorType);
        request.getSession().setAttribute ("errorInfo", errorInfo);
        String path = getServletContext().getContextPath() + "/error.html";
        response.sendRedirect(path);
        return;
    }

    /**
     * Method to check email validity
     * @param email email to check
     * @return true if it's valid, false otherwise
     */
    boolean isEmailValid(String email) {
        return email != null && EmailValidator.getInstance().isValid(email);
    }

    /**
     * Method to check username validity
     * @param username username to check
     * @return true if it's valid, false otherwise
     */
    boolean isUsernameValid(String username) {
        return username != null && username.length()<32 && username.length() > 1;
    }

    /**
     * Method to check username validity
     * @param password password to check
     * @return true if it's valid, false otherwise
     */
    boolean isPasswordValid(String password){
        return password != null && password.length()<16 && password.length() > 1;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String isSignUp = StringEscapeUtils.escapeJava(request.getParameter("signUp"));

        //SIGNUP OR LOGIN

        if (isSignUp != null && isSignUp.equals("true")) {

            //SIGNUP
            if (!(isEmailValid(email) && isUsernameValid(username) && isPasswordValid(password))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Wrong Credentials");
                sendError(response, request,"Invalid Completion","invalid username, email or password format");
                return;
            }

            //try to register a new user
            try {
                User user = userService.addUser(username,email,password,false);
                userService.LogUser(user);
                request.getSession().setAttribute("user", username);
                String path = getServletContext().getContextPath() + "/homePage.html";
                response.sendRedirect(path);
                return;
            }

            //USER ALREADY EXISTING
            catch (PersistenceException | IllegalArgumentException | EJBException e) {
                if (e.getCause().getCause().getMessage().contains("Duplicate entry")) {
                    sendError(response, request,"Invalid Completion", "username already taken");
                }
                else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    sendError(response, request, "Invalid Completion","internal server error");
                }
            }
        }
        // LOGIN
        else{
            try {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                User credentialCheckResultUser = userService.checkCredentials(username, password);
                userService.LogUser(credentialCheckResultUser);

                request.getSession().setAttribute("user", credentialCheckResultUser.getUsername());

                //Admin Login
                if(credentialCheckResultUser.isAdmin()){
                    request.getSession().setAttribute("admin", credentialCheckResultUser.getUserID());
                    String path = getServletContext().getContextPath() + "/adminHomePage.html";
                    response.sendRedirect(path);
                }
                //Casual User Login
                else{
                    String path = getServletContext().getContextPath() + "/homePage.html";
                    response.sendRedirect(path);
                }
            }
            catch (InvalidParameterException | EJBException f) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                sendError(response, request, "Invalid Completion", "login error");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String)request.getSession().getAttribute("user");
        PrintWriter out = response.getWriter();
        out.print((new Gson()).toJson(username));
    }
}
