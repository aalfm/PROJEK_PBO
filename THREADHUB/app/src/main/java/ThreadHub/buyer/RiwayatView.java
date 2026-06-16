package ThreadHub.buyer;

import ThreadHub.controller.DataStore;
import ThreadHub.model.*;
import ThreadHub.view.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.util.List;

public class RiwayatView {

    private final Buyer buyer;
    private final DataStore ds = DataStore.getInstance();

    public RiwayatView(Buyer buyer) {
        this.buyer = buyer;
    }

    public VBox build() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");

        Label title = StyleKit.titleLabel("📋 Riwayat Pembelian", 22);

        List<Transaksi> list = ds.getTransaksiBuyer(buyer);

        if (list.isEmpty()) {
            Label kosong = StyleKit.mutedLabel("Belum ada riwayat pembelian.");
            kosong.setFont(Font.font(StyleKit.FONT_FAMILY, 16));
            kosong.setPadding(new Insets(40, 0, 0, 0));
            content.getChildren().addAll(title, kosong);
            return content;
        }

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox trxList = new VBox(16);
        trxList.setPadding(new Insets(4, 0, 0, 0));

        for (int i = list.size() - 1; i >= 0; i--) {
            trxList.getChildren().add(buildTrxCard(list.get(i)));
        }
        scroll.setContent(trxList);

        content.getChildren().addAll(title, StyleKit.hSeparator(), scroll);
        return content;
    }

    private VBox buildTrxCard(Transaksi trx) {
        VBox card = StyleKit.card(18);
        card.setSpacing(10);

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        Label idLabel = new Label("#TRX-" + String.format("%04d", trx.getId()));
        idLabel.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 15));
        idLabel.setTextFill(Color.web(StyleKit.TEXT_PRIMARY));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label waktu = StyleKit.mutedLabel("🕐 " + trx.getWaktuFormatted());

        Label statusBadge = new Label(trx.getStatus());
        statusBadge.setStyle(
            "-fx-background-color: " + StyleKit.SUCCESS + ";" +
            "-fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;" +
            "-fx-padding: 3 10; -fx-background-radius: 6;"
        );
        header.getChildren().addAll(idLabel, spacer, waktu, statusBadge);

        VBox itemsBox = new VBox(6);
        for (ItemKeranjang item : trx.getItems()) {
            HBox itemRow = new HBox(10);
            itemRow.setAlignment(Pos.CENTER_LEFT);

            Label nama = new Label("• " + item.getProduk().getNama());
            nama.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
            nama.setTextFill(Color.web(StyleKit.TEXT_PRIMARY));

            Label qty = StyleKit.mutedLabel("x" + item.getJumlah());

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            Label sub = new Label(item.getSubtotalFormatted());
            sub.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 13));
            sub.setTextFill(Color.web(StyleKit.TEXT_PRIMARY));

            itemRow.getChildren().addAll(nama, qty, sp, sub);
            itemsBox.getChildren().add(itemRow);
        }

        VBox rincianBiayaBox = new VBox(6);
        rincianBiayaBox.setPadding(new Insets(5, 0, 5, 0));

        HBox rowSubtotal = buildRincianRow("Subtotal Produk", String.format("Rp %,.0f", trx.getSubtotal()), false);
        rincianBiayaBox.getChildren().add(rowSubtotal);

        if (trx.getDiskon() > 0) {
            HBox rowDiskon = buildRincianRow("Diskon", String.format("- Rp %,.0f", trx.getDiskon()), true);
            rincianBiayaBox.getChildren().add(rowDiskon);
        }

        if (trx.getPpn() > 0) {
            HBox rowPpn = buildRincianRow("PPN", String.format("Rp %,.0f", trx.getPpn()), false);
            rincianBiayaBox.getChildren().add(rowPpn);
        }

        if (trx.getPph() > 0) {
            HBox rowPph = buildRincianRow("PPh", String.format("Rp %,.0f", trx.getPph()), false);
            rincianBiayaBox.getChildren().add(rowPph);
        }

        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_RIGHT);
        Label totalKey = new Label("TOTAL  ");
        totalKey.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 14));
        totalKey.setTextFill(Color.web(StyleKit.TEXT_MUTED));
        Label totalVal = new Label(trx.getTotalFormatted());
        totalVal.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 18));
        totalVal.setTextFill(Color.web(StyleKit.ACCENT));
        totalRow.getChildren().addAll(totalKey, totalVal);

        card.getChildren().addAll(
                header,
                StyleKit.hSeparator(),
                itemsBox,
                StyleKit.hSeparator(),
                rincianBiayaBox,     
                StyleKit.hSeparator(),
                totalRow
        );
        return card;
    }

    private HBox buildRincianRow(String labelText, String valueText, boolean isDiscount) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(labelText);
        lbl.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
        lbl.setTextFill(Color.web(StyleKit.TEXT_MUTED));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label val = new Label(valueText);
        val.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
        val.setTextFill(Color.web(isDiscount ? StyleKit.ACCENT : StyleKit.TEXT_PRIMARY)); 

        row.getChildren().addAll(lbl, spacer, val);
        return row;
    }
}