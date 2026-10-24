package com.kasir.util;

import javax.servlet.http.HttpServletRequest;

/** Pesan sekali tampil (disimpan di session, dihapus setelah ditampilkan oleh header.jspf). */
public class Flash {
    public static void ok(HttpServletRequest r, String msg) { set(r, "ok", msg); }
    public static void err(HttpServletRequest r, String msg) { set(r, "err", msg); }
    private static void set(HttpServletRequest r, String type, String msg) {
        r.getSession().setAttribute("flashType", type);
        r.getSession().setAttribute("flash", msg);
    }
}
