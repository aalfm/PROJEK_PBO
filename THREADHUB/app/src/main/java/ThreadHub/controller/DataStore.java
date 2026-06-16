package ThreadHub.controller;

import ThreadHub.model.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;

    private DataStore() {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        initDatabase();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void initDatabase() {
        DBInitialization.initDatabase();
    }

    public User login(String username, String password) {
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if ("admin".equals(rs.getString("role"))) {
                    return new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("nama"));
                } else {
                    return new Buyer(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("nama"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal login: " + e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                if ("admin".equals(rs.getString("role"))) {
                    users.add(new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("nama")));
                } else {
                    users.add(new Buyer(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("nama")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil users: " + e.getMessage());
        }
        return users;
    }

    public void tambahUser(User u) {
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (id, username, password, role, nama) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setInt(1, u.getId());
            pstmt.setString(2, u.getUsername());
            pstmt.setString(3, u.getPassword());
            pstmt.setString(4, u.getRole());
            pstmt.setString(5, u.getNama());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal tambah user: " + e.getMessage());
        }
    }

    public void hapusUser(int id) {
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal hapus user: " + e.getMessage());
        }
    }

    public int generateUserId() {
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM users")) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
             System.out.println("Error: " + e.getMessage());
        }
        return 1;
    }

    public List<Produk> getAllProduk() {
        List<Produk> list = new ArrayList<>();
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produk")) {
            while (rs.next()) {
                Produk p = new Produk(rs.getInt("id"), rs.getString("nama"), rs.getString("deskripsi"), rs.getString("kategori"),
                        rs.getDouble("harga"), rs.getInt("stok"), rs.getString("ukuran"), rs.getString("warna"), rs.getString("gender"), rs.getString("image_path"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public Produk getProdukById(int id) {
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM produk WHERE id = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Produk(rs.getInt("id"), rs.getString("nama"), rs.getString("deskripsi"), rs.getString("kategori"),
                        rs.getDouble("harga"), rs.getInt("stok"), rs.getString("ukuran"), rs.getString("warna"), rs.getString("gender"), rs.getString("image_path"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<Produk> cariProduk(String keyword) {
        String kw = "%" + keyword.toLowerCase() + "%";
        List<Produk> list = new ArrayList<>();
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM produk WHERE lower(nama) LIKE ? OR lower(kategori) LIKE ? OR lower(warna) LIKE ? OR lower(gender) LIKE ?")) {
            pstmt.setString(1, kw);
            pstmt.setString(2, kw);
            pstmt.setString(3, kw);
            pstmt.setString(4, kw);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Produk(rs.getInt("id"), rs.getString("nama"), rs.getString("deskripsi"), rs.getString("kategori"),
                        rs.getDouble("harga"), rs.getInt("stok"), rs.getString("ukuran"), rs.getString("warna"), rs.getString("gender"), rs.getString("image_path")));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public void tambahProduk(Produk p) {
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO produk (id, nama, deskripsi, kategori, harga, stok, ukuran, warna, gender, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setInt(1, p.getId());
            pstmt.setString(2, p.getNama());
            pstmt.setString(3, p.getDeskripsi());
            pstmt.setString(4, p.getKategori());
            pstmt.setDouble(5, p.getHarga());
            pstmt.setInt(6, p.getStok());
            pstmt.setString(7, p.getUkuran());
            pstmt.setString(8, p.getWarna());
            pstmt.setString(9, p.getGender());
            pstmt.setString(10, p.getImagePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateProduk(Produk p) {
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE produk SET nama=?, deskripsi=?, kategori=?, harga=?, stok=?, ukuran=?, warna=?, gender=?, image_path=? WHERE id=?")) {
            pstmt.setString(1, p.getNama());
            pstmt.setString(2, p.getDeskripsi());
            pstmt.setString(3, p.getKategori());
            pstmt.setDouble(4, p.getHarga());
            pstmt.setInt(5, p.getStok());
            pstmt.setString(6, p.getUkuran());
            pstmt.setString(7, p.getWarna());
            pstmt.setString(8, p.getGender());
            pstmt.setString(9, p.getImagePath());
            pstmt.setInt(10, p.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void hapusProduk(int id) {
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM produk WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public int generateProdukId() {
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM produk")) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
             System.out.println("Error: " + e.getMessage());
        }
        return 1;
    }

    public void tambahTransaksi(Transaksi t) {
        try (Connection conn = DBConnect.connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO transaksi (id, buyer_id, subtotal, diskon, ppn, pph, total_harga, waktu, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                pstmt.setInt(1, t.getId());
                pstmt.setInt(2, t.getBuyer().getId());
                pstmt.setDouble(3, t.getSubtotal());
                pstmt.setDouble(4, t.getDiskon());
                pstmt.setDouble(5, t.getPpn());
                pstmt.setDouble(6, t.getPph());
                pstmt.setDouble(7, t.getTotalHarga());
                pstmt.setString(8, t.getWaktu().toString());
                pstmt.setString(9, t.getStatus());
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO transaksi_items (transaksi_id, produk_id, jumlah) VALUES (?, ?, ?)")) {
                for (ItemKeranjang item : t.getItems()) {
                    pstmt.setInt(1, t.getId());
                    pstmt.setInt(2, item.getProduk().getId());
                    pstmt.setInt(3, item.getJumlah());
                    pstmt.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public int generateTransaksiId() {
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM transaksi")) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
             System.out.println("Error: " + e.getMessage());
        }
        return 1;
    }

    public List<Transaksi> getAllTransaksi() {
        List<Transaksi> list = new ArrayList<>();
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM transaksi")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int buyerId = rs.getInt("buyer_id");
                Buyer buyer = (Buyer) getAllUsers().stream().filter(u -> u.getId() == buyerId).findFirst().orElse(null);
                
                List<ItemKeranjang> items = new ArrayList<>();
                try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM transaksi_items WHERE transaksi_id=?")) {
                    pstmt.setInt(1, id);
                    ResultSet rsItems = pstmt.executeQuery();
                    while (rsItems.next()) {
                        Produk p = getProdukById(rsItems.getInt("produk_id"));
                        if (p != null) {
                            items.add(new ItemKeranjang(p, rsItems.getInt("jumlah")));
                        }
                    }
                }
                
                Transaksi t = new Transaksi(id, buyer, items, rs.getDouble("subtotal"), rs.getDouble("diskon"), rs.getDouble("ppn"), rs.getDouble("pph"), rs.getDouble("total_harga"));
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public List<Transaksi> getTransaksiBuyer(Buyer buyer) {
        return getAllTransaksi().stream().filter(t -> t.getBuyer().getId() == buyer.getId()).toList();
    }

    public void tambahOutfit(OutfitBundle ob) {
        try (Connection conn = DBConnect.connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO outfit (id, nama_paket, kategori_style, deskripsi, image_path) VALUES (?, ?, ?, ?, ?)")) {
                pstmt.setInt(1, ob.getId());
                pstmt.setString(2, ob.getNamaPaket());
                pstmt.setString(3, ob.getKategoriStyle());
                pstmt.setString(4, ob.getDeskripsi());
                pstmt.setString(5, ob.getImagePath());
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO outfit_produk (outfit_id, produk_id) VALUES (?, ?)")) {
                for (Produk p : ob.getListProduk()) {
                    pstmt.setInt(1, ob.getId());
                    pstmt.setInt(2, p.getId());
                    pstmt.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateOutfit(OutfitBundle ob) {
        try (Connection conn = DBConnect.connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE outfit SET nama_paket=?, kategori_style=?, deskripsi=?, image_path=? WHERE id=?")) {
                pstmt.setString(1, ob.getNamaPaket());
                pstmt.setString(2, ob.getKategoriStyle());
                pstmt.setString(3, ob.getDeskripsi());
                pstmt.setString(4, ob.getImagePath());
                pstmt.setInt(5, ob.getId());
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM outfit_produk WHERE outfit_id=?")) {
                pstmt.setInt(1, ob.getId());
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO outfit_produk (outfit_id, produk_id) VALUES (?, ?)")) {
                for (Produk p : ob.getListProduk()) {
                    pstmt.setInt(1, ob.getId());
                    pstmt.setInt(2, p.getId());
                    pstmt.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void hapusOutfit(int id) {
        try (Connection conn = DBConnect.connect()) {
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM outfit_produk WHERE outfit_id=?")) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM outfit WHERE id=?")) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<OutfitBundle> getAllOutfit() {
        List<OutfitBundle> list = new ArrayList<>();
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM outfit")) {
            while (rs.next()) {
                OutfitBundle ob = new OutfitBundle(rs.getInt("id"), rs.getString("nama_paket"), rs.getString("kategori_style"), rs.getString("deskripsi"), rs.getString("image_path"));
                try (PreparedStatement pstmt = conn.prepareStatement("SELECT produk_id FROM outfit_produk WHERE outfit_id=?")) {
                    pstmt.setInt(1, rs.getInt("id"));
                    ResultSet rsItems = pstmt.executeQuery();
                    while (rsItems.next()) {
                        Produk p = getProdukById(rsItems.getInt("produk_id"));
                        if (p != null) ob.tambahProdukKePaket(p);
                    }
                }
                list.add(ob);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public List<OutfitBundle> getOutfitByStyle(String style) {
        return getAllOutfit().stream().filter(o -> o.getKategoriStyle().equalsIgnoreCase(style)).toList();
    }
    
    // Stub for backwards compatibility with parts of code that might still call these
    public void simpanDataProduk() {}
    public void simpanDataOutfit() {}
    public void simpanDataTransaksi() {}
    public void simpanDataUser() {}
    
    public int generateOutfitId() {
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM outfit")) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
             System.out.println("Error: " + e.getMessage());
        }
        return 1;
    }
}
