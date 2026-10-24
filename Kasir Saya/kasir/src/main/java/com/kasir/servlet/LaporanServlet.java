package com.kasir.servlet;

import com.kasir.dao.TransaksiDAO;
import com.kasir.model.Transaksi;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/laporan")
public class LaporanServlet extends HttpServlet {
    private final TransaksiDAO dao = new TransaksiDAO();

    private LocalDate parse(String s, LocalDate def) {
        try { return LocalDate.parse(s); } catch (RuntimeException e) { return def; }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate today = LocalDate.now();
        LocalDate dari = parse(req.getParameter("dari"), today);
        LocalDate sampai = parse(req.getParameter("sampai"), today);
        if (sampai.isBefore(dari)) sampai = dari;

        List<Transaksi> list = dao.laporan(java.sql.Date.valueOf(dari), java.sql.Date.valueOf(sampai));
        long omzet = 0;
        for (Transaksi t : list) omzet += t.getTotal();

        req.setAttribute("judul", "Laporan Penjualan");
        req.setAttribute("menu", "laporan");
        req.setAttribute("dari", dari.toString());
        req.setAttribute("sampai", sampai.toString());
        req.setAttribute("daftar", list);
        req.setAttribute("omzet", omzet);
        req.getRequestDispatcher("/WEB-INF/jsp/laporan.jsp").forward(req, resp);
    }
}
