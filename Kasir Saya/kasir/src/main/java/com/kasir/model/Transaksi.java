package com.kasir.model;

public class Transaksi {
    private int id;
    private String noFaktur;
    private java.sql.Timestamp tanggal;
    private int kasirId;
    private String kasir;
    private long total;
    private long bayar;
    private long kembali;
    private java.util.List<CartItem> items;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNoFaktur() { return noFaktur; }
    public void setNoFaktur(String noFaktur) { this.noFaktur = noFaktur; }
    public java.sql.Timestamp getTanggal() { return tanggal; }
    public void setTanggal(java.sql.Timestamp tanggal) { this.tanggal = tanggal; }
    public int getKasirId() { return kasirId; }
    public void setKasirId(int kasirId) { this.kasirId = kasirId; }
    public String getKasir() { return kasir; }
    public void setKasir(String kasir) { this.kasir = kasir; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public long getBayar() { return bayar; }
    public void setBayar(long bayar) { this.bayar = bayar; }
    public long getKembali() { return kembali; }
    public void setKembali(long kembali) { this.kembali = kembali; }
    public java.util.List<CartItem> getItems() { return items; }
    public void setItems(java.util.List<CartItem> items) { this.items = items; }

    public Transaksi() { this.items = new java.util.ArrayList<>(); }
}
