package com.kasir.filter;

import com.kasir.model.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

/** Memaksa login untuk semua halaman, dan membatasi menu admin. */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();
        boolean publik = path.equals("/login") || path.startsWith("/css/");

        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (!publik && user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        boolean adminOnly = path.startsWith("/produk") || path.startsWith("/user") || path.startsWith("/laporan");
        if (adminOnly && !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Hanya untuk admin");
            return;
        }
        chain.doFilter(request, response);
    }
}
