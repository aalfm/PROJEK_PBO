package ThreadHub.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitialization {

    public static void initDatabase() {
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement()) {

            String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL," +
                    "nama TEXT NOT NULL)";
            stmt.execute(createUsers);

            String createProduk = "CREATE TABLE IF NOT EXISTS produk (" +
                    "id INTEGER PRIMARY KEY," +
                    "nama TEXT NOT NULL," +
                    "deskripsi TEXT," +
                    "kategori TEXT," +
                    "harga REAL," +
                    "stok INTEGER," +
                    "ukuran TEXT," +
                    "warna TEXT," +
                    "gender TEXT," +
                    "image_path TEXT)";
            stmt.execute(createProduk);

            String createTransaksi = "CREATE TABLE IF NOT EXISTS transaksi (" +
                    "id INTEGER PRIMARY KEY," +
                    "buyer_id INTEGER," +
                    "subtotal REAL," +
                    "diskon REAL," +
                    "ppn REAL," +
                    "pph REAL," +
                    "total_harga REAL," +
                    "waktu TEXT," +
                    "status TEXT," +
                    "FOREIGN KEY(buyer_id) REFERENCES users(id))";
            stmt.execute(createTransaksi);

            String createTransaksiItem = "CREATE TABLE IF NOT EXISTS transaksi_items (" +
                    "transaksi_id INTEGER," +
                    "produk_id INTEGER," +
                    "jumlah INTEGER," +
                    "FOREIGN KEY(transaksi_id) REFERENCES transaksi(id)," +
                    "FOREIGN KEY(produk_id) REFERENCES produk(id))";
            stmt.execute(createTransaksiItem);

            String createOutfit = "CREATE TABLE IF NOT EXISTS outfit (" +
                    "id INTEGER PRIMARY KEY," +
                    "nama_paket TEXT NOT NULL," +
                    "kategori_style TEXT," +
                    "deskripsi TEXT," +
                    "image_path TEXT)";
            stmt.execute(createOutfit);

            String createOutfitProduk = "CREATE TABLE IF NOT EXISTS outfit_produk (" +
                    "outfit_id INTEGER," +
                    "produk_id INTEGER," +
                    "FOREIGN KEY(outfit_id) REFERENCES outfit(id)," +
                    "FOREIGN KEY(produk_id) REFERENCES produk(id))";
            stmt.execute(createOutfitProduk);

            // Check if admin exists
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM users WHERE role='admin'");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO users (id, username, password, role, nama) VALUES (1, 'admin', 'admin123', 'admin', 'Administrator')");
            }

            // Check and seed Produk
            ResultSet rsProduk = stmt.executeQuery("SELECT count(*) FROM produk");
            if (rsProduk.next() && rsProduk.getInt(1) == 0) {
                stmt.execute("INSERT INTO produk (id, nama, deskripsi, kategori, harga, stok, ukuran, warna, gender) VALUES " +
                        "(1, 'Kaos Polos Premium', 'Kaos bahan cotton combed 30s', 'Kaos', 89000, 50, 'M', 'Putih', 'PRIA')," +
                        "(2, 'Kemeja Flanel Kotak', 'Kemeja casual', 'Kemeja', 175000, 30, 'L', 'Merah', 'PRIA')," +
                        "(3, 'Blouse Floral Elegan', 'Blouse cantik', 'Kemeja', 150000, 25, 'M', 'Navy', 'WANITA')," +
                        "(4, 'Rok Midi Plisket', 'Rok premium', 'Rok', 120000, 15, 'All Size', 'Hitam', 'WANITA')," +
                        "(5, 'Kaos Karakter Superhero', 'Kaos anak', 'Kaos', 65000, 20, 'S', 'Biru', 'ANAK-ANAK')," +
                        "(6, 'Polo Shirt Pique', 'Polo formal', 'Kaos Polo', 145000, 40, 'M', 'Dongker', 'PRIA')," +
                        "(7, 'Sepatu Sneakers Canvas', 'Sepatu kasual', 'Sepatu', 285000, 18, '40', 'Putih', 'WANITA')," +
                        "(8, 'Topi Baseball Anak', 'Topi pelindung', 'Topi', 45000, 22, 'All Size', 'Merah', 'ANAK-ANAK')");
            }

            // Check and seed Outfit
            ResultSet rsOutfit = stmt.executeQuery("SELECT count(*) FROM outfit");
            if (rsOutfit.next() && rsOutfit.getInt(1) == 0) {
                stmt.execute("INSERT INTO outfit (id, nama_paket, kategori_style, deskripsi) VALUES " +
                        "(1, 'Streetwear Starter Pack', 'STREETWEAR', 'Kombinasi kasual jalanan yang modis')," +
                        "(2, 'Summer Casual Chic', 'CASUAL', 'Gaya santai nan elegan untuk wanita')");
                
                stmt.execute("INSERT INTO outfit_produk (outfit_id, produk_id) VALUES " +
                        "(1, 1), (1, 2), (2, 3), (2, 7)");
            }

        } catch (SQLException e) {
            System.out.println("Gagal inisialisasi database: " + e.getMessage());
        }
    }
}
