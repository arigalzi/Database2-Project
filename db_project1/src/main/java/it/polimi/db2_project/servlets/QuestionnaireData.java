package it.polimi.db2_project.servlets;

import com.google.gson.Gson;
import it.polimi.db2_project.entities.Question;
import it.polimi.db2_project.services.QuestionnaireService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/QuestionnaireData"})
public class QuestionnaireData extends HttpServlet {
    @EJB(
            name = "it.polimi.db2.entities.services/QuestionnaireService"
    )
    private QuestionnaireService questionnaireService;

    public QuestionnaireData() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        List<Question> questionList = this.questionnaireService.getQuestionsOfTheDay();
        this.questionnaireService.orderByQuestionNumber(questionList);
        out.print((new Gson()).toJson(questionList));
    }
}
