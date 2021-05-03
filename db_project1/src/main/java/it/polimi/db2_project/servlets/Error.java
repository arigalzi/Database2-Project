package it.polimi.db2_project.servlets;

import com.google.gson.Gson;
import it.polimi.db2_project.auxiliary.jsonContent.ErrorContent;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Error")
public class Error extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String errorType = (String) request.getSession().getAttribute("errorType");
        request.getSession().removeAttribute("errorType");
        String errorInfo = (String) request.getSession().getAttribute("errorInfo");
        request.getSession().removeAttribute("errorInfo");
        ErrorContent errorContent = new ErrorContent(errorType, errorInfo);
        String jsonError = new Gson().toJson(errorContent);
        out.print(jsonError);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}