package com.kasir.servlet;

import com.kasir.dao.TransaksiDAO;
import com.kasir.model.Transaksi;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/struk")
public class StrukServlet extends HttpServlet {
    private final TransaksiDAO dao = new TransaksiDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Transaksi t = null;
        try { t = dao.findById(Integer.parseInt(req.getParameter("id"))); } catch (NumberFormatException ignored) { }
        if (t == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Transaksi tidak ditemukan");
            return;
        }
        req.setAttribute("t", t);
        req.getRequestDispatcher("/WEB-INF/jsp/struk.jsp").forward(req, resp);
    }
}
