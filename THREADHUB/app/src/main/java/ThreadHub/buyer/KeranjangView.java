package ThreadHub.buyer;

import ThreadHub.controller.KeranjangController;
import ThreadHub.model.*;
import ThreadHub.view.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.StageStyle;

public class KeranjangView {
  private final BuyerDashboardView dashboard;
  private final KeranjangController keranjang;
  
  private Label totalValueLabel;
  private Label subtotalValueLabel;
  private Label ppnValueLabel;
  private Label pphValueLabel;
  private Label diskonValueLabel;
  private HBox rowDiskon;
  private HBox rowPpn;
  private HBox rowPph; 
  
  private StackPane mainStack;
  
  public KeranjangView(BuyerDashboardView dashboard, KeranjangController keranjang) {
    this.dashboard = dashboard;
    this.keranjang = keranjang;
  }

  public StackPane build() {
    mainStack = new StackPane();
    
    VBox content = new VBox(20);
    content.setPadding(new Insets(30));
    content.setStyle("-fx-background-color: #FFFFFF;");

    Label title = StyleKit.titleLabel("Keranjang Belanja", 22);
    title.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 22px;");

    if (keranjang.isEmpty()) {
      Label kosong = new Label("Keranjang kosong. Yuk belanja dulu!");
      kosong.setStyle("-fx-text-fill: gray; -fx-font-size: 16px;");
      content.getChildren().addAll(title, kosong);
      mainStack.getChildren().add(content);
      return mainStack;
    }

    VBox itemList = new VBox(12);
    for (ItemKeranjang item : keranjang.getItems()) {
      itemList.getChildren().add(buildItemRow(item));
    }

    ScrollPane scroll = new ScrollPane(itemList);
    scroll.setFitToWidth(true);
    scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
    scroll.setPrefHeight(360);
    VBox.setVgrow(scroll, Priority.ALWAYS);

    itemList.setCache(true);
    itemList.setCacheHint(javafx.scene.CacheHint.SPEED);

    itemList.setOnScroll(speedEvent -> {
        double deltaY = speedEvent.getDeltaY() * 3;
        double width = scroll.getContent().getBoundsInLocal().getHeight();
        double vvalue = scroll.getVvalue();
        scroll.setVvalue(vvalue - deltaY / width);
    });

    VBox summary = StyleKit.card(20);
    summary.setSpacing(12);

    Label rincianTitle = new Label("Rincian Pembayaran");
    rincianTitle.setStyle("-fx-text-fill: #eaeaea; -fx-font-weight: bold; -fx-font-size: 16px;");
    
    double subtotal = keranjang.getSubtotal();
    double diskon = keranjang.getDiskon();
    double ppn = keranjang.getPpn();
    double pph = keranjang.getPph();
    double totalHarga = keranjang.getTotal();
    
    subtotalValueLabel = new Label(String.format("Rp %,.0f", subtotal));
    HBox rowSubtotal = buildRow("Subtotal", subtotalValueLabel, false);
    
    ppnValueLabel = new Label(String.format("Rp %,.0f", ppn));
    rowPpn = buildRow("PPN (11%)", ppnValueLabel, false);
    
    pphValueLabel = new Label(String.format("Rp %,.0f", pph));
    rowPph = buildRow("PPh", pphValueLabel, false);
    
    diskonValueLabel = new Label(String.format("- Rp %,.0f", diskon));
    rowDiskon = buildRow("Diskon", diskonValueLabel, true);
    
    rowDiskon.setVisible(diskon > 0);
    rowDiskon.setManaged(diskon > 0);
    rowPpn.setVisible(ppn > 0);
    rowPpn.setManaged(ppn > 0);
    rowPph.setVisible(pph > 0);
    rowPph.setManaged(pph > 0);

    VBox rincianBox = new VBox(6);
    rincianBox.getChildren().addAll(rowSubtotal, rowDiskon, rowPpn, rowPph);

    Label totalLabel = new Label("Total Bayar");
    totalLabel.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 14px; -fx-padding: 8 0 0 0;");

    totalValueLabel = new Label(String.format("Rp %,.0f", totalHarga));
    totalValueLabel.setStyle("-fx-text-fill: " + StyleKit.ACCENT + "; -fx-font-weight: bold; -fx-font-size: 28px;");

    Button btnCheckout = StyleKit.primaryButton("Bayar Sekarang");
    btnCheckout.setMaxWidth(Double.MAX_VALUE);
    btnCheckout.setOnAction(e -> doCheckout());

    Button btnKosongkan = StyleKit.outlineButton("Kosongkan Keranjang");
    btnKosongkan.setMaxWidth(Double.MAX_VALUE);
    btnKosongkan.setOnAction(e -> {
      keranjang.kosongkanKeranjang();
      dashboard.updateCartBadge();
      dashboard.showKeranjangView();
    });

    summary.getChildren().addAll(
        rincianTitle, 
        rincianBox, 
        StyleKit.hSeparator(), 
        totalLabel, 
        totalValueLabel, 
        btnCheckout, 
        btnKosongkan
    );

    HBox layout = new HBox(20, scroll, summary);
    HBox.setHgrow(scroll, Priority.ALWAYS);
    summary.setPrefWidth(280);
    layout.setAlignment(Pos.TOP_LEFT);

    content.getChildren().addAll(title, StyleKit.hSeparator(), layout);
    
    mainStack.getChildren().add(content);
    return mainStack;
  }
  
