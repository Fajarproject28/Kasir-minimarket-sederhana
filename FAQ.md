# FAQ (Frequently Asked Questions)

Pertanyaan yang sering ditanyakan tentang Aplikasi Kasir Minimarket.

## Instalasi & Setup

### Q: Apa saja yang harus diinstall sebelum menjalankan aplikasi?
**A:** Anda memerlukan:
1. JDK (Java Development Kit) 8 atau lebih tinggi
2. MariaDB atau MySQL database server
3. NetBeans IDE 8.2 atau lebih tinggi
4. MariaDB JDBC Driver

### Q: Apakah bisa menggunakan MySQL instead of MariaDB?
**A:** Ya, bisa! MariaDB dan MySQL kompatibel. Cukup sesuaikan driver JDBC:
- MariaDB: `org.mariadb.jdbc.Driver`
- MySQL: `com.mysql.cj.jdbc.Driver`

### Q: Bagaimana cara install MariaDB JDBC Driver?
**A:** Ada 2 cara:
1. **Manual**: Download dari https://mariadb.com/downloads/connectors/ dan tambahkan ke Libraries di NetBeans
2. **Maven**: Tambahkan dependency di pom.xml (jika menggunakan Maven)

### Q: Error "ClassNotFoundException: org.mariadb.jdbc.Driver", apa solusinya?
**A:** JDBC driver belum ditambahkan ke project. Ikuti langkah:
1. Klik kanan project → Properties
2. Libraries → Add JAR/Folder
3. Pilih file mariadb-java-client-x.x.x.jar
4. Clean and Build project

### Q: Database connection failed, kenapa?
**A:** Cek hal berikut:
1. MariaDB service sudah running?
2. Username dan password sudah benar?
3. Database `db_kasir_minimarket` sudah dibuat?
4. Port 3306 tidak digunakan aplikasi lain?

---

## Penggunaan Aplikasi

### Q: Apakah nama pembeli wajib diisi?
**A:** Ya, nama pembeli wajib diisi sebelum menambahkan barang ke keranjang. Ini untuk identifikasi transaksi.

### Q: Bagaimana cara menambah barang baru?
**A:** Saat ini barang di-hardcode di array `dataBarang`. Untuk versi production, bisa menambahkan fitur CRUD untuk master barang yang terintegrasi dengan database.

### Q: Bisa menghapus item dari keranjang?
**A:** Ya, pilih item di tabel keranjang lalu klik tombol "Hapus Item".

### Q: Bagaimana jika uang pembayaran kurang?
**A:** Aplikasi akan menampilkan peringatan "Uang Kurang!" dan tidak bisa memproses transaksi.

### Q: Struk tidak bisa dicetak, kenapa?
**A:** Pastikan:
1. Printer sudah terinstall dan connected
2. Printer driver sudah terinstall
3. Klik "Proses Transaksi" terlebih dahulu sebelum cetak

### Q: Apakah data transaksi otomatis tersimpan?
**A:** Ya, setelah klik "Proses Transaksi", data otomatis tersimpan ke database.

---

## Database

### Q: Bagaimana cara backup database?
**A:** Gunakan mysqldump:
```bash
mysqldump -u root -p db_kasir_minimarket > backup.sql
```

### Q: Bagaimana cara restore database?
**A:** Import kembali file backup:
```bash
mysql -u root -p db_kasir_minimarket < backup.sql
```

### Q: Apakah data akan hilang jika restart aplikasi?
**A:** Tidak, semua data tersimpan di database MariaDB dan tidak akan hilang.

### Q: Bagaimana cara melihat history transaksi?
**A:** Saat ini belum ada fitur view history di GUI. Anda bisa query langsung ke database:
```sql
SELECT * FROM view_laporan_transaksi;
```

### Q: Apakah ada laporan penjualan?
**A:** Ada stored procedure untuk laporan harian:
```sql
CALL sp_laporan_harian('2026-02-14');
```

---

## Kustomisasi

### Q: Bagaimana cara mengubah nama toko di struk?
**A:** Edit method `generateStruk()` di FormKasir.java, ubah bagian:
```java
struk.append("       TOKO FALAH MINIMARKET\n");
```

### Q: Bisa menambah barang lebih banyak?
**A:** Ya, edit array `dataBarang` di FormKasir.java:
```java
private String[][] dataBarang = {
    {"Nama Barang", "Harga"},
    // tambahkan di sini
};
```

