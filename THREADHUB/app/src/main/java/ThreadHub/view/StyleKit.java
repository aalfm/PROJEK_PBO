package ThreadHub.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StyleKit {
  public static final String DARK_BG = "#1a1a2e";
  public static final String CARD_BG = "#16213e";
  public static final String ACCENT = "#e94560";
  public static final String ACCENT_HOVER = "#c73652";
  public static final String SUCCESS = "#0f9b58";
  public static final String WARNING = "#f39c12";
  public static final String TEXT_PRIMARY = "#eaeaea";
  public static final String TEXT_MUTED = "#8892a4";
  public static final String BORDER = "#0f3460";
  public static final String SIDEBAR_BG = "#0f3460";
  public static final String TEXT_CONTRAST = "#ffffff";
  public static final String FONT_FAMILY = "Segoe UI";
  public static final double RADIUS = 10.0;

  public static Button primaryButton(String text) {
    Button btn = new Button(text);
    btn.setStyle("-fx-background-color: " + ACCENT + ";" +
      "-fx-text-fill: white;" +
      "-fx-font-size: 13px;" +
      "-fx-font-weight: bold;" +
      "-fx-padding: 10 24;" +
      "-fx-background-radius: 8;" +
      "-fx-cursor: hand;"
    );
    btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + ACCENT_HOVER + ";" +
      "-fx-text-fill: white;" +
      "-fx-font-size: 13px;" +
      "-fx-font-weight: bold;" +
      "-fx-padding: 10 24;" +
      "-fx-background-radius: 8;" +
      "-fx-cursor: hand;"
    ));
    btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + ACCENT + ";" +
      "-fx-text-fill: white;" +
      "-fx-font-size: 13px;" +
      "-fx-font-weight: bold;" +
      "-fx-padding: 10 24;" +
      "-fx-background-radius: 8;" +
      "-fx-cursor: hand;"
    ));
    return btn;
  }

  public static Button outlineButton(String text) {
    Button btn = new Button(text);
    btn.setStyle("-fx-background-color: transparent;" +
      "-fx-text-fill: " + ACCENT + ";" +
      "-fx-border-color: " + ACCENT + ";" +
      "-fx-border-radius: 8;" +
      "-fx-background-radius: 8;" +
      "-fx-font-size: 13px;" +
      "-fx-padding: 9 22;" +
      "-fx-cursor: hand;"
    );
    return btn;
  }

  public static Button sidebarButton(String text) {
    Button btn = new Button(text);
    btn.setMaxWidth(Double.MAX_VALUE);
    btn.setStyle("-fx-background-color: transparent;" +
      "-fx-text-fill: " + TEXT_PRIMARY + ";" +
      "-fx-font-size: 14px;" +
      "-fx-padding: 12 20;" +
      "-fx-alignment: CENTER_LEFT;" +
      "-fx-cursor: hand;" +
      "-fx-background-radius: 8;"
    );
    btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: rgba(233,69,96,0.15);" +
      "-fx-text-fill: " + ACCENT + ";" +
      "-fx-font-size: 14px;" +
      "-fx-padding: 12 20;" +
      "-fx-alignment: CENTER_LEFT;" +
      "-fx-cursor: hand;" +
      "-fx-background-radius: 8;"
    ));
    btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent;" +
      "-fx-text-fill: " + TEXT_PRIMARY + ";" +
      "-fx-font-size: 14px;" +
      "-fx-padding: 12 20;" +
      "-fx-alignment: CENTER_LEFT;" +
      "-fx-cursor: hand;" +
      "-fx-background-radius: 8;"
    ));
    return btn;
  }

  public static Label titleLabel(String text, int size) {
    Label lbl = new Label(text);
    lbl.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, size));
    lbl.setTextFill(Color.web(TEXT_PRIMARY));
    return lbl;
  }

  public static Label mutedLabel(String text) {
    Label lbl = new Label(text);
    lbl.setFont(Font.font(FONT_FAMILY, 12));
    lbl.setTextFill(Color.web(TEXT_MUTED));
    return lbl;
  }

  public static VBox card(double padding) {
    VBox box = new VBox(10);
    box.setPadding(new Insets(padding));
    box.setStyle("-fx-background-color: " + CARD_BG + ";" +
      "-fx-background-radius: " + RADIUS + ";" +
      "-fx-border-color: " + BORDER + ";" +
      "-fx-border-radius: " + RADIUS + ";" +
      "-fx-border-width: 1;"
    );
    return box;
  }

  public static Region hSeparator() {
    Region sep = new Region();
    sep.setPrefHeight(1);
    sep.setStyle("-fx-background-color: " + BORDER + ";");
    return sep;
  }

  public static void showSuccessDialog(String titleText, String msg) {
    javafx.stage.Stage dialog = new javafx.stage.Stage();
    dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
    dialog.initStyle(javafx.stage.StageStyle.TRANSPARENT);

    VBox root = new VBox(15);
    root.setPadding(new Insets(30));
    root.setAlignment(javafx.geometry.Pos.CENTER);
    root.setPrefWidth(420);
    root.setStyle("-fx-background-color: #1a1c29; -fx-background-radius: 15; -fx-border-color: #2d3142; -fx-border-radius: 15; -fx-border-width: 2;");

    Label icon = new Label("✅");
    icon.setStyle("-fx-font-size: 48px;");

    Label title = new Label(titleText);
    title.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-font-size: 20px;");

    Label content = new Label(msg);
    content.setStyle("-fx-text-fill: #a9b1d6; -fx-font-size: 14px;");
    content.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
    content.setWrapText(true);
    content.setMaxWidth(340);

    Button btnOk = StyleKit.primaryButton("OK");
    btnOk.setMinWidth(120);
    btnOk.setOnAction(e -> dialog.close());

    root.getChildren().addAll(icon, title, content, btnOk);

    javafx.scene.Scene scene = new javafx.scene.Scene(root);
    scene.setFill(Color.TRANSPARENT);
    dialog.setScene(scene);
    dialog.centerOnScreen();
    dialog.showAndWait();
  }

  public static void showErrorDialog(String titleText, String msg) {
    javafx.stage.Stage dialog = new javafx.stage.Stage();
    dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
    dialog.initStyle(javafx.stage.StageStyle.TRANSPARENT);

    VBox root = new VBox(15);
    root.setPadding(new Insets(30));
    root.setAlignment(javafx.geometry.Pos.CENTER);
    root.setPrefWidth(420);
    root.setStyle("-fx-background-color: #1a1c29; -fx-background-radius: 15; -fx-border-color: #2d3142; -fx-border-radius: 15; -fx-border-width: 2;");

    Label icon = new Label("❌");
    icon.setStyle("-fx-font-size: 48px;");

    Label title = new Label(titleText);
    title.setStyle("-fx-text-fill: #e06c75; -fx-font-weight: bold; -fx-font-size: 20px;");

    Label content = new Label(msg);
    content.setStyle("-fx-text-fill: #a9b1d6; -fx-font-size: 14px;");
    content.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
    content.setWrapText(true);
    content.setMaxWidth(340);

    Button btnOk = StyleKit.primaryButton("Mengerti");
    btnOk.setMinWidth(120);
    btnOk.setOnAction(e -> dialog.close());

    root.getChildren().addAll(icon, title, content, btnOk);

    javafx.scene.Scene scene = new javafx.scene.Scene(root);
    scene.setFill(Color.TRANSPARENT);
    dialog.setScene(scene);
    dialog.centerOnScreen();
    dialog.showAndWait();
}

