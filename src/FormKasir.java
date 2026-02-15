package kasirminimarket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormKasir extends JFrame {
    // Komponen GUI
    private JTextField txtNamaPembeli;
    private JComboBox<String> cmbNamaBarang;
    private JTextField txtHarga;
    private JSpinner spnQuantity;
    private JTextField txtSubtotal;
    private JTable tblKeranjang;
    private DefaultTableModel modelTabel;
    private JTextArea txtAreaStruk;
    private JTextField txtTotalBelanja;
    private JTextField txtJumlahBayar;
    private JTextField txtKembalian;
    private JButton btnTambah, btnHapus, btnProses, btnCetak, btnReset;
    
    // Data barang
    private String[][] dataBarang = {
        {"Indomie Goreng", "3000"},
        {"Air Mineral", "3000"},
        {"Snack Chitato", "2000"},
        {"Teh Botol", "4000"},
        {"Kopi Kapal Api", "2500"}
    };
    
    // Format mata uang
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    
    // Koneksi database
    private Connection conn;
    
    public FormKasir() {
        initComponents();
        koneksiDatabase();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Aplikasi Kasir Minimarket - Toko Falah");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Panel Utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel lblJudul = new JLabel("KASIR MINIMARKET TOKO BERKAH");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 24));
        lblJudul.setForeground(Color.WHITE);
        headerPanel.add(lblJudul);
        
        // Panel Input (Kiri)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Data Pembelian"));
        
        // Nama Pembeli
        JPanel panelNama = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNama.add(new JLabel("Nama Pembeli:"));
        txtNamaPembeli = new JTextField(20);
        panelNama.add(txtNamaPembeli);
        inputPanel.add(panelNama);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Nama Barang
        JPanel panelBarang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBarang.add(new JLabel("Nama Barang:"));
        cmbNamaBarang = new JComboBox<>();
        for (String[] barang : dataBarang) {
            cmbNamaBarang.addItem(barang[0]);
        }
        cmbNamaBarang.addActionListener(e -> updateHarga());
        panelBarang.add(cmbNamaBarang);
        inputPanel.add(panelBarang);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Harga
        JPanel panelHarga = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHarga.add(new JLabel("Harga:"));
        txtHarga = new JTextField(15);
        txtHarga.setEditable(false);
        txtHarga.setBackground(Color.LIGHT_GRAY);
        panelHarga.add(txtHarga);
        inputPanel.add(panelHarga);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Quantity
        JPanel panelQty = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelQty.add(new JLabel("Quantity:"));
        spnQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spnQuantity.addChangeListener(e -> hitungSubtotal());
        panelQty.add(spnQuantity);
        inputPanel.add(panelQty);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Subtotal
        JPanel panelSubtotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSubtotal.add(new JLabel("Subtotal:"));
        txtSubtotal = new JTextField(15);
        txtSubtotal.setEditable(false);
        txtSubtotal.setBackground(Color.LIGHT_GRAY);
        panelSubtotal.add(txtSubtotal);
        inputPanel.add(panelSubtotal);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Tombol Tambah dan Hapus
        JPanel panelTombol1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnTambah = new JButton("Tambah ke Keranjang");
        btnTambah.setBackground(new Color(46, 204, 113));
        btnTambah.setForeground(Color.BLACK);
        btnTambah.addActionListener(e -> tambahKeKeranjang());
        
        btnHapus = new JButton("Hapus Item");
        btnHapus.setBackground(new Color(231, 76, 60));
        btnHapus.setForeground(Color.BLACK);
        btnHapus.addActionListener(e -> hapusItem());
        
        panelTombol1.add(btnTambah);
        panelTombol1.add(btnHapus);
        inputPanel.add(panelTombol1);
        
        // Panel Tabel Keranjang (Tengah)
        JPanel keranjangPanel = new JPanel(new BorderLayout());
        keranjangPanel.setBorder(BorderFactory.createTitledBorder("Keranjang Belanja"));
        
        String[] kolomTabel = {"No", "Nama Barang", "Harga", "Qty", "Subtotal"};
        modelTabel = new DefaultTableModel(kolomTabel, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblKeranjang = new JTable(modelTabel);
        JScrollPane scrollTabel = new JScrollPane(tblKeranjang);
        keranjangPanel.add(scrollTabel, BorderLayout.CENTER);
        
        // Panel Total dan Pembayaran
        JPanel pembayaranPanel = new JPanel();
        pembayaranPanel.setLayout(new BoxLayout(pembayaranPanel, BoxLayout.Y_AXIS));
        pembayaranPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTotal.add(new JLabel("Total Belanja:"));
        txtTotalBelanja = new JTextField(15);
        txtTotalBelanja.setEditable(false);
        txtTotalBelanja.setBackground(Color.YELLOW);
        txtTotalBelanja.setFont(new Font("Arial", Font.BOLD, 14));
        panelTotal.add(txtTotalBelanja);
        pembayaranPanel.add(panelTotal);
        
        pembayaranPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panelBayar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBayar.add(new JLabel("Jumlah Bayar:"));
        txtJumlahBayar = new JTextField(15);
        txtJumlahBayar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                hitungKembalian();
            }
        });
        panelBayar.add(txtJumlahBayar);
        pembayaranPanel.add(panelBayar);
        
        pembayaranPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panelKembalian = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelKembalian.add(new JLabel("Kembalian:"));
        txtKembalian = new JTextField(15);
        txtKembalian.setEditable(false);
        txtKembalian.setBackground(Color.LIGHT_GRAY);
        txtKembalian.setFont(new Font("Arial", Font.BOLD, 14));
        panelKembalian.add(txtKembalian);
        pembayaranPanel.add(panelKembalian);
        
        keranjangPanel.add(pembayaranPanel, BorderLayout.SOUTH);
        
        // Panel Struk (Kanan)
        JPanel strukPanel = new JPanel(new BorderLayout());
        strukPanel.setBorder(BorderFactory.createTitledBorder("Struk Pembelian"));
        
        txtAreaStruk = new JTextArea(20, 30);
        txtAreaStruk.setEditable(false);
        txtAreaStruk.setFont(new Font("Courier New", Font.PLAIN, 11));
        JScrollPane scrollStruk = new JScrollPane(txtAreaStruk);
        strukPanel.add(scrollStruk, BorderLayout.CENTER);
        
        // Tombol Aksi Struk
        JPanel panelTombol2 = new JPanel(new FlowLayout());
        btnProses = new JButton("Proses Transaksi");
        btnProses.setBackground(new Color(52, 152, 219));
        btnProses.setForeground(Color.BLACK);
        btnProses.addActionListener(e -> prosesTransaksi());
        
        btnCetak = new JButton("Cetak Struk");
        btnCetak.setBackground(new Color(142, 68, 173));
        btnCetak.setForeground(Color.BLACK);
        btnCetak.setEnabled(false);
        btnCetak.addActionListener(e -> cetakStruk());
        
        btnReset = new JButton("Transaksi Baru");
        btnReset.setBackground(new Color(243, 156, 18));
        btnReset.setForeground(Color.BLACK);
        btnReset.addActionListener(e -> resetForm());
        
        panelTombol2.add(btnProses);
        panelTombol2.add(btnCetak);
        panelTombol2.add(btnReset);
        strukPanel.add(panelTombol2, BorderLayout.SOUTH);
        
        // Layout utama
        JSplitPane splitKiri = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, keranjangPanel);
        splitKiri.setDividerLocation(350);
        
        JSplitPane splitUtama = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitKiri, strukPanel);
        splitUtama.setDividerLocation(650);
        
        mainPanel.add(splitUtama, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Set harga awal
        updateHarga();
    }
    
    private void koneksiDatabase() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/db_kasir_minimarket",
                "root",
                ""
            );
            System.out.println("Koneksi database berhasil!");
        } catch (Exception e) {
            System.out.println("Koneksi database gagal: " + e.getMessage());
        }
    }
    
    private void updateHarga() {
        int index = cmbNamaBarang.getSelectedIndex();
        if (index >= 0) {
            String harga = dataBarang[index][1];
            txtHarga.setText("Rp " + harga);
            hitungSubtotal();
        }
    }
    
    private void hitungSubtotal() {
        try {
            int index = cmbNamaBarang.getSelectedIndex();
            if (index >= 0) {
                int harga = Integer.parseInt(dataBarang[index][1]);
                int qty = (Integer) spnQuantity.getValue();
                int subtotal = harga * qty;
                txtSubtotal.setText(formatRupiah.format(subtotal));
            }
        } catch (Exception e) {
            txtSubtotal.setText("Rp 0");
        }
    }
    
    private void tambahKeKeranjang() {
        if (txtNamaPembeli.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pembeli harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int index = cmbNamaBarang.getSelectedIndex();
        String namaBarang = dataBarang[index][0];
        int harga = Integer.parseInt(dataBarang[index][1]);
        int qty = (Integer) spnQuantity.getValue();
        int subtotal = harga * qty;
        
        int no = modelTabel.getRowCount() + 1;
        modelTabel.addRow(new Object[]{
            no,
            namaBarang,
            formatRupiah.format(harga),
            qty,
            formatRupiah.format(subtotal)
        });
        
        hitungTotal();
        spnQuantity.setValue(1);
    }
    
    private void hapusItem() {
        int selectedRow = tblKeranjang.getSelectedRow();
        if (selectedRow >= 0) {
            modelTabel.removeRow(selectedRow);
            // Update nomor urut
            for (int i = 0; i < modelTabel.getRowCount(); i++) {
                modelTabel.setValueAt(i + 1, i, 0);
            }
            hitungTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih item yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void hitungTotal() {
        int total = 0;
        for (int i = 0; i < modelTabel.getRowCount(); i++) {
            String subtotalStr = modelTabel.getValueAt(i, 4).toString();
            subtotalStr = subtotalStr.replace("Rp", "").replace(".", "").replace(",00", "").trim();
            total += Integer.parseInt(subtotalStr);
        }
        txtTotalBelanja.setText(formatRupiah.format(total));
        hitungKembalian();
    }
    
    private void hitungKembalian() {
        try {
            String totalStr = txtTotalBelanja.getText().replace("Rp", "").replace(".", "").replace(",00", "").trim();
            String bayarStr = txtJumlahBayar.getText().trim();
            
            if (!totalStr.isEmpty() && !bayarStr.isEmpty()) {
                int total = Integer.parseInt(totalStr);
                int bayar = Integer.parseInt(bayarStr);
                int kembalian = bayar - total;
                
                if (kembalian >= 0) {
                    txtKembalian.setText(formatRupiah.format(kembalian));
                    txtKembalian.setForeground(Color.BLACK);
                } else {
                    txtKembalian.setText("Uang Kurang!");
                    txtKembalian.setForeground(Color.RED);
                }
            }
        } catch (NumberFormatException e) {
            txtKembalian.setText("Rp 0");
        }
    }
    
    private void prosesTransaksi() {
        if (modelTabel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (txtJumlahBayar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah bayar harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String kembalianStr = txtKembalian.getText();
        if (kembalianStr.equals("Uang Kurang!")) {
            JOptionPane.showMessageDialog(this, "Uang pembayaran kurang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Generate struk
        generateStruk();
        
        // Simpan ke database
        simpanTransaksi();
        
        btnCetak.setEnabled(true);
        JOptionPane.showMessageDialog(this, "Transaksi berhasil diproses!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void generateStruk() {
        StringBuilder struk = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String tanggal = sdf.format(new Date());
        
        struk.append("========================================\n");
        struk.append("       TOKO MINIMARKET BERKAH\n");
        struk.append("    Jl. Raya Pasarkemis No. 123\n");
        struk.append("          Tangerang, Banten\n");
        struk.append("         Telp: 021-1234567\n");
        struk.append("========================================\n");
        struk.append("Tanggal: ").append(tanggal).append("\n");
        struk.append("Kasir  : Admin\n");
        struk.append("Pembeli: ").append(txtNamaPembeli.getText()).append("\n");
        struk.append("========================================\n\n");
        
        struk.append("DAFTAR BELANJA:\n");
        struk.append("----------------------------------------\n");
        
        for (int i = 0; i < modelTabel.getRowCount(); i++) {
            String nama = modelTabel.getValueAt(i, 1).toString();
            String harga = modelTabel.getValueAt(i, 2).toString();
            String qty = modelTabel.getValueAt(i, 3).toString();
            String subtotal = modelTabel.getValueAt(i, 4).toString();
            
            struk.append(String.format("%-20s\n", nama));
            struk.append(String.format("%s x %s = %s\n\n", harga, qty, subtotal));
        }
        
        struk.append("========================================\n");
        struk.append(String.format("Total Belanja : %s\n", txtTotalBelanja.getText()));
        struk.append(String.format("Jumlah Bayar  : %s\n", formatRupiah.format(Integer.parseInt(txtJumlahBayar.getText()))));
        struk.append(String.format("Kembalian     : %s\n", txtKembalian.getText()));
        struk.append("========================================\n");
        struk.append("    Terima kasih atas kunjungan Anda!\n");
        struk.append("        Selamat berbelanja kembali\n");
        struk.append("========================================\n");
        
        txtAreaStruk.setText(struk.toString());
    }
    
    private void simpanTransaksi() {
        try {
            if (conn != null) {
                String totalStr = txtTotalBelanja.getText().replace("Rp", "").replace(".", "").replace(",00", "").trim();
                int total = Integer.parseInt(totalStr);
                int bayar = Integer.parseInt(txtJumlahBayar.getText());
                String kembalianStr = txtKembalian.getText().replace("Rp", "").replace(".", "").replace(",00", "").trim();
                int kembalian = Integer.parseInt(kembalianStr);
                
                // Simpan transaksi
                String sqlTransaksi = "INSERT INTO transaksi (nama_pembeli, total_belanja, jumlah_bayar, kembalian, tanggal) VALUES (?, ?, ?, ?, NOW())";
                PreparedStatement psTransaksi = conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);
                psTransaksi.setString(1, txtNamaPembeli.getText());
                psTransaksi.setInt(2, total);
                psTransaksi.setInt(3, bayar);
                psTransaksi.setInt(4, kembalian);
                psTransaksi.executeUpdate();
                
                // Dapatkan ID transaksi
                ResultSet rs = psTransaksi.getGeneratedKeys();
                int idTransaksi = 0;
                if (rs.next()) {
                    idTransaksi = rs.getInt(1);
                }
                
                // Simpan detail transaksi
                String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, nama_barang, harga, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                
                for (int i = 0; i < modelTabel.getRowCount(); i++) {
                    String namaBarang = modelTabel.getValueAt(i, 1).toString();
                    String hargaStr = modelTabel.getValueAt(i, 2).toString().replace("Rp", "").replace(".", "").replace(",00", "").trim();
                    int harga = Integer.parseInt(hargaStr);
                    int qty = Integer.parseInt(modelTabel.getValueAt(i, 3).toString());
                    String subtotalStr = modelTabel.getValueAt(i, 4).toString().replace("Rp", "").replace(".", "").replace(",00", "").trim();
                    int subtotal = Integer.parseInt(subtotalStr);
                    
                    psDetail.setInt(1, idTransaksi);
                    psDetail.setString(2, namaBarang);
                    psDetail.setInt(3, harga);
                    psDetail.setInt(4, qty);
                    psDetail.setInt(5, subtotal);
                    psDetail.executeUpdate();
                }
                
                System.out.println("Transaksi berhasil disimpan ke database!");
            }
        } catch (Exception e) {
            System.out.println("Error menyimpan transaksi: " + e.getMessage());
        }
    }
    
    private void cetakStruk() {
        try {
            boolean complete = txtAreaStruk.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, "Struk berhasil dicetak!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak struk: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void resetForm() {
        txtNamaPembeli.setText("");
        txtJumlahBayar.setText("");
        txtKembalian.setText("");
        txtTotalBelanja.setText("");
        txtAreaStruk.setText("");
        modelTabel.setRowCount(0);
        spnQuantity.setValue(1);
        btnCetak.setEnabled(false);
        cmbNamaBarang.setSelectedIndex(0);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new FormKasir().setVisible(true);
        });
    }
}
