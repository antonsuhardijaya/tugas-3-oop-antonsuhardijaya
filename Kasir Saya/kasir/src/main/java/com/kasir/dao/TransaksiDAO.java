package com.kasir.dao;

import com.kasir.model.CartItem;
import com.kasir.model.Transaksi;
import com.kasir.util.DB;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransaksiDAO {

    /**
     * Menyimpan transaksi + detail dan mengurangi stok dalam satu transaksi database.
     * @throws IllegalStateException bila stok tidak mencukupi
     * @return id transaksi baru
     */
    public int simpan(Transaksi t) {
        Connection c = null;
        try {
            c = DB.get();
            c.setAutoCommit(false);

            for (CartItem i : t.getItems()) {
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE produk SET stok=stok-? WHERE id=? AND stok>=?")) {
                    ps.setInt(1, i.getQty());
                    ps.setInt(2, i.getProdukId());
                    ps.setInt(3, i.getQty());
                    if (ps.executeUpdate() == 0)
                        throw new IllegalStateException("Stok tidak cukup untuk " + i.getNama());
                }
            }

            String faktur = "INV" + new SimpleDateFormat("yyMMddHHmmss").format(new Date())
                    + (10 + (int) (Math.random() * 90));
            int id;
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO transaksi (no_faktur,kasir_id,total,bayar,kembali) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, faktur);
                ps.setInt(2, t.getKasirId());
                ps.setLong(3, t.getTotal());
                ps.setLong(4, t.getBayar());
                ps.setLong(5, t.getKembali());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    id = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO detail_transaksi (transaksi_id,produk_id,kode,nama_produk,harga,qty) VALUES (?,?,?,?,?,?)")) {
                for (CartItem i : t.getItems()) {
                    ps.setInt(1, id);
                    ps.setInt(2, i.getProdukId());
                    ps.setString(3, i.getKode());
                    ps.setString(4, i.getNama());
                    ps.setLong(5, i.getHarga());
                    ps.setInt(6, i.getQty());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            c.commit();
            return id;
        } catch (IllegalStateException e) {
            rollback(c);
            throw e;
        } catch (SQLException e) {
            rollback(c);
            throw new RuntimeException(e);
        } finally {
            if (c != null) try { c.close(); } catch (SQLException ignored) { }
        }
    }

    private void rollback(Connection c) {
        if (c != null) try { c.rollback(); } catch (SQLException ignored) { }
    }

    private Transaksi map(ResultSet rs) throws SQLException {
        Transaksi t = new Transaksi();
        t.setId(rs.getInt("id"));
        t.setNoFaktur(rs.getString("no_faktur"));
        t.setTanggal(rs.getTimestamp("tanggal"));
        t.setKasir(rs.getString("kasir"));
        t.setTotal(rs.getLong("total"));
        t.setBayar(rs.getLong("bayar"));
        t.setKembali(rs.getLong("kembali"));
        return t;
    }

    private static final String SELECT_T =
            "SELECT t.*, u.nama AS kasir FROM transaksi t LEFT JOIN users u ON u.id=t.kasir_id ";

    /** Transaksi lengkap dengan item (untuk struk). */
    public Transaksi findById(int id) {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(SELECT_T + "WHERE t.id=?")) {
            ps.setInt(1, id);
            Transaksi t;
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                t = map(rs);
            }
            try (PreparedStatement ps2 = c.prepareStatement(
                    "SELECT produk_id,kode,nama_produk,harga,qty FROM detail_transaksi WHERE transaksi_id=? ORDER BY id")) {
                ps2.setInt(1, id);
                try (ResultSet rs = ps2.executeQuery()) {
                    while (rs.next()) {
                        CartItem i = new CartItem();
                        i.setProdukId(rs.getInt("produk_id"));
                        i.setKode(rs.getString("kode"));
                        i.setNama(rs.getString("nama_produk"));
                        i.setHarga(rs.getLong("harga"));
                        i.setQty(rs.getInt("qty"));
                        t.getItems().add(i);
                    }
                }
            }
            return t;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Daftar transaksi dalam rentang tanggal (inklusif). */
    public List<Transaksi> laporan(java.sql.Date dari, java.sql.Date sampai) {
        List<Transaksi> list = new ArrayList<>();
        String sql = SELECT_T + "WHERE DATE(t.tanggal) BETWEEN ? AND ? ORDER BY t.tanggal DESC";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, dari);
            ps.setDate(2, sampai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /** {total penjualan hari ini, jumlah transaksi hari ini} */
    public long[] ringkasanHariIni() {
        String sql = "SELECT COALESCE(SUM(total),0), COUNT(*) FROM transaksi WHERE DATE(tanggal)=CURDATE()";
        try (Connection c = DB.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return new long[]{rs.getLong(1), rs.getLong(2)};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
