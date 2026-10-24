package com.kasir.servlet;

import com.kasir.dao.UserDAO;
import com.kasir.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User u = (username == null || password == null) ? null : userDAO.login(username.trim(), password);
        if (u == null) {
            req.setAttribute("error", "Username atau password salah.");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }
        HttpSession old = req.getSession(false);
        if (old != null) old.invalidate();          // cegah session fixation
        HttpSession s = req.getSession(true);
        s.setAttribute("user", u);
        s.setMaxInactiveInterval(30 * 60);
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
