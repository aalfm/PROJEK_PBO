package ThreadHub.model;

import java.io.Serializable;

public class ItemKeranjang implements Serializable {
    private Produk produk;
    private int    jumlah;

    public ItemKeranjang(Produk produk, int jumlah) {
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public Produk getProduk()  { return produk; }
    public int    getJumlah()  { return jumlah; }

    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getSubtotal() {
        return produk.getHarga() * jumlah;
    }

    public String getSubtotalFormatted() {
        return String.format("Rp %,.0f", getSubtotal());
    }
}
