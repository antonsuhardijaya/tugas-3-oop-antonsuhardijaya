package com.kasir.model;

public class CartItem {
    private int produkId;
    private String kode;
    private String nama;
    private long harga;
    private int qty;

    public int getProdukId() { return produkId; }
    public void setProdukId(int produkId) { this.produkId = produkId; }
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public long getHarga() { return harga; }
    public void setHarga(long harga) { this.harga = harga; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public long getSubtotal() { return harga * qty; }
}
