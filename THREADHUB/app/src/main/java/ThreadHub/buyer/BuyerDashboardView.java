package ThreadHub.buyer;

import ThreadHub.controller.*;
import ThreadHub.model.*;
import ThreadHub.view.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class BuyerDashboardView {

    private final Stage stage;
    private final Buyer buyer;
    private final KeranjangController keranjang;
    private BorderPane root;
    private Label cartBadge;
    private TextField searchBar; 
    
    private Label activeCategoryLabel;

    public BuyerDashboardView(Stage stage, Buyer buyer) {
        this.stage     = stage;
        this.buyer     = buyer;
        this.keranjang = new KeranjangController(buyer);
    }

    public void show() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;"); 

        root.setTop(buildZaloraHeader());
        showProdukView(); 

        if (stage.getScene() != null) {
            stage.getScene().setRoot(root);
        } else {
            Scene scene = new Scene(root, 1200, 720);
            stage.setScene(scene);
            stage.centerOnScreen();
        }

        stage.setTitle("ThreadHub — Belanja");
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }

    private VBox buildZaloraHeader() {
        VBox header = new VBox();
        header.setStyle(
            "-fx-background-color: " + StyleKit.CARD_BG + ";" + 
            "-fx-border-color: transparent transparent " + StyleKit.BORDER + " transparent;" +
            "-fx-border-width: 1;"
        );

        GridPane topRow = new GridPane();
        
        topRow.setPadding(new Insets(35, 50, 15, 50)); 

        ColumnConstraints colKiri = new ColumnConstraints();
        colKiri.setPercentWidth(30);
        colKiri.setHalignment(HPos.LEFT);

        ColumnConstraints colTengah = new ColumnConstraints();
        colTengah.setPercentWidth(40);
        colTengah.setHalignment(HPos.CENTER);

        ColumnConstraints colKanan = new ColumnConstraints();
        colKanan.setPercentWidth(30);
        colKanan.setHalignment(HPos.RIGHT);

        topRow.getColumnConstraints().addAll(colKiri, colTengah, colKanan);

        Label logo = new Label("THREADHUB");
        logo.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.EXTRA_BOLD, 26)); 
        logo.setTextFill(Color.web(StyleKit.ACCENT)); 
        logo.setTranslateY(2); 
        HBox leftBox = new HBox(logo);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        topRow.add(leftBox, 0, 0);

        searchBar = new TextField();
        searchBar.setPromptText("Cari produk atau kategori... (Tekan Enter)");
        searchBar.setPrefWidth(450); 
        searchBar.setStyle(
            "-fx-background-radius: 20; -fx-border-radius: 20; " +
            "-fx-padding: 8 15 8 15; -fx-border-color: #FFFFFF; " + 
            "-fx-background-color: transparent; -fx-text-fill: #FFFFFF; " + 
            "-fx-prompt-text-fill: #AAAAAA;" 
        );
        HBox centerBox = new HBox(searchBar);
        centerBox.setAlignment(Pos.CENTER); 
        topRow.add(centerBox, 1, 0);

        HBox userActions = new HBox(20);
        userActions.setAlignment(Pos.CENTER_RIGHT);

        Label greeting = new Label("Hi, " + buyer.getNama());
        greeting.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.SEMI_BOLD, 13));
        greeting.setTextFill(Color.WHITE); 
        
        Button btnRiwayat = createFlatButton("Riwayat");
        btnRiwayat.setOnAction(e -> {
            setActiveCategory(null); 
            showRiwayatView();
        });

        Button btnLogout = createFlatButton("Logout");
        btnLogout.setOnAction(e -> new LoginView(stage).show());

        StackPane cartIconContainer = buildCartIcon();

        userActions.getChildren().addAll(greeting, btnRiwayat, cartIconContainer, btnLogout);
        topRow.add(userActions, 2, 0);


        HBox categoryRow = new HBox(35);
        categoryRow.setAlignment(Pos.CENTER); 
        categoryRow.setPadding(new Insets(0, 0, 15, 0));

        String[] categories = {"PAKAIAN", "WANITA", "PRIA", "ANAK-ANAK", "✨ INSPIRASI OUTFIT"};
        
        for (String cat : categories) {
            Label catLabel = new Label(cat);
            catLabel.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 12));
            catLabel.setCursor(Cursor.HAND);
            
            if (cat.equals("PAKAIAN")) {
                catLabel.setStyle("-fx-text-fill: " + StyleKit.ACCENT + ";");
                activeCategoryLabel = catLabel;
            } else {
                catLabel.setStyle("-fx-text-fill: #FFFFFF;"); 
            }

            catLabel.setOnMouseEntered(e -> catLabel.setStyle("-fx-text-fill: " + StyleKit.ACCENT + ";"));
            
            catLabel.setOnMouseExited(e -> {
                if (activeCategoryLabel != catLabel) {
                    catLabel.setStyle("-fx-text-fill: #FFFFFF;");
                }
            }); 
            
            catLabel.setOnMouseClicked(e -> {
                setActiveCategory(catLabel); 
                searchBar.clear(); 
                
                if (cat.equals("✨ INSPIRASI OUTFIT")) {
                    showOutfitView();
                } else {
                    String targetGender = cat.equals("PAKAIAN") ? "SEMUA" : cat;
                    showProdukView(targetGender);
                }
            }); 
            
            if (cat.equals("PAKAIAN")) {
                logo.setOnMouseClicked(e -> {
                    setActiveCategory(catLabel); 
                    searchBar.clear();
                    showProdukView();
                });
                logo.setCursor(Cursor.HAND);
            }
            


            categoryRow.getChildren().add(catLabel);
        }

        header.getChildren().addAll(topRow, categoryRow);
        
        searchBar.setOnAction(e -> {
            String keyword = searchBar.getText().trim();
            if (activeCategoryLabel != null && activeCategoryLabel.getText().equals("✨ INSPIRASI OUTFIT")) {
                showOutfitView(keyword);
            } else {
                String targetGender = (activeCategoryLabel != null && !activeCategoryLabel.getText().equals("PAKAIAN")) 
                                      ? activeCategoryLabel.getText() : "SEMUA";
                showProdukView(targetGender, keyword);
            }
        });
        
        return header;
    }
    
    private void setActiveCategory(Label newActiveLabel) {
        if (activeCategoryLabel != null) {
            activeCategoryLabel.setStyle("-fx-text-fill: #FFFFFF;");
        }
        
        activeCategoryLabel = newActiveLabel;
        if (activeCategoryLabel != null) {
            activeCategoryLabel.setStyle("-fx-text-fill: " + StyleKit.ACCENT + ";");
        }
    }

    private StackPane buildCartIcon() {
        StackPane stack = new StackPane();
        stack.setAlignment(Pos.TOP_RIGHT);
        stack.setCursor(Cursor.HAND);

        Button btnKeranjang = new Button("🛒");
        btnKeranjang.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-padding: 0; -fx-text-fill: white;");
        btnKeranjang.setMouseTransparent(true); 
        
        cartBadge = new Label("0");
        cartBadge.setStyle(
            "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 10px; " +
            "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 1 5 1 5;"
        );
        cartBadge.setTranslateX(5); 
        cartBadge.setTranslateY(-5);
        cartBadge.setVisible(false);

        stack.getChildren().addAll(btnKeranjang, cartBadge);
        stack.setOnMouseClicked(e -> {
            setActiveCategory(null); 
            showIntegrasiKeranjang();
        });

        return stack;
    }

    private void showIntegrasiKeranjang() {
        showKeranjangView();
    }

    private Button createFlatButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: white;"); 
        btn.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.SEMI_BOLD, 13));
        btn.setOnMouseEntered(e -> btn.setUnderline(true));
        btn.setOnMouseExited(e -> btn.setUnderline(false));
        return btn;
    }

    public void showProdukView() {
        showProdukView("SEMUA", "");
    }

    public void showProdukView(String gender) {
        showProdukView(gender, "");
    }

    public void showProdukView(String gender, String keyword) {
        root.setCenter(new ProdukListView(this, buyer, keranjang, gender, keyword).build());
    }

    public void showKeranjangView() {
        root.setCenter(new KeranjangView(this, keranjang).build());
    }

    public void showRiwayatView() {
        root.setCenter(new RiwayatView(buyer).build());
    }

    public void showDetailView(Produk p) {
        root.setCenter(new ProdukDetailView(this, p, keranjang).build());
    }

    public void showOutfitView() {
        showOutfitView("");
    }

    public void showOutfitView(String keyword) {
        root.setCenter(new OutfitView(this, keranjang, keyword).build());
    }

    public void updateCartBadge() {
        int total = keranjang.getItems().stream()
                .mapToInt(ThreadHub.model.ItemKeranjang::getJumlah).sum();
        cartBadge.setText(String.valueOf(total));
        cartBadge.setVisible(total > 0);
    }
}