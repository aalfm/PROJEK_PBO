package ThreadHub.buyer;

import ThreadHub.controller.DataStore;
import ThreadHub.controller.KeranjangController;
import ThreadHub.model.*;
import ThreadHub.view.StyleKit;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class OutfitView {

    private final BuyerDashboardView dashboard;
    private final KeranjangController keranjang;
    private final DataStore ds = DataStore.getInstance();
    private final String keyword;
    private FlowPane cardsContainer;
    
    private final List<Button> filterButtons = new ArrayList<>();

    public OutfitView(BuyerDashboardView dashboard, KeranjangController keranjang, String keyword) {
        this.dashboard = dashboard;
        this.keranjang = keranjang;
        this.keyword = keyword;
    }

    public VBox build() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #F8F9FA;");

        Label title = new Label("✨ Inspirasi Outfit");
        title.setStyle("-fx-text-fill: #222222; -fx-font-weight: bold; -fx-font-size: 24px;");

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        String[] styles = {"SEMUA", "STREETWEAR", "CASUAL", "FORMAL"};
        
        for (String style : styles) {
            Button btnFilter = new Button(style);
            
            String defaultStyle = "-fx-background-color: white; -fx-border-color: #DDDDDD; " +
                                  "-fx-border-radius: 20; -fx-background-radius: 20; " +
                                  "-fx-text-fill: #555555; -fx-font-weight: bold; -fx-cursor: hand;";
                                  
            String activeStyle = "-fx-background-color: " + StyleKit.ACCENT + "; -fx-border-color: " + StyleKit.ACCENT + "; " +
                                 "-fx-border-radius: 20; -fx-background-radius: 20; " +
                                 "-fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";

            btnFilter.setStyle(defaultStyle);
            
            if (style.equals("SEMUA")) {
                btnFilter.setStyle(activeStyle);
                btnFilter.setUserData("ACTIVE");
            } else {
                btnFilter.setUserData("INACTIVE");
            }
            
            btnFilter.setOnMouseEntered(e -> {
                if (btnFilter.getUserData().equals("INACTIVE")) {
                    btnFilter.setStyle(
                        "-fx-background-color: #F0F0F0; -fx-border-color: #CCCCCC; " +
                        "-fx-border-radius: 20; -fx-background-radius: 20; " +
                        "-fx-text-fill: #333333; -fx-font-weight: bold; -fx-cursor: hand;"
                    );
                }
            });

            btnFilter.setOnMouseExited(e -> {
                if (btnFilter.getUserData().equals("INACTIVE")) {
                    btnFilter.setStyle(defaultStyle);
                }
            });

            btnFilter.setOnAction(e -> {
                for (Button btn : filterButtons) {
                    btn.setStyle(defaultStyle);
                    btn.setUserData("INACTIVE");
                }
                
                btnFilter.setStyle(activeStyle);
                btnFilter.setUserData("ACTIVE");
                
                muatDaftarOutfit(style);
            });
            
            filterButtons.add(btnFilter);
            filterBox.getChildren().add(btnFilter);
        }

        cardsContainer = new FlowPane(Orientation.HORIZONTAL, 25, 25);
        cardsContainer.setPadding(new Insets(10, 0, 10, 0));

        ScrollPane scroll = new ScrollPane(cardsContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #F8F9FA; -fx-border-color: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        muatDaftarOutfit("SEMUA");

        content.getChildren().addAll(title, filterBox, scroll);
        return content;
    }

    private void muatDaftarOutfit(String filter) {
        cardsContainer.getChildren().clear();
        
        List<OutfitBundle> list = filter.equals("SEMUA") 
                                  ? ds.getAllOutfit() 
                                  : ds.getOutfitByStyle(filter);

        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.toLowerCase();
            list = list.stream().filter(o -> 
                o.getNamaPaket().toLowerCase().contains(kw) || 
                o.getDeskripsi().toLowerCase().contains(kw)
            ).toList();
        }

        if (list.isEmpty()) {
            Label kosong = new Label("Belum ada outfit untuk kategori ini.");
            kosong.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            cardsContainer.getChildren().add(kosong);
            return;
        }

        for (OutfitBundle ob : list) {
            cardsContainer.getChildren().add(buildOutfitCard(ob));
        }
    }

    private VBox buildOutfitCard(OutfitBundle ob) {
        VBox card = new VBox(12);
        
        card.setPrefWidth(250); 
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 12; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);"
        );

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12 12 0 0;");
        
        imageBox.setPrefHeight(280); 
        
        if (ob.getImagePath() != null && !ob.getImagePath().isEmpty()) {
            try {
                String imgUri = new java.io.File(ob.getImagePath()).toURI().toString();
                ImageView imgView = new ImageView(new Image(imgUri));
                
                imgView.setFitWidth(250);
                imgView.setFitHeight(280);
                imgView.setPreserveRatio(false); 
                
                javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(250, 280);
                clip.setArcWidth(24);
                clip.setArcHeight(24);
                imgView.setClip(clip);
                
                imageBox.getChildren().add(imgView);
            } catch (Exception e) {
                Label lblImgError = new Label("📷\nGambar tidak ditemukan");
                lblImgError.setTextAlignment(TextAlignment.CENTER);
                lblImgError.setTextFill(Color.GRAY);
                imageBox.getChildren().add(lblImgError);
            }
        } else {
            Label lblNoImg = new Label("📷\nBelum ada gambar");
            lblNoImg.setTextAlignment(TextAlignment.CENTER);
            lblNoImg.setTextFill(Color.GRAY);
            imageBox.getChildren().add(lblNoImg);
        }

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(15));

        Label lblStyle = new Label(ob.getKategoriStyle());
        lblStyle.setStyle("-fx-background-color: #222222; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 5;");
        
        Label lblNama = new Label(ob.getNamaPaket());
        lblNama.setStyle("-fx-text-fill: #111111; -fx-font-weight: bold; -fx-font-size: 18px;");
        lblNama.setWrapText(true);

        Label lblDeskripsi = new Label(ob.getDeskripsi());
        lblDeskripsi.setStyle("-fx-text-fill: #666666; -fx-font-size: 13px;");
        lblDeskripsi.setWrapText(true);

        VBox itemsBox = new VBox(5);
        itemsBox.setStyle("-fx-background-color: #F9F9F9; -fx-padding: 10; -fx-background-radius: 8;");
        Label lblIsi = new Label("Isi Paket:");
        lblIsi.setStyle("-fx-font-weight: bold; -fx-text-fill: #444444; -fx-font-size: 12px;");
        itemsBox.getChildren().add(lblIsi);
        
        for (Produk p : ob.getListProduk()) {
            Label lblItem = new Label("• " + p.getNama());
            lblItem.setStyle("-fx-text-fill: #555555; -fx-font-size: 13px;");
            itemsBox.getChildren().add(lblItem);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label lblTotal = new Label(ob.getTotalHargaFormatted());
        lblTotal.setStyle("-fx-text-fill: " + StyleKit.ACCENT + "; -fx-font-weight: bold; -fx-font-size: 20px;");

        Button btnBeliSet = StyleKit.primaryButton("🛒 Beli 1 Set");
        btnBeliSet.setMaxWidth(Double.MAX_VALUE);
        btnBeliSet.setOnAction(e -> {
            for (Produk p : ob.getListProduk()) {
                keranjang.tambahItem(p, 1);
            }
            dashboard.updateCartBadge();
            
            StyleKit.showSuccessDialog("Berhasil!", "Paket '" + ob.getNamaPaket() + "' ditambahkan ke keranjang.");
        });

        contentBox.getChildren().addAll(lblStyle, lblNama, lblDeskripsi, itemsBox, spacer, lblTotal, btnBeliSet);
        
        card.getChildren().addAll(imageBox, contentBox);
        return card;
    }
}