package ThreadHub.model;

public class Admin extends User {

    public Admin(int id, String username, String password, String nama) {
        super(id, username, password, nama, "admin");
    }

    @Override
    public String getDashboardTitle() {
        return "ThreadHub — Admin Panel";
    }
}