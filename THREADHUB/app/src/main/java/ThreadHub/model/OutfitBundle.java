package ThreadHub.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OutfitBundle implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String namaPaket;
    private String kategoriStyle;
    private String deskripsi;
    private String imagePath;
    private List<Produk> listProduk;

    public OutfitBundle(int id, String namaPaket, String kategoriStyle, String deskripsi, String imagePath) {
        this.id = id;
        this.namaPaket = namaPaket;
        this.kategoriStyle = kategoriStyle.toUpperCase();
        this.deskripsi = deskripsi;
        this.imagePath = imagePath;
        this.listProduk = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getNamaPaket() { return namaPaket; }
    public String getKategoriStyle() { return kategoriStyle; }
    public String getDeskripsi() { return deskripsi; }
    public String getImagePath() { return imagePath; } // Getter foto
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public List<Produk> getListProduk() { return listProduk; }

    public void setNamaPaket(String namaPaket) { this.namaPaket = namaPaket; }
    public void setKategoriStyle(String kategoriStyle) { this.kategoriStyle = kategoriStyle.toUpperCase(); }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public void tambahProdukKePaket(Produk p) {
        if (p != null) this.listProduk.add(p);
    }

    public double getTotalHarga() {
        return listProduk.stream().mapToDouble(Produk::getHarga).sum();
    }

    public String getTotalHargaFormatted() {
        return String.format("Rp %,.0f", getTotalHarga());
    }
}