package com.kasir.servlet;

import com.kasir.dao.ProdukDAO;
import com.kasir.dao.TransaksiDAO;
import com.kasir.model.CartItem;
import com.kasir.model.Produk;
import com.kasir.model.Transaksi;
import com.kasir.model.User;
import com.kasir.util.Flash;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** Halaman kasir: keranjang disimpan di session, pembayaran menyimpan transaksi & mengurangi stok. */
@WebServlet("/kasir")
public class KasirServlet extends HttpServlet {
    private final ProdukDAO produkDAO = new ProdukDAO();
    private final TransaksiDAO transaksiDAO = new TransaksiDAO();

    @SuppressWarnings("unchecked")
    private Map<Integer, CartItem> cart(HttpSession s) {
        Map<Integer, CartItem> m = (Map<Integer, CartItem>) s.getAttribute("cart");
        if (m == null) {
            m = new LinkedHashMap<>();
            s.setAttribute("cart", m);
        }
        return m;
    }

    private long total(Map<Integer, CartItem> m) {
        long t = 0;
        for (CartItem i : m.values()) t += i.getSubtotal();
        return t;
    }

    private int toInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (RuntimeException e) { return 0; }
    }

    private long toLong(String s) {
        try { return Long.parseLong(s.replaceAll("\\D", "")); } catch (RuntimeException e) { return 0; }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        Map<Integer, CartItem> cart = cart(req.getSession());
        req.setAttribute("judul", "Kasir");
        req.setAttribute("menu", "kasir");
        req.setAttribute("produk", produkDAO.findAll(q == null ? "" : q.trim()));
        req.setAttribute("items", cart.values());
        req.setAttribute("total", total(cart));
        req.getRequestDispatcher("/WEB-INF/jsp/kasir.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<Integer, CartItem> cart = cart(session);
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        String redirect = req.getContextPath() + "/kasir";

        switch (action) {
            case "add": {
                String kode = req.getParameter("kode") == null ? "" : req.getParameter("kode").trim();
                Produk p = kode.isEmpty() ? produkDAO.findById(toInt(req.getParameter("id"))) : produkDAO.findByKode(kode);
                if (p == null) {
                    Flash.err(req, "Produk tidak ditemukan.");
                    break;
                }
                CartItem ci = cart.get(p.getId());
                int qty = ci == null ? 1 : ci.getQty() + 1;
                if (qty > p.getStok()) {
                    Flash.err(req, "Stok " + p.getNama() + " tidak cukup (sisa " + p.getStok() + ").");
                    break;
                }
                if (ci == null) {
                    ci = new CartItem();
                    ci.setProdukId(p.getId());
                    ci.setKode(p.getKode());
                    ci.setNama(p.getNama());
                    ci.setHarga(p.getHarga());
                    cart.put(p.getId(), ci);
                }
                ci.setQty(qty);
                break;
            }
            case "set": {
                int id = toInt(req.getParameter("id"));
                int qty = toInt(req.getParameter("qty"));
                CartItem ci = cart.get(id);
                if (ci == null) break;
                if (qty <= 0) {
                    cart.remove(id);
                } else {
                    Produk p = produkDAO.findById(id);
                    if (p != null && qty > p.getStok()) Flash.err(req, "Stok " + p.getNama() + " hanya " + p.getStok() + ".");
                    else ci.setQty(qty);
                }
                break;
            }
            case "remove":
                cart.remove(toInt(req.getParameter("id")));
                break;
            case "clear":
                cart.clear();
                break;
            case "pay": {
                if (cart.isEmpty()) {
                    Flash.err(req, "Keranjang masih kosong.");
                    break;
                }
                long total = total(cart);
                long bayar = toLong(req.getParameter("bayar") == null ? "" : req.getParameter("bayar"));
                if (bayar < total) {
                    Flash.err(req, "Uang bayar kurang dari total.");
                    break;
                }
                Transaksi t = new Transaksi();
                t.setKasirId(((User) session.getAttribute("user")).getId());
                t.setTotal(total);
                t.setBayar(bayar);
                t.setKembali(bayar - total);
                t.getItems().addAll(cart.values());
                try {
                    int id = transaksiDAO.simpan(t);
                    cart.clear();
                    resp.sendRedirect(req.getContextPath() + "/struk?id=" + id);
                    return;
                } catch (IllegalStateException e) {
                    Flash.err(req, e.getMessage());
                }
                break;
            }
            default:
                break;
        }
        resp.sendRedirect(redirect);
    }
}
