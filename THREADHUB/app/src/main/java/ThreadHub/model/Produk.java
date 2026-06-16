package ThreadHub.model;

import java.io.Serializable;

public class Produk implements Serializable {
    
    private static final long serialVersionUID = 1L; // ID Versi penyimpanan
    
    private int id;
    private String nama;
    private String deskripsi;
    private String kategori;
    private double harga;
    private int stok;
    private String ukuran;   // S, M, L, XL, XXL
    private String warna;
    private String gender;   // Atribut gender (PRIA, WANITA, ANAK-ANAK)
    private String imagePath; // URL/Path file foto produk

    public Produk(int id, String nama, String deskripsi, String kategori,
                  double harga, int stok, String ukuran, String warna, String gender, String imagePath) {
        this.id        = id;
        this.nama      = nama;
        this.deskripsi = deskripsi;
        this.kategori  = kategori;
        this.harga     = harga;
        this.stok      = stok;
        this.ukuran    = ukuran;
        this.warna     = warna;
        this.gender    = gender;
        this.imagePath = imagePath;
    }

    public int    getId()        { return id; }
    public String getNama()      { return nama; }
    public String getDeskripsi() { return deskripsi; }
    public String getKategori()  { return kategori; }
    public double getHarga()     { return harga; }
    public int    getStok()      { return stok; }
    public String getUkuran()    { return ukuran; }
    public String getWarna()     { return warna; }
    public String getGender()    { return gender; }
    public String getImagePath() { return imagePath; }

    public void setNama(String nama)           { this.nama = nama; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setKategori(String kategori)   { this.kategori = kategori; }
    public void setHarga(double harga)         { this.harga = harga; }
    public void setStok(int stok)              { this.stok = stok; }
    public void setUkuran(String ukuran)       { this.ukuran = ukuran; }
    public void setWarna(String warna)         { this.warna = warna; }
    public void setGender(String gender)       { this.gender = gender; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isAvailable() {
        return stok > 0;
    }

    public String getHargaFormatted() {
        return String.format("Rp %,.0f", harga);
    }

    @Override
    public String toString() {
        return nama + " - " + getHargaFormatted();
    }
}