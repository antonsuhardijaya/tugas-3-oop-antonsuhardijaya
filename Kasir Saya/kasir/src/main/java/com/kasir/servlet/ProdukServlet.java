package com.kasir.servlet;

import com.kasir.dao.ProdukDAO;
import com.kasir.model.Produk;
import com.kasir.util.Flash;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/produk")
public class ProdukServlet extends HttpServlet {
    private final ProdukDAO dao = new ProdukDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("menu", "produk");
        if ("form".equals(req.getParameter("action"))) {
            Produk p = null;
            try { p = dao.findById(Integer.parseInt(req.getParameter("id"))); } catch (NumberFormatException ignored) { }
            req.setAttribute("judul", p == null ? "Tambah Produk" : "Edit Produk");
            req.setAttribute("p", p == null ? new Produk() : p);
            req.getRequestDispatcher("/WEB-INF/jsp/produk-form.jsp").forward(req, resp);
            return;
        }
        String q = req.getParameter("q");
        req.setAttribute("judul", "Data Produk");
        req.setAttribute("daftar", dao.findAll(q == null ? "" : q.trim()));
        req.getRequestDispatcher("/WEB-INF/jsp/produk.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            try {
                dao.delete(Integer.parseInt(req.getParameter("id")));
                Flash.ok(req, "Produk dihapus.");
            } catch (NumberFormatException e) {
                Flash.err(req, "ID tidak valid.");
            }
            resp.sendRedirect(req.getContextPath() + "/produk");
            return;
        }
        // simpan
        Produk p = new Produk();
        try {
            String id = req.getParameter("id");
            p.setId(id == null || id.isEmpty() ? 0 : Integer.parseInt(id));
            p.setKode(req.getParameter("kode").trim());
            p.setNama(req.getParameter("nama").trim());
            p.setHarga(Long.parseLong(req.getParameter("harga")));
            p.setStok(Integer.parseInt(req.getParameter("stok")));
        } catch (RuntimeException e) {
            p = null;
        }
        String error = null;
        if (p == null || p.getKode().isEmpty() || p.getNama().isEmpty()) error = "Data tidak lengkap / tidak valid.";
        else if (p.getHarga() < 0 || p.getStok() < 0) error = "Harga dan stok tidak boleh negatif.";
        else {
            Produk ada = dao.findByKode(p.getKode());
            if (ada != null && ada.getId() != p.getId()) error = "Kode produk sudah dipakai.";
        }
        if (error != null) {
            Flash.err(req, error);
            String back = (p != null && p.getId() > 0) ? "&id=" + p.getId() : "";
            resp.sendRedirect(req.getContextPath() + "/produk?action=form" + back);
            return;
        }
        dao.save(p);
        Flash.ok(req, "Produk disimpan.");
        resp.sendRedirect(req.getContextPath() + "/produk");
    }
}