public static void showConfirmationDialog(String titleText, String headerText, String contentText, Runnable onConfirm) {
    javafx.stage.Stage dialog = new javafx.stage.Stage();
    dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
    dialog.initStyle(javafx.stage.StageStyle.TRANSPARENT);

    VBox root = new VBox(15);
    root.setPadding(new Insets(30));
    root.setAlignment(javafx.geometry.Pos.CENTER);
    root.setPrefWidth(420);
    root.setStyle("-fx-background-color: #1a1c29; -fx-background-radius: 15; -fx-border-color: #2d3142; -fx-border-radius: 15; -fx-border-width: 2;");

    Label icon = new Label("❓");
    icon.setStyle("-fx-font-size: 48px;");

    Label title = new Label(titleText);
    title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px;");

    Label header = new Label(headerText);
    header.setStyle("-fx-text-fill: #a9b1d6; -fx-font-size: 14px; -fx-font-weight: bold;");

    Label content = new Label(contentText);
    content.setStyle("-fx-text-fill: #8e95b3; -fx-font-size: 13px;");
    content.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

    Button btnYes = StyleKit.primaryButton("Ya, Lanjutkan");
    btnYes.setStyle(btnYes.getStyle() + "-fx-background-color: #e06c75;");
    btnYes.setOnAction(e -> { onConfirm.run(); dialog.close(); });

    Button btnNo = StyleKit.outlineButton("Batal");
    btnNo.setOnAction(e -> dialog.close());

    HBox btnBox = new HBox(15, btnNo, btnYes);
    btnBox.setAlignment(javafx.geometry.Pos.CENTER);

    root.getChildren().addAll(icon, title, header, content, btnBox);

    javafx.scene.Scene scene = new javafx.scene.Scene(root);
    scene.setFill(Color.TRANSPARENT);
    dialog.setScene(scene);
    dialog.centerOnScreen();
    dialog.showAndWait();
}
}