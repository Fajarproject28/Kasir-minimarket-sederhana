

Aplikasi kasir minimarket sederhana berbasis Java Swing dengan database MariaDB. Aplikasi ini dibuat untuk memudahkan proses transaksi penjualan di minimarket dengan fitur pencatatan pembeli, pengelolaan keranjang belanja, dan pencetakan struk.

## 📋 Fitur Utama

- ✅ Input nama pembeli
- ✅ Pemilihan barang dari dropdown menu
- ✅ Automatic price update
- ✅ Quantity selector dengan spinner
- ✅ Keranjang belanja dengan tabel
- ✅ Perhitungan otomatis total, bayar, dan kembalian
- ✅ Generate struk pembelian
- ✅ Cetak struk langsung ke printer
- ✅ Simpan transaksi ke database MariaDB
- ✅ Auto-generated GUI (tanpa manual JLabel)
- ✅ Responsive design dengan JSplitPane

## 🛠️ Teknologi yang Digunakan

- **Java SE 8 atau lebih tinggi**
- **Java Swing** - untuk GUI
- **MariaDB/MySQL** - untuk database
- **JDBC** - untuk koneksi database
- **NetBeans IDE** - untuk development

## 📦 Struktur Project

```
kasir_minimarket/
├── src/
│   └── FormKasir.java          # Main application file
├── database/
│   └── db_kasir_minimarket.sql # Database schema
├── docs/
│   └── Buku_Panduan.docx       # User manual (Word)
├── lib/
│   └── mariadb-java-client.jar # MariaDB JDBC Driver
└── README.md
```

## 🚀 Cara Instalasi

### 1. Persiapan Database

1. Install MariaDB atau MySQL di komputer Anda
2. Jalankan MariaDB/MySQL server
3. Import database:
   ```sql
   mysql -u root -p < database/db_kasir_minimarket.sql
   ```
   Atau buka file `db_kasir_minimarket.sql` dan jalankan di phpMyAdmin/MySQL Workbench

### 2. Setup Project di NetBeans

1. Buka NetBeans IDE
2. File → New Project → Java → Java Application
3. Nama Project: `KasirMinimarket`
4. Copy file `FormKasir.java` ke folder `src/` project Anda
5. Download MariaDB JDBC Driver:
   - https://mariadb.com/downloads/connectors/connectors-data-access/java8-connector/
   - Atau gunakan Maven:
     ```xml
     <dependency>
         <groupId>org.mariadb.jdbc</groupId>
         <artifactId>mariadb-java-client</artifactId>
         <version>3.1.0</version>
     </dependency>
     ```

6. Tambahkan library MariaDB JDBC ke project:
   - Klik kanan project → Properties → Libraries
   - Add JAR/Folder → Pilih file `mariadb-java-client-x.x.x.jar`

### 3. Konfigurasi Database

Edit bagian koneksi database di `FormKasir.java`:

```java
private void koneksiDatabase() {
    try {
        Class.forName("org.mariadb.jdbc.Driver");
        conn = DriverManager.getConnection(
            "jdbc:mariadb://localhost:3306/db_kasir_minimarket",
            "root",        // username database Anda
            ""             // password database Anda
        );
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### 4. Jalankan Aplikasi

1. Set `FormKasir.java` sebagai Main Class
2. Klik Run Project (F6) atau tombol Run di toolbar
3. Aplikasi akan terbuka

## 💡 Cara Penggunaan

### 1. Melakukan Transaksi

1. **Input Nama Pembeli**: Masukkan nama pembeli di field "Nama Pembeli"
2. **Pilih Barang**: Pilih barang dari dropdown "Nama Barang"
3. **Set Quantity**: Atur jumlah barang dengan spinner
4. **Tambah ke Keranjang**: Klik tombol "Tambah ke Keranjang"
5. **Ulangi** untuk barang lainnya
6. **Input Jumlah Bayar**: Masukkan nominal uang yang dibayarkan
7. **Proses Transaksi**: Klik "Proses Transaksi" untuk generate struk
8. **Cetak Struk**: Klik "Cetak Struk" untuk print
9. **Transaksi Baru**: Klik "Transaksi Baru" untuk reset form

### 2. Menghapus Item

- Pilih baris item di tabel keranjang
- Klik tombol "Hapus Item"

### 3. Contoh Struk

```
========================================
       TOKO FALAH MINIMARKET
    Jl. Raya Pasarkemis No. 123
          Tangerang, Banten
         Telp: 021-1234567