  private HBox buildRow(String key, Label valLabel, boolean isAccent) {
      HBox row = new HBox(10);
      row.setAlignment(Pos.CENTER_LEFT);
      
      Label lblKey = new Label(key);
      lblKey.setStyle("-fx-text-fill: gray; -fx-font-size: 13px;");
      
      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);
      
      valLabel.setStyle("-fx-text-fill: " + (isAccent ? StyleKit.ACCENT : "#ffffff") + "; -fx-font-size: 13px; -fx-font-weight: bold;");
      
      row.getChildren().addAll(lblKey, spacer, valLabel);
      return row;
  }

  private HBox buildItemRow(ItemKeranjang item) {
    HBox row = new HBox(16);
    row.setAlignment(Pos.CENTER_LEFT);
    row.setPadding(new Insets(14));
    row.setStyle("-fx-background-color: #F9F9F9; -fx-background-radius: 10; -fx-border-color: #EEEEEE; -fx-border-radius: 10;");

    VBox info = new VBox(4);
    HBox.setHgrow(info, Priority.ALWAYS);

    Label lblNama = new Label(item.getProduk().getNama());
    lblNama.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 15px;");

    Label lblHarga = new Label(item.getProduk().getHargaFormatted() + " /pcs");
    lblHarga.setStyle("-fx-text-fill: gray; -fx-font-size: 13px;");

    info.getChildren().addAll(lblNama, lblHarga);

    Label lblSubtotal = new Label(item.getSubtotalFormatted());
    lblSubtotal.setStyle("-fx-text-fill: " + StyleKit.ACCENT + "; -fx-font-weight: bold; -fx-font-size: 15px;");

    Button btnMinus = new Button("-");
    btnMinus.setStyle("-fx-text-fill: black; -fx-background-color: #E0E0E0; -fx-cursor: hand; -fx-font-weight: bold;");

    Button btnPlus = new Button("+");
    btnPlus.setStyle("-fx-text-fill: black; -fx-background-color: #E0E0E0; -fx-cursor: hand; -fx-font-weight: bold;");

    Label lblJumlah = new Label(String.valueOf(item.getJumlah()));
    lblJumlah.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
    lblJumlah.setMinWidth(30);
    lblJumlah.setAlignment(Pos.CENTER);

    btnMinus.setOnAction(e -> {
      if (item.getJumlah() > 1) {
        item.setJumlah(item.getJumlah() - 1);
        lblJumlah.setText(String.valueOf(item.getJumlah()));
        lblSubtotal.setText(item.getSubtotalFormatted());
        updateTotalSummary();
      }
    });

    btnPlus.setOnAction(e -> {
      if (item.getJumlah() < item.getProduk().getStok()) {
        item.setJumlah(item.getJumlah() + 1);
        lblJumlah.setText(String.valueOf(item.getJumlah()));
        lblSubtotal.setText(item.getSubtotalFormatted());
        updateTotalSummary();
      }
    });

    HBox qtyBox = new HBox(8, btnMinus, lblJumlah, btnPlus);
    qtyBox.setAlignment(Pos.CENTER);

    Button btnHapus = new Button("x");
    btnHapus.setStyle("-fx-background-color: transparent; -fx-text-fill: " + StyleKit.ACCENT + "; -fx-font-size: 16px; -fx-cursor: hand; -fx-font-weight: bold;");
    btnHapus.setOnAction(e -> {
      keranjang.hapusItem(item);
      dashboard.updateCartBadge();
      dashboard.showKeranjangView();
    });

    Region spacer1 = new Region(); spacer1.setMinWidth(20);
    Region spacer2 = new Region(); spacer2.setMinWidth(10);

    row.getChildren().addAll(info, qtyBox, spacer1, lblSubtotal, spacer2, btnHapus);
    return row;
  }

  private void updateTotalSummary() {
    if (totalValueLabel != null) {
      double subtotal = keranjang.getSubtotal();
      double diskon = keranjang.getDiskon();
      double ppn = keranjang.getPpn();
      double pph = keranjang.getPph();
      double totalHarga = keranjang.getTotal();

      rowDiskon.setVisible(diskon > 0);
      rowDiskon.setManaged(diskon > 0);
      rowPpn.setVisible(ppn > 0);
      rowPpn.setManaged(ppn > 0);
      rowPph.setVisible(pph > 0);
      rowPph.setManaged(pph > 0);

      subtotalValueLabel.setText(String.format("Rp %,.0f", subtotal));
      ppnValueLabel.setText(String.format("Rp %,.0f", ppn));
      pphValueLabel.setText(String.format("Rp %,.0f", pph));
      
      if (diskon > 0) {
          diskonValueLabel.setText(String.format("- Rp %,.0f", diskon));
      }
        
      totalValueLabel.setText(String.format("Rp %,.0f", totalHarga));
    }
  }

  private void doCheckout() {
    Alert confirm = new Alert(Alert.AlertType.NONE);
    confirm.initStyle(StageStyle.TRANSPARENT);

    DialogPane dialogPane = confirm.getDialogPane();
    dialogPane.setStyle("-fx-background-color: transparent;");
    dialogPane.getScene().setFill(Color.TRANSPARENT);

    VBox root = new VBox(15);
    root.setPadding(new Insets(30));
    root.setAlignment(Pos.CENTER);
    root.setPrefSize(480, 260);
    root.setStyle("-fx-background-color: #1a1c29; -fx-background-radius: 15; -fx-border-color: #2d3142; -fx-border-radius: 15; -fx-border-width: 2;");

    Label icon = new Label("❓");
    icon.setStyle("-fx-font-size: 48px;");

    Label title = new Label("Konfirmasi Pembayaran");
    title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px;");
    
    Label content = new Label("Yakin ingin melanjutkan pembayaran?\nTotal: " + totalValueLabel.getText());
    content.setStyle("-fx-text-fill: #a9b1d6; -fx-font-size: 14px; -fx-line-spacing: 4;");
    content.setTextAlignment(TextAlignment.CENTER);

    Button btnYes = StyleKit.primaryButton("Ya, Bayar Sekarang");
    btnYes.setOnAction(e -> {
        confirm.setResult(ButtonType.OK);
        confirm.close();
    });

    Button btnNo = StyleKit.outlineButton("Batal");
    btnNo.setOnAction(e -> {
        confirm.setResult(ButtonType.CANCEL);
        confirm.close();
    });

    HBox btnBox = new HBox(15, btnNo, btnYes);
    btnBox.setAlignment(Pos.CENTER);

    root.getChildren().addAll(icon, title, content, btnBox);
    dialogPane.setContent(root);

    confirm.showAndWait().ifPresent(btn -> {
        if (btn == ButtonType.OK) {
            prosesTransaksi();
        }
    });
  }

  private void prosesTransaksi() {
    Transaksi trx = keranjang.checkout();
    if (trx != null) {
        dashboard.updateCartBadge();
        showCustomResultDialog("✅", "Pembayaran Berhasil!", "Pesanan " + trx.getRingkasan() + "\nTerima kasih telah berbelanja di ThreadHub!", true);
    } else {
        showCustomResultDialog("❌", "Checkout Gagal", "Pesanan tidak dapat diproses.\nHarap periksa kembali ketersediaan stok produk.", false);
    }
  }

  private void showCustomResultDialog(String iconStr, String titleText, String msg, boolean isSuccess) {
    Alert resultAlert = new Alert(Alert.AlertType.NONE);
    resultAlert.initStyle(StageStyle.TRANSPARENT);

    DialogPane dialogPane = resultAlert.getDialogPane();
    dialogPane.setStyle("-fx-background-color: transparent;");
    dialogPane.getScene().setFill(Color.TRANSPARENT);

    VBox root = new VBox(15);
    root.setPadding(new Insets(30));
    root.setAlignment(Pos.CENTER);
    root.setPrefSize(480, 320);
    root.setStyle("-fx-background-color: #1a1c29; -fx-background-radius: 15; -fx-border-color: #2d3142; -fx-border-radius: 15; -fx-border-width: 2;");

    Label icon = new Label(iconStr);
    icon.setStyle("-fx-font-size: 48px;");

    Label title = new Label(titleText);
    title.setStyle("-fx-text-fill: " + (isSuccess ? "#2ecc71" : "#e06c75") + "; -fx-font-weight: bold; -fx-font-size: 20px;");

    Label content = new Label(msg);
    content.setStyle("-fx-text-fill: #a9b1d6; -fx-font-size: 13px; -fx-line-spacing: 5;");
    content.setTextAlignment(TextAlignment.CENTER);
    content.setWrapText(true);

    Button btnOk = StyleKit.primaryButton("Mengerti");
    btnOk.setMinWidth(140);
    btnOk.setOnAction(e -> {
        resultAlert.setResult(ButtonType.OK);
        resultAlert.close();
    });

    root.getChildren().addAll(icon, title, content, btnOk);
    dialogPane.setContent(root);

    resultAlert.showAndWait();
    
    if (isSuccess) {
        dashboard.showRiwayatView();
    }
  }
}