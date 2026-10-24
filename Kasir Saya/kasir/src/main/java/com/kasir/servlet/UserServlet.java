package com.kasir.servlet;

import com.kasir.dao.UserDAO;
import com.kasir.model.User;
import com.kasir.util.Flash;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("judul", "Manajemen User");
        req.setAttribute("menu", "user");
        req.setAttribute("daftar", dao.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User me = (User) req.getSession().getAttribute("user");
        if ("delete".equals(req.getParameter("action"))) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                if (id == me.getId()) Flash.err(req, "Anda tidak bisa menghapus akun yang sedang dipakai.");
                else {
                    dao.delete(id);
                    Flash.ok(req, "User dihapus.");
                }
            } catch (NumberFormatException e) {
                Flash.err(req, "ID tidak valid.");
            }
        } else {
            String username = val(req, "username"), nama = val(req, "nama"), password = val(req, "password");
            String role = "ADMIN".equals(req.getParameter("role")) ? "ADMIN" : "KASIR";
            if (username.isEmpty() || nama.isEmpty() || password.length() < 6) {
                Flash.err(req, "Isi semua kolom; password minimal 6 karakter.");
            } else if (dao.exists(username)) {
                Flash.err(req, "Username sudah dipakai.");
            } else {
                User u = new User();
                u.setUsername(username);
                u.setNama(nama);
                u.setRole(role);
                dao.insert(u, password);
                Flash.ok(req, "User ditambahkan.");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/user");
    }

    private String val(HttpServletRequest r, String k) {
        String v = r.getParameter(k);
        return v == null ? "" : v.trim();
    }
}
