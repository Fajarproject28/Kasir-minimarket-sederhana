-- Database: db_kasir_minimarket
-- Author: Fajar Eka Diyan Permana
-- Description: Database untuk aplikasi kasir minimarket sederhana

-- Buat database
CREATE DATABASE IF NOT EXISTS db_kasir_minimarket;
USE db_kasir_minimarket;

-- Tabel Transaksi
CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    nama_pembeli VARCHAR(100) NOT NULL,
    total_belanja INT NOT NULL,
    jumlah_bayar INT NOT NULL,
    kembalian INT NOT NULL,
    tanggal DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_tanggal (tanggal),
    INDEX idx_nama_pembeli (nama_pembeli)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabel Detail Transaksi
CREATE TABLE IF NOT EXISTS detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    nama_barang VARCHAR(100) NOT NULL,
    harga INT NOT NULL,
    quantity INT NOT NULL,
    subtotal INT NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi) ON DELETE CASCADE,
    INDEX idx_id_transaksi (id_transaksi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabel Master Barang (opsional, untuk pengembangan)
CREATE TABLE IF NOT EXISTS master_barang (
    id_barang INT AUTO_INCREMENT PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    harga INT NOT NULL,
    stok INT DEFAULT 0,
    kategori VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nama_barang (nama_barang)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert data barang awal
INSERT INTO master_barang (nama_barang, harga, stok, kategori) VALUES
('Indomie Goreng', 3000, 100, 'Mie Instant'),
('Air Mineral', 3000, 200, 'Minuman'),
('Snack Chitato', 2000, 150, 'Snack'),
('Teh Botol', 4000, 180, 'Minuman'),
('Kopi Kapal Api', 2500, 120, 'Minuman');

-- View untuk laporan transaksi
CREATE OR REPLACE VIEW view_laporan_transaksi AS
SELECT 
    t.id_transaksi,
    t.nama_pembeli,
    t.total_belanja,
    t.jumlah_bayar,
    t.kembalian,
    t.tanggal,
    COUNT(dt.id_detail) as jumlah_item
FROM transaksi t
LEFT JOIN detail_transaksi dt ON t.id_transaksi = dt.id_transaksi
GROUP BY t.id_transaksi
ORDER BY t.tanggal DESC;

-- View untuk detail transaksi lengkap
CREATE OR REPLACE VIEW view_detail_transaksi_lengkap AS
SELECT 
    t.id_transaksi,
    t.nama_pembeli,
    t.tanggal,
    dt.nama_barang,
    dt.harga,
    dt.quantity,
    dt.subtotal,
    t.total_belanja,
    t.jumlah_bayar,
    t.kembalian
FROM transaksi t
INNER JOIN detail_transaksi dt ON t.id_transaksi = dt.id_transaksi
ORDER BY t.tanggal DESC, dt.id_detail;

-- Stored Procedure untuk laporan harian
DELIMITER //
CREATE PROCEDURE sp_laporan_harian(IN tanggal_laporan DATE)
BEGIN
    SELECT 
        COUNT(DISTINCT id_transaksi) as total_transaksi,
        SUM(total_belanja) as total_pendapatan,
        AVG(total_belanja) as rata_rata_belanja,
        MAX(total_belanja) as transaksi_terbesar,
        MIN(total_belanja) as transaksi_terkecil
    FROM transaksi
    WHERE DATE(tanggal) = tanggal_laporan;
    
    SELECT 
        nama_pembeli,
        total_belanja,
        tanggal
    FROM transaksi
    WHERE DATE(tanggal) = tanggal_laporan
    ORDER BY total_belanja DESC
    LIMIT 10;
END //
DELIMITER ;

-- Contoh pemanggilan stored procedure
-- CALL sp_laporan_harian('2026-02-14');

-- Grant privileges (sesuaikan dengan user yang digunakan)
-- GRANT ALL PRIVILEGES ON db_kasir_minimarket.* TO 'root'@'localhost';
-- FLUSH PRIVILEGES;
