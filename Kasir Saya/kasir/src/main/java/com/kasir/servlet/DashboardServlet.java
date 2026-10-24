package com.kasir.servlet;

import com.kasir.dao.ProdukDAO;
import com.kasir.dao.TransaksiDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final ProdukDAO produkDAO = new ProdukDAO();
    private final TransaksiDAO transaksiDAO = new TransaksiDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long[] r = transaksiDAO.ringkasanHariIni();
        req.setAttribute("judul", "Dashboard");
        req.setAttribute("menu", "dashboard");
        req.setAttribute("penjualanHariIni", r[0]);
        req.setAttribute("transaksiHariIni", r[1]);
        req.setAttribute("totalProduk", produkDAO.count());
        req.setAttribute("stokMenipis", produkDAO.stokMenipis(5));
        req.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(req, resp);
    }
}
