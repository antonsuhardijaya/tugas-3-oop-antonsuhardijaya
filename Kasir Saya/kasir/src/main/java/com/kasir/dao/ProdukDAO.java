package com.kasir.dao;

import com.kasir.model.Produk;
import com.kasir.util.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukDAO {

    private Produk map(ResultSet rs) throws SQLException {
        Produk p = new Produk();
        p.setId(rs.getInt("id"));
        p.setKode(rs.getString("kode"));
        p.setNama(rs.getString("nama"));
        p.setHarga(rs.getLong("harga"));
        p.setStok(rs.getInt("stok"));
        return p;
    }

    public List<Produk> findAll(String q) {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT * FROM produk WHERE nama LIKE ? OR kode LIKE ? ORDER BY nama";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + q + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private Produk one(String sql, Object param) {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Produk findById(int id) { return one("SELECT * FROM produk WHERE id=?", id); }

    public Produk findByKode(String kode) { return one("SELECT * FROM produk WHERE kode=?", kode); }

    public void save(Produk p) {
        boolean baru = p.getId() == 0;
        String sql = baru
                ? "INSERT INTO produk (kode,nama,harga,stok) VALUES (?,?,?,?)"
                : "UPDATE produk SET kode=?, nama=?, harga=?, stok=? WHERE id=?";
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getKode());
            ps.setString(2, p.getNama());
            ps.setLong(3, p.getHarga());
            ps.setInt(4, p.getStok());
            if (!baru) ps.setInt(5, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM produk WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count() {
        try (Connection c = DB.get(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM produk")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Produk> stokMenipis(int batas) {
        List<Produk> list = new ArrayList<>();
        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM produk WHERE stok<=? ORDER BY stok LIMIT 10")) {
            ps.setInt(1, batas);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
