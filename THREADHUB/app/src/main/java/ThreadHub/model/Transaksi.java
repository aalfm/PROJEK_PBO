package ThreadHub.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;

    private int               id;
    private Buyer             buyer;
    private List<ItemKeranjang> items;
    
    private double            subtotal;
    private double            diskon;
    private double            ppn;
    private double            pph;
    private double            totalHarga;
    
    private LocalDateTime     waktu;
    private String            status;

    public Transaksi(int id, Buyer buyer, List<ItemKeranjang> items, double subtotal, double diskon, double ppn, double pph, double totalHarga) {
        this.id         = id;
        this.buyer      = buyer;
        this.items      = List.copyOf(items);
        this.waktu      = LocalDateTime.now();
        this.status     = "BERHASIL";
        
        this.subtotal   = subtotal;
        this.diskon     = diskon;
        this.ppn        = ppn;
        this.pph        = pph;
        this.totalHarga = totalHarga;
    }

    public int                getId()         { return id; }
    public Buyer              getBuyer()      { return buyer; }
    public List<ItemKeranjang> getItems()      { return items; }
    
    public double             getSubtotal()   { return subtotal; }
    public double             getDiskon()     { return diskon; }
    public double             getPpn()        { return ppn; }
    public double             getPph()        { return pph; }
    public double             getTotalHarga() { return totalHarga; }
    
    public LocalDateTime      getWaktu()      { return waktu; }
    public String             getStatus()     { return status; }

    public String getTotalFormatted() {
        return String.format("Rp %,.0f", totalHarga);
    }

    public String getWaktuFormatted() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        return waktu.format(fmt);
    }

    public String getRingkasan() {
        return String.format("#TRX-%04d | %s | %s | %s",
                id, getWaktuFormatted(), getTotalFormatted(), status);
    }
}