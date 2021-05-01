package it.polimi.db2_project.servlets;

import it.polimi.db2_project.entities.User;
import it.polimi.db2_project.services.AdminService;
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
        try {
            getServletConfig().getServletContext().getRequestDispatcher("/error.html").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
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
        return username != null && username.length()<32 && username.length() > 3;
    }

    /**
     * Method to check username validity
     * @param password password to check
     * @return true if it's valid, false otherwise
     */
    boolean isPasswordValid(String password){
        return password != null && password.length()<16 && password.length() > 3;
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
            }
            //USER ALREADY EXISTING
            catch (PersistenceException | IllegalArgumentException | EJBException e) {
                if (e.getCause().getCause().getMessage().contains("Duplicate entry")) {
                    sendError(response, request,"Invalid Completion", "username already taken");
                }
                else {
                    sendError(response, request, "Invalid Completion","internal server error");
                }
            }
        }

        // LOGIN
        else if (isSignUp != null && isSignUp.equals("false")) {
            //username and password validation
            if (!(isUsernameValid(username) && isPasswordValid(password))) {
                sendError(response, request,"Invalid Completion","invalid username or password");
                return;
            }
            //login
            try {
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
                    sendError(response, request, "Invalid Completion", f.getCause().getMessage());
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
/*TODO Va gestito il messaggio di errore se uno fa login senza essere registrato*/