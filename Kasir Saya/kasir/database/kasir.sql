CREATE DATABASE IF NOT EXISTS db_kasir CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_kasir;

DROP TABLE IF EXISTS detail_transaksi;
DROP TABLE IF EXISTS transaksi;
DROP TABLE IF EXISTS produk;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(200) NOT NULL,
  nama VARCHAR(100) NOT NULL,
  role ENUM('ADMIN','KASIR') NOT NULL DEFAULT 'KASIR'
) ENGINE=InnoDB;

CREATE TABLE produk (
  id INT AUTO_INCREMENT PRIMARY KEY,
  kode VARCHAR(30) NOT NULL UNIQUE,
  nama VARCHAR(150) NOT NULL,
  harga BIGINT NOT NULL,
  stok INT NOT NULL DEFAULT 0
) ENGINE=InnoDB;

CREATE TABLE transaksi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  no_faktur VARCHAR(30) NOT NULL UNIQUE,
  tanggal DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  kasir_id INT NULL,
  total BIGINT NOT NULL,
  bayar BIGINT NOT NULL,
  kembali BIGINT NOT NULL,
  FOREIGN KEY (kasir_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE detail_transaksi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  transaksi_id INT NOT NULL,
  produk_id INT NULL,
  kode VARCHAR(30) NOT NULL,
  nama_produk VARCHAR(150) NOT NULL,
  harga BIGINT NOT NULL,
  qty INT NOT NULL,
  FOREIGN KEY (transaksi_id) REFERENCES transaksi(id) ON DELETE CASCADE,
  FOREIGN KEY (produk_id) REFERENCES produk(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Akun awal: admin / admin123  dan  kasir / kasir123
INSERT INTO users (username,password,nama,role) VALUES
('admin','65000:81e0ac77f96d6addd63accff5589db42:2e8811db0c605968a93703aae2dff3aaff2c57b91862400a918d6e26157d9331','Administrator','ADMIN'),
('kasir','65000:0affd3d65eb6729e983423db2a6e6182:45ef49695bdef19b793cc05de1b9b80d29505b65fb088f3657107ff711aa4f56','Kasir 1','KASIR');

INSERT INTO produk (kode,nama,harga,stok) VALUES
('8991001','Indomie Goreng',3500,100),
('8991002','Aqua 600ml',4000,80),
('8991003','Teh Botol Sosro',5000,60),
('8991004','Roti Tawar',16000,20),
('8991005','Gula Pasir 1kg',17500,30),
('8991006','Minyak Goreng 1L',18000,25),
('8991007','Kopi Kapal Api Sachet',2000,200),
('8991008','Telur Ayam 1kg',28000,4);
