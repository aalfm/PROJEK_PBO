package ThreadHub.model;

import java.io.Serializable;

public class Buyer extends User implements Serializable {

    public Buyer(int id, String username, String password, String nama) {
        super(id, username, password, nama, "buyer");
    }

    @Override
    public String getDashboardTitle() {
        return "ThreadHub — Selamat Belanja, " + getNama() + "!";
    }
}