### Q: Bagaimana cara mengubah warna theme aplikasi?
**A:** Edit color di method `initComponents()`, contoh:
```java
btnTambah.setBackground(new Color(46, 204, 113)); // Hijau
```

### Q: Bisa mengubah format struk?
**A:** Ya, edit method `generateStruk()` sesuai keinginan Anda.

---

## Development

### Q: Apakah bisa dikembangkan menjadi multi-user?
**A:** Ya! Perlu ditambahkan:
1. Tabel users untuk login system
2. Role management (admin, kasir)
3. Session management
4. Authentication & authorization

### Q: Bagaimana cara menambah fitur laporan?
**A:** Buat form baru dengan:
1. JTable untuk menampilkan data
2. Query ke database untuk get data
3. Filter berdasarkan tanggal/periode
4. Export ke Excel/PDF (optional)

### Q: Apakah support barcode scanner?
**A:** Belum, tapi bisa dikembangkan dengan:
1. Library barcode reader (ZXing)
2. Event listener untuk scanner input
3. Auto-fill berdasarkan barcode

### Q: Bisa dijadikan aplikasi web?
**A:** Ya, dengan cara:
1. Rebuild backend menggunakan Spring Boot/Servlet
2. Frontend menggunakan React/Vue/Angular
3. REST API untuk komunikasi
4. Deploy ke server

### Q: Apakah open source?
**A:** Ya, project ini menggunakan MIT License. Anda bebas menggunakan, memodifikasi, dan mendistribusikan.

---

## Troubleshooting

### Q: "Table doesn't exist" error
**A:** Database belum diimport. Jalankan:
```bash
mysql -u root -p < database/db_kasir_minimarket.sql
```

### Q: GUI tidak muncul dengan benar
**A:** Pastikan menggunakan Java 8 atau lebih tinggi. Coba ubah Look and Feel:
```java
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
```

### Q: Aplikasi crash saat input angka besar
**A:** Pastikan input tidak melebihi kapasitas INT (2,147,483,647). Bisa ubah ke BIGINT jika perlu angka lebih besar.

### Q: Slow performance saat banyak transaksi
**A:** Optimasi database:
1. Tambah index yang tepat
2. Archiving data lama
3. Optimize query
4. Gunakan connection pooling

### Q: Format rupiah tidak muncul
**A:** Pastikan Locale sudah di-set dengan benar:
```java
NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
```

---

## Lisensi & Support

### Q: Apakah gratis?
**A:** Ya, 100% gratis dan open source dengan MIT License.

### Q: Bisa digunakan untuk komersial?
**A:** Ya, Anda bebas menggunakan untuk keperluan komersial.

### Q: Dimana saya bisa bertanya jika ada masalah?
**A:** Buat Issue di GitHub repository atau email ke developer.

### Q: Bagaimana cara berkontribusi?
**A:** Baca file `CONTRIBUTING.md` untuk panduan lengkap.

### Q: Apakah ada versi berbayar dengan fitur lebih lengkap?
**A:** Tidak, semua fitur gratis. Tapi Anda bisa request fitur baru via GitHub Issues.

---

## Tips & Tricks

### Q: Apa shortcut keyboard yang tersedia?
**A:** Saat ini belum ada shortcut khusus, tapi bisa ditambahkan dengan KeyListener.

### Q: Bagaimana cara mempercepat input?
**A:** Tips:
1. Gunakan Tab untuk berpindah antar field
2. Enter untuk submit form
3. Gunakan spinner quantity untuk cepat tambah/kurang

### Q: Best practice untuk backup data?
**A:** 
1. Backup harian otomatis
2. Simpan di lokasi terpisah
3. Test restore secara berkala
4. Keep 3 generasi backup (daily, weekly, monthly)

---

## Contact

Punya pertanyaan lain yang belum terjawab?

📧 Email: your.email@example.com  
🐙 GitHub: [@yourusername](https://github.com/yourusername)  
💬 Issues: [GitHub Issues](https://github.com/yourusername/kasir-minimarket/issues)

---

*FAQ ini akan terus diupdate berdasarkan pertanyaan yang masuk.*

Last Updated: February 14, 2026
Version: 1.0.0