========================================
Tanggal: 14/02/2026 15:30:45
Kasir  : Admin
Pembeli: Budi Santoso
========================================

DAFTAR BELANJA:
----------------------------------------
Indomie Goreng      
Rp3.000,00 x 2 = Rp6.000,00

Air Mineral         
Rp3.000,00 x 1 = Rp3.000,00

========================================
Total Belanja : Rp9.000,00
Jumlah Bayar  : Rp10.000,00
Kembalian     : Rp1.000,00
========================================
    Terima kasih atas kunjungan Anda!
        Selamat berbelanja kembali
========================================
```

## 📊 Database Schema

### Tabel `transaksi`
- `id_transaksi` (INT, PK, Auto Increment)
- `nama_pembeli` (VARCHAR)
- `total_belanja` (INT)
- `jumlah_bayar` (INT)
- `kembalian` (INT)
- `tanggal` (DATETIME)

### Tabel `detail_transaksi`
- `id_detail` (INT, PK, Auto Increment)
- `id_transaksi` (INT, FK)
- `nama_barang` (VARCHAR)
- `harga` (INT)
- `quantity` (INT)
- `subtotal` (INT)

### Tabel `master_barang` (opsional)
- `id_barang` (INT, PK, Auto Increment)
- `nama_barang` (VARCHAR)
- `harga` (INT)
- `stok` (INT)
- `kategori` (VARCHAR)
- `created_at` (DATETIME)
- `updated_at` (DATETIME)

## 🎨 Screenshot

*Screenshot aplikasi akan ditambahkan di sini*

## 📝 Daftar Barang Default

| Nama Barang | Harga |
|-------------|-------|
| Indomie Goreng | Rp 3.000 |
| Air Mineral | Rp 3.000 |
| Snack Chitato | Rp 2.000 |
| Teh Botol | Rp 4.000 |
| Kopi Kapal Api | Rp 2.500 |

## 🔧 Troubleshooting

### Problem: Database connection failed
**Solusi**: 
- Pastikan MariaDB/MySQL server sudah running
- Cek username dan password database
- Pastikan database `db_kasir_minimarket` sudah dibuat

### Problem: ClassNotFoundException: org.mariadb.jdbc.Driver
**Solusi**: 
- Pastikan MariaDB JDBC driver sudah ditambahkan ke project libraries
- Download driver dari https://mariadb.com/downloads/

### Problem: GUI tidak muncul
**Solusi**: 
- Pastikan menggunakan Java 8 atau lebih tinggi
- Cek apakah ada error di console NetBeans

## 🚀 Pengembangan Lebih Lanjut

Fitur yang bisa ditambahkan:
- [ ] Login system untuk kasir
- [ ] Laporan penjualan harian/bulanan
- [ ] Export laporan ke Excel/PDF
- [ ] Manajemen stok barang
- [ ] Barcode scanner integration
- [ ] Multi-user support
- [ ] Diskon dan promosi
- [ ] Member/loyalty program

## 📄 Lisensi

Project ini dibuat untuk tujuan pembelajaran. Anda bebas menggunakan, memodifikasi, dan mendistribusikan kode ini.

## 👨‍💻 Fajar Eka Diyan Permana

**Tammim Falah**
- GitHub: (https://github.com/Fajarproject28)
- Email: feka79647@gmail.com

## 🙏 Kontribusi

Kontribusi sangat diterima! Silakan fork repository ini dan submit pull request untuk improvement.

## 📚 Referensi

- [Java Swing Documentation](https://docs.oracle.com/javase/tutorial/uiswing/)
- [MariaDB Documentation](https://mariadb.com/kb/en/documentation/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)

---

⭐ Jika project ini membantu Anda, jangan lupa untuk memberikan star!
