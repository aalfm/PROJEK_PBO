package ThreadHub.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private int    id;
    private String username;
    private String password;
    private String nama;
    private String role;

    public User(int id, String username, String password, String nama, String role) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.nama     = nama;
        this.role     = role;
    }

    public int    getId()       { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNama()     { return nama; }
    public String getRole()     { return role; }

    public void setNama(String nama)         { this.nama = nama; }
    public void setPassword(String password) { this.password = password; }

    public boolean cekPassword(String input) {
        return this.password.equals(input);
    }

    public abstract String getDashboardTitle();
}
