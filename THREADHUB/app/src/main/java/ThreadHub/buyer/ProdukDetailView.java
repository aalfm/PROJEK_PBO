package ThreadHub.buyer;

import ThreadHub.controller.KeranjangController;
import ThreadHub.model.*;
import ThreadHub.view.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProdukDetailView {
  private final BuyerDashboardView dashboard;
  private final Produk produk;
  private final KeranjangController keranjang;

  public ProdukDetailView(BuyerDashboardView dashboard, Produk produk, KeranjangController keranjang) {
    this.dashboard = dashboard;
    this.produk = produk;
    this.keranjang = keranjang;
  }

  public ScrollPane build() {
    VBox content = new VBox(24);
    content.setPadding(new Insets(36));
    content.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");

    Button btnBack = StyleKit.outlineButton("Kembali");
    btnBack.setOnAction(e -> dashboard.showProdukView());

    HBox body = new HBox(32);
    body.setAlignment(Pos.TOP_LEFT);

    StackPane imgBox = new StackPane();
    imgBox.setAlignment(Pos.CENTER);
    imgBox.setPrefSize(300, 350);
    imgBox.setStyle("-fx-background-color: " + StyleKit.SIDEBAR_BG + "; -fx-background-radius: 14;");

    if (produk.getImagePath() != null && !produk.getImagePath().isBlank()) {
      try {
        String imgUri = new java.io.File(produk.getImagePath()).toURI().toString();
        ImageView imgView = new ImageView(new Image(imgUri));
        imgView.setFitWidth(300);
        imgView.setFitHeight(350);
        imgView.setPreserveRatio(true);
        imgBox.getChildren().add(imgView);
      } catch (Exception ex) {
        Label emoji = new Label(emojiKategori(produk.getKategori()));
        emoji.setFont(Font.font(100));
        imgBox.getChildren().add(emoji);
      }
    } else {
      Label emoji = new Label(emojiKategori(produk.getKategori()));
      emoji.setFont(Font.font(100));
      imgBox.getChildren().add(emoji);
    }

    VBox info = new VBox(14);
    info.setMaxWidth(500);
    HBox.setHgrow(info, Priority.ALWAYS);

    Label badgeKat = new Label(produk.getKategori().toUpperCase());
    badgeKat.setStyle("-fx-background-color: " + StyleKit.SIDEBAR_BG + "; -fx-text-fill: " + StyleKit.ACCENT + "; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 12; -fx-background-radius: 6;");

    Label nama = StyleKit.titleLabel(produk.getNama(), 26);
    nama.setWrapText(true);

    Label harga = new Label(produk.getHargaFormatted());
    harga.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 28));
    harga.setTextFill(Color.web(StyleKit.ACCENT));

    GridPane attrs = new GridPane();
    attrs.setHgap(20); attrs.setVgap(8);
    addAttr(attrs, 0, "Ukuran", produk.getUkuran());
    addAttr(attrs, 1, "Warna", produk.getWarna());
    addAttr(attrs, 2, "Stok", produk.isAvailable() ? produk.getStok() + " tersedia" : "Habis");

    Label deskLabel = new Label("Deskripsi");
    deskLabel.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 14));
    deskLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));

    Label deskripsi = new Label(produk.getDeskripsi());
    deskripsi.setWrapText(true);
    deskripsi.setFont(Font.font(StyleKit.FONT_FAMILY, 14));
    deskripsi.setTextFill(Color.web(StyleKit.TEXT_PRIMARY));

    Label jumlahLabel = new Label("Jumlah");
    jumlahLabel.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 14));
    jumlahLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));

    Spinner<Integer> spinner = new Spinner<>(1, Math.max(1, produk.getStok()), 1);
    spinner.setPrefWidth(100);
    spinner.setStyle("-fx-background-color: " + StyleKit.CARD_BG + ";");

    Button btnKeranjang = StyleKit.primaryButton("Tambah ke Keranjang");
    btnKeranjang.setDisable(!produk.isAvailable());
    btnKeranjang.setOnAction(e -> {
      keranjang.tambahItem(produk, spinner.getValue());
      dashboard.updateCartBadge();
      
      StyleKit.showSuccessDialog("Berhasil!", spinner.getValue() + "x '" + produk.getNama() + "' ditambahkan ke keranjang.");
    });

    info.getChildren().addAll(
      badgeKat, nama, harga,
      StyleKit.hSeparator(),
      attrs,
      StyleKit.hSeparator(),
      deskLabel, deskripsi,
      StyleKit.hSeparator(),
      jumlahLabel, spinner, btnKeranjang
    );

    body.getChildren().addAll(imgBox, info);
    content.getChildren().addAll(btnBack, body);

    ScrollPane scroll = new ScrollPane(content);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background-color: " + StyleKit.DARK_BG + "; -fx-background: " + StyleKit.DARK_BG + ";");
    return scroll;
  }

  private void addAttr(GridPane grid, int row, String key, String value) {
    Label k = new Label(key + ":");
    k.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
    k.setTextFill(Color.web(StyleKit.TEXT_MUTED));
    Label v = new Label(value);
    v.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 13));
    v.setTextFill(Color.web(StyleKit.TEXT_PRIMARY));
    grid.add(k, 0, row);
    grid.add(v, 1, row);
  }

  private String emojiKategori(String kategori) {
    return switch (kategori.toLowerCase()) {
      case "kaos" -> "👕";
      case "kemeja" -> "👔";
      case "celana" -> "👖";
      case "jaket" -> "🧥";
      case "hoodie" -> "🥷";
      default -> "?";
    };
  }
}