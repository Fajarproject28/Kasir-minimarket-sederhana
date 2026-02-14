# Quick Start Guide - Aplikasi Kasir Minimarket

Panduan cepat untuk memulai menggunakan aplikasi dalam 5 menit!

## Prerequisites

✅ Java JDK 8+  
✅ MariaDB/MySQL  
✅ NetBeans IDE  

## Langkah 1: Clone Repository

```bash
git clone https://github.com/yourusername/kasir-minimarket.git
cd kasir-minimarket
```

## Langkah 2: Setup Database

### Windows:
```bash
install.bat
```

### Linux/Mac:
```bash
chmod +x install.sh
./install.sh
```

**Atau import manual:**
1. Buka phpMyAdmin
2. Import file `database/db_kasir_minimarket.sql`

## Langkah 3: Setup NetBeans Project

1. Buka NetBeans
2. File → New Project → Java Application
3. Project Name: `KasirMinimarket`
4. Copy file `src/FormKasir.java` ke folder `src/` project

## Langkah 4: Tambah Library

1. Download MariaDB JDBC Driver:
   - https://downloads.mariadb.com/Connectors/java/
   
2. Di NetBeans:
   - Klik kanan project → Properties
   - Libraries → Add JAR/Folder
   - Pilih `mariadb-java-client-x.x.x.jar`

## Langkah 5: Konfigurasi Database

Edit `FormKasir.java` line ~252:

```java
conn = DriverManager.getConnection(
    "jdbc:mariadb://localhost:3306/db_kasir_minimarket",
    "root",      // username Anda
    ""           // password Anda
);
```

## Langkah 6: Run!

1. Set Main Class: `kasirminimarket.FormKasir`
2. Klik Run (F6)
3. Done! ✨

## Troubleshooting

### Database connection error
- Pastikan MariaDB sudah running
- Cek username/password di kode
- Pastikan database sudah diimport

### ClassNotFoundException
- Pastikan JDBC driver sudah ditambahkan di Libraries

### GUI tidak muncul
- Pastikan menggunakan Java 8+
- Cek console untuk error messages

## Demo Data

Default barang yang tersedia:
- Indomie Goreng - Rp 3.000
- Air Mineral - Rp 3.000
- Snack Chitato - Rp 2.000
- Teh Botol - Rp 4.000
- Kopi Kapal Api - Rp 2.500

## Dokumentasi Lengkap

📖 Baca: `Buku_Panduan_Aplikasi_Kasir_Minimarket.docx`  
💾 Database: `docs/DATABASE_API.md`  
🤝 Kontribusi: `CONTRIBUTING.md`

## Support

Ada masalah? [Buat Issue](https://github.com/yourusername/kasir-minimarket/issues)

---

Happy Coding! 🚀
