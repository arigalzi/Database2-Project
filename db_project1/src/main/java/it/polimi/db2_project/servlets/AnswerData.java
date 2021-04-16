
package it.polimi.db2_project.servlets;

import it.polimi.db2_project.services.AnswerService;
import it.polimi.db2_project.services.UserService;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/AnswerData"})
public class AnswerData extends HttpServlet {
    @EJB(
            name = "it.polimi.db2.entities.services/AnswerService"
    )
    private AnswerService answerService;
    @EJB(
            name = "it.polimi.db2.entities.services/UserService"
    )
    private UserService userService;

    public AnswerData() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String)request.getSession().getAttribute("user");
        String[] mandatory_answers = request.getParameterValues("mandatory");
        if (this.answerService.hasDirtyWord(this.answerService.correctAnswerFormat(mandatory_answers))) {
            this.userService.banUser(username);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
