package ThreadHub;
import ThreadHub.controller.DataStore;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Initializing Database...");
        DataStore ds = DataStore.getInstance();
        System.out.println("Database initialized. Products count: " + ds.getAllProduk().size());
        System.exit(0);
    }
}
