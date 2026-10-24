# Aplikasi Kasir (Java Web, JSP + Servlet + MySQL)

## Kebutuhan
- JDK 11+ (disarankan 17)
- Apache NetBeans (versi 12+), dengan dukungan Java Web / Maven
- Apache Tomcat 9.x (javax.servlet)  -> JANGAN pakai Tomcat 10+, namespace-nya jakarta.*
- MySQL / MariaDB (bisa lewat XAMPP)

## Langkah
1. Jalankan MySQL, lalu import `database/kasir.sql` (phpMyAdmin > Import, atau `mysql -u root < database/kasir.sql`).
2. Buka `src/main/java/com/kasir/util/DB.java`, sesuaikan USER dan PASS MySQL Anda.
3. NetBeans: File > Open Project > pilih folder `kasir`.
4. Tools > Servers > Add Server > Apache Tomcat 9, lalu klik kanan project > Properties > Run > pilih server tsb.
5. Klik kanan project > Clean and Build, lalu Run.
6. Buka http://localhost:8080/kasir

## Akun awal
| Username | Password  | Role  |
|----------|-----------|-------|
| admin    | admin123  | ADMIN |
| kasir    | kasir123  | KASIR |

Segera ganti/hapus akun ini setelah dipakai sungguhan (menu User).

## Fitur
- Login / logout (session, password di-hash PBKDF2), pembatasan akses per role
- Dashboard: penjualan & jumlah transaksi hari ini, total produk, peringatan stok menipis
- Kasir: cari produk, scan/ketik kode, keranjang, ubah qty, hapus item, hitung kembalian, bayar
- Stok berkurang otomatis (dalam satu transaksi database), cegah stok minus
- Cetak struk (print browser)
- Produk (admin): tambah, ubah, hapus, cari
- Laporan penjualan (admin): filter rentang tanggal, total omzet, lihat ulang struk
- Manajemen user (admin)
