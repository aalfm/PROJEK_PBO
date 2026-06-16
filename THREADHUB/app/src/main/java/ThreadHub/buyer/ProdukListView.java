package ThreadHub.buyer;

import ThreadHub.controller.*;
import ThreadHub.model.*;
import ThreadHub.view.*;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.util.List;

public class ProdukListView {
  private final BuyerDashboardView dashboard;
  private final Buyer buyer;
  private final KeranjangController keranjang;
  private final DataStore ds = DataStore.getInstance();
  private final String currentGender;
  private final String searchKeyword;
  private FlowPane grid;

  public ProdukListView(BuyerDashboardView dashboard, Buyer buyer, KeranjangController keranjang, String gender, String keyword) {
    this.dashboard = dashboard;
    this.buyer = buyer;
    this.keranjang = keranjang;
    this.currentGender = gender;
    this.searchKeyword = keyword;
  }

  public ScrollPane build() {
    VBox content = new VBox(25);
    content.setPadding(new Insets(30, 40, 40, 40));
    content.setStyle("-fx-background-color: #FFFFFF;");

    String titleText = currentGender.equals("SEMUA") ? "Koleksi Pakaian" : "Koleksi " + currentGender;
    if (searchKeyword != null && !searchKeyword.isBlank()) {
      titleText = "Hasil Pencarian: \"" + searchKeyword + "\"";
    }

    Label title = new Label(titleText);
    title.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 24));
    title.setTextFill(Color.BLACK);

    ComboBox<String> kategoriBox = new ComboBox<>();
    kategoriBox.getItems().add("Semua");

    kategoriBox.getItems().addAll("Kaos", "Kemeja", "Celana", "Sepatu", "Topi", "Kaos Polo", "Rok", "Gaun", "Jaket", "Hoodie");
    kategoriBox.setValue("Semua");
    kategoriBox.setStyle("-fx-background-color: transparent; -fx-border-color: #DDDDDD; -fx-border-radius: 0; -fx-padding: 2 10;");

    HBox filterRow = new HBox(15);
    filterRow.setAlignment(Pos.CENTER_LEFT);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    filterRow.getChildren().addAll(
      title, spacer,
      new Label("Kategori:") {{ setFont(Font.font(StyleKit.FONT_FAMILY, 12)); setTextFill(Color.GRAY); }},
      kategoriBox
    );

    grid = new FlowPane();
    grid.setHgap(20);
    grid.setVgap(40);
    grid.setPadding(new Insets(10, 0, 0, 0));

    applyFilter(searchKeyword, "Semua");
    kategoriBox.valueProperty().addListener((obs, o, nv) -> applyFilter(searchKeyword, nv));

    content.getChildren().addAll(filterRow, grid);

    ScrollPane scroll = new ScrollPane(content);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background-color: #FFFFFF; -fx-background: #FFFFFF; -fx-border-color: transparent;");
    return scroll;
  }

  private void applyFilter(String keyword, String kategori) {
    List<Produk> all = keyword == null || keyword.isBlank() ? ds.getAllProduk() : ds.cariProduk(keyword);
    if (!currentGender.equals("SEMUA")) {
      all = all.stream().filter(p -> p.getGender().equalsIgnoreCase(currentGender)).toList();
    }
    if (!"Semua".equals(kategori) && kategori != null) {
      all = all.stream().filter(p -> p.getKategori().equalsIgnoreCase(kategori)).toList();
    }
    loadGrid(all);
  }

  private void loadGrid(List<Produk> list) {
    grid.getChildren().clear();
    for (Produk p : list) {
      grid.getChildren().add(buildZaloraCard(p));
    }
    if (list.isEmpty()) {
      Label kosong = new Label("Tidak ada produk yang cocok dengan pencarian Anda.");
      kosong.setFont(Font.font(StyleKit.FONT_FAMILY, 14));
      kosong.setTextFill(Color.GRAY);
      grid.getChildren().add(kosong);
    }
  }

  private VBox buildZaloraCard(Produk p) {
    VBox card = new VBox(6);
    card.setPrefWidth(240);
    card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #EEEEEE; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4;");
    card.setCursor(Cursor.HAND);

    StackPane imgBox = new StackPane();
    imgBox.setPrefHeight(320);
    imgBox.setStyle("-fx-background-color: #F4F4F4;");

    if (p.getImagePath() != null && !p.getImagePath().isBlank()) {
      try {
        String imgUri = new java.io.File(p.getImagePath()).toURI().toString();
        ImageView imgView = new ImageView(new Image(imgUri));
        imgView.setFitWidth(240);
        imgView.setFitHeight(320);
        imgView.setPreserveRatio(true);
        imgBox.getChildren().add(imgView);
      } catch (Exception ex) {
        Label emoji = new Label(emojiKategori(p.getKategori()));
        emoji.setFont(Font.font(60));
        imgBox.getChildren().add(emoji);
      }
    } else {
      Label emoji = new Label(emojiKategori(p.getKategori()));
      emoji.setFont(Font.font(60));
      imgBox.getChildren().add(emoji);
    }

    Label kategori = new Label(p.getKategori().toUpperCase());
    kategori.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 10));
    kategori.setTextFill(Color.web("#888888"));

    Label nama = new Label(p.getNama());
    nama.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
    nama.setTextFill(Color.BLACK);
    nama.setWrapText(true);
    nama.setMaxHeight(40);

    Label harga = new Label(p.getHargaFormatted());
    harga.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 14));
    harga.setTextFill(Color.BLACK);

    Button btnBeli = new Button("TAMBAH KE TAS");
    btnBeli.setMaxWidth(Double.MAX_VALUE);
    btnBeli.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 8 0; -fx-cursor: hand;");
    btnBeli.setVisible(false);

    if (!p.isAvailable()) {
      btnBeli.setText("STOK HABIS");
      btnBeli.setStyle(btnBeli.getStyle().replace("#000000", "#CCCCCC"));
      btnBeli.setDisable(true);
    } else {
      btnBeli.setOnAction(e -> {
        keranjang.tambahItem(p, 1);
        dashboard.updateCartBadge();
        StyleKit.showSuccessDialog("Berhasil!", p.getNama() + " ditambahkan ke keranjang.");
      });
    }

    VBox textContent = new VBox(3, kategori, nama, harga);
    textContent.setPadding(new Insets(10, 10, 5, 10));

    VBox btnContainer = new VBox(btnBeli);
    btnContainer.setPadding(new Insets(0, 10, 10, 10));
    btnContainer.setPrefHeight(35);

    card.getChildren().addAll(imgBox, textContent, btnContainer);

    card.setOnMouseClicked(e -> dashboard.showDetailView(p));
    card.setOnMouseEntered(e -> {
      imgBox.setOpacity(0.85);
      btnBeli.setVisible(true);
    });
    card.setOnMouseExited(e -> {
      imgBox.setOpacity(1.0);
      btnBeli.setVisible(false);
    });

    return card;
  }

  private String emojiKategori(String kategori) {
    return switch (kategori.toLowerCase()) {
      case "kaos" -> "👕";
      case "kemeja" -> "👔";
      case "celana" -> "👖";
      case "jaket" -> "🧥";
      case "hoodie" -> "🥷";
      case "sepatu" -> "👟";
      case "topi" -> "🧢";
      case "rok", "gaun" -> "👗";
      case "kaos polo" -> "🎽";
      default -> "?";
    };
  }
}