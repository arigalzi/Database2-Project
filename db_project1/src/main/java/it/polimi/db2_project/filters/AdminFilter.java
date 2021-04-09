package it.polimi.db2_project.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@javax.servlet.annotation.WebFilter(filterName = "AdminFilter")
public class AdminFilter implements javax.servlet.Filter {
    public void destroy() {
    }

    public void doFilter(javax.servlet.ServletRequest req, javax.servlet.ServletResponse resp, javax.servlet.FilterChain chain) throws javax.servlet.ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String loginPath = req.getServletContext().getContextPath() + "/index.html";

        HttpSession session = request.getSession();

        //if it's not logged it
        if (session.isNew() || session.getAttribute("admin") == null) {
            session.invalidate();
            response.sendRedirect(loginPath);
            (response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("error: unauthorized user, please log in as an admin");
            return;
        }

        chain.doFilter(req, resp);
    }

    public void init(javax.servlet.FilterConfig config) throws javax.servlet.ServletException {

    }

}
