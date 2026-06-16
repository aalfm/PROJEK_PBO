package ThreadHub.view;

import ThreadHub.controller.DataStore;
import ThreadHub.model.*;
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

public class LoginView {
  private final Stage stage;
  private Label errorLabel;

  public LoginView(Stage stage) {
    this.stage = stage;
  }

  public HBox buildLayout() {
    VBox brandPanel = new VBox(12);
    brandPanel.setAlignment(Pos.CENTER);
    brandPanel.setPrefWidth(400);
    brandPanel.setStyle("-fx-background-color: " + StyleKit.ACCENT + ";");
    brandPanel.setPadding(new Insets(60));

    ImageView logoView = new ImageView();
    try {
      Image img = new Image(getClass().getResourceAsStream("/logo_threadhub.png"));
      logoView.setImage(img);
      logoView.setFitWidth(250);
      logoView.setPreserveRatio(true);
      logoView.setSmooth(true);
    } catch (Exception e) {
      System.out.println("Gagal memuat gambar logo: " + e.getMessage());
    }

    Label appName = new Label("ThreadHub");
    appName.setFont(Font.font("Times New Roman", FontWeight.BOLD, 48));
    appName.setTextFill(Color.WHITE);
    VBox.setMargin(appName, new Insets(-25, 0, 0, 0));

    Label tagline = new Label("Temukan gaya terbaikmu.\nBelanja pakaian kualitas premium");
    tagline.setFont(Font.font(StyleKit.FONT_FAMILY, 15));
    tagline.setTextFill(Color.rgb(255, 255, 255, 0.85));
    tagline.setTextAlignment(TextAlignment.CENTER);
    tagline.setWrapText(true);

    brandPanel.getChildren().addAll(logoView, appName, tagline);

    VBox formPanel = new VBox(25);
    formPanel.setAlignment(Pos.CENTER);
    formPanel.setPadding(new Insets(60, 70, 60, 70));
    formPanel.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");
    formPanel.setPrefWidth(440);

    Label welcomeLabel = StyleKit.titleLabel("Selamat Datang!", 26);
    Label subLabel = StyleKit.mutedLabel("Masuk untuk melanjutkan ke ThreadHub");
    subLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 14));

    VBox inputContainer = new VBox(14);
    inputContainer.setMaxWidth(320);
    inputContainer.setAlignment(Pos.CENTER_LEFT);

    Label usernameLabel = new Label("Username");
    usernameLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));
    usernameLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
    TextField usernameField = new TextField();
    usernameField.setPromptText("Masukkan username");
    styleTextField(usernameField);

    Label passwordLabel = new Label("Password");
    passwordLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));
    passwordLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Masukkan password");
    styleTextField(passwordField);

    errorLabel = new Label("");
    errorLabel.setTextFill(Color.web(StyleKit.ACCENT));
    errorLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));

    Button loginBtn = StyleKit.primaryButton("Masuk");
    loginBtn.setMaxWidth(Double.MAX_VALUE);
    loginBtn.setStyle(loginBtn.getStyle() + "-fx-font-size: 15px; -fx-padding: 12 0;");

    loginBtn.setOnAction(e -> handleLogin(usernameField.getText().trim(), passwordField.getText()));
    passwordField.setOnAction(e -> handleLogin(usernameField.getText().trim(), passwordField.getText()));

    HBox registerBox = new HBox(5);
    registerBox.setAlignment(Pos.CENTER);
    Label lblBawah = new Label("Belum punya akun?");
    lblBawah.setTextFill(Color.web(StyleKit.TEXT_MUTED));
    lblBawah.setFont(Font.font(StyleKit.FONT_FAMILY, 13));

    Hyperlink linkDaftar = new Hyperlink("Daftar di sini");
    linkDaftar.setTextFill(Color.web(StyleKit.ACCENT));
    linkDaftar.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 13));
    linkDaftar.setBorder(Border.EMPTY);
    linkDaftar.setOnAction(e -> showRegisterDialog());
    registerBox.getChildren().addAll(lblBawah, linkDaftar);
    VBox.setMargin(registerBox, new Insets(5, 0, 0, 0));

    inputContainer.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, errorLabel, loginBtn, registerBox);
    formPanel.getChildren().addAll(welcomeLabel, subLabel, inputContainer);

    HBox root = new HBox(brandPanel, formPanel);
    HBox.setHgrow(formPanel, Priority.ALWAYS);

    return root;
  }

  public Scene buildScene() {
    return new Scene(buildLayout(), 840, 560);
  }

  private void handleLogin(String username, String password) {
    if (username.isEmpty() || password.isEmpty()) {
      errorLabel.setText("Username dan password tidak boleh kosong");
      return;
    }
    User user = DataStore.getInstance().login(username, password);
    if (user == null) {
      errorLabel.setText("Username atau password salah");
      return;
    }
    errorLabel.setText("");
    if ("admin".equals(user.getRole())) {
      new ThreadHub.admin.AdminDashboardView(stage, (ThreadHub.model.Admin) user).show();
    } else {
      new ThreadHub.buyer.BuyerDashboardView(stage, (ThreadHub.model.Buyer) user).show();
    }
  }

  private void showRegisterDialog() {
    Stage dialog = new Stage();
    dialog.setTitle("Daftar Akun Baru");
    dialog.initOwner(stage);

    VBox form = new VBox(14);
    form.setPadding(new Insets(30));
    form.setPrefWidth(350);
    form.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");

    Label title = StyleKit.titleLabel("Buat Akun Pembeli", 20);

    TextField tfNama = new TextField();
    tfNama.setPromptText("Nama Lengkap (Contoh: Budi Santoso)");
    styleTextField(tfNama);

    TextField tfUsername = new TextField();
    tfUsername.setPromptText("Username (tanpa spasi)");
    styleTextField(tfUsername);

    PasswordField pfPassword = new PasswordField();
    pfPassword.setPromptText("Password");
    styleTextField(pfPassword);

    Label errLabel = new Label("");
    errLabel.setTextFill(Color.web(StyleKit.ACCENT));
    errLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 12));

    Button btnDaftar = StyleKit.primaryButton("Daftar Sekarang");
    btnDaftar.setMaxWidth(Double.MAX_VALUE);
    btnDaftar.setOnAction(e -> {
      String nama = tfNama.getText().trim();
      String username = tfUsername.getText().trim();
      String password = pfPassword.getText().trim();

      if (nama.isEmpty() || username.isEmpty() || password.isEmpty()) {
        errLabel.setText("Semua kolom wajib diisi");
        return;
      }
      if (username.contains(" ")) {
        errLabel.setText("Username tidak boleh mengandung spasi");
        return;
      }
      if (!username.matches("^[a-zA-Z0-9_]+$")) {
        errLabel.setText("Username hanya boleh huruf, angka, dan underscore");
        return;
      }
      if (username.length() < 4) {
        errLabel.setText("Username minimal 4 karakter");
        return;
      }
      if (password.length() < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$")) {
        errLabel.setText("Password minimal 8 karakter (huruf dan angka)");
        return;
      }

      DataStore ds = DataStore.getInstance();
      boolean exists = ds.getAllUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
      if (exists) {
        errLabel.setText("Username sudah terpakai, pilih yang lain");
        return;
      }

      int newId = ds.generateUserId();
      Buyer pembeliBaru = new Buyer(newId, username, password, nama);
      ds.tambahUser(pembeliBaru);

      StyleKit.showSuccessDialog("Pendaftaran Berhasil!", "Akun berhasil dibuat! Anda akan langsung masuk ke Dashboard.");
      dialog.close();
      new ThreadHub.buyer.BuyerDashboardView(stage, pembeliBaru).show();
    });

    form.getChildren().addAll(
      title, StyleKit.hSeparator(),
      new Label("Nama Lengkap") {{ setTextFill(Color.web(StyleKit.TEXT_MUTED)); setFont(Font.font(12)); }},
      tfNama,
      new Label("Username") {{ setTextFill(Color.web(StyleKit.TEXT_MUTED)); setFont(Font.font(12)); }},
      tfUsername,
      new Label("Password") {{ setTextFill(Color.web(StyleKit.TEXT_MUTED)); setFont(Font.font(12)); }},
      pfPassword,
      errLabel, btnDaftar
    );

    dialog.setScene(new Scene(form));
    dialog.setResizable(false);
    dialog.showAndWait();
  }

  private void styleTextField(TextField tf) {
    String idleStyle =
      "-fx-background-color: " + StyleKit.CARD_BG + ";" +
      "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
      "-fx-prompt-text-fill: " + StyleKit.TEXT_MUTED + ";" +
      "-fx-border-color: " + StyleKit.BORDER + ";" +
      "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 14; -fx-font-size: 14px;";

    String focusedStyle = idleStyle.replace("-fx-border-color: " + StyleKit.BORDER, "-fx-border-color: " + StyleKit.ACCENT);

    tf.setStyle(idleStyle);
    tf.focusedProperty().addListener((obs, old, focused) -> {
      tf.setStyle(focused ? focusedStyle : idleStyle);
    });
  }

  public void show() {
    stage.setTitle("ThreadHub — Login");
    HBox layout = buildLayout();
    if (stage.getScene() != null) {
      stage.getScene().setRoot(layout);
    } else {
      Scene scene = new Scene(layout, 840, 560);
      stage.setScene(scene);
    }
    stage.setResizable(true);
    stage.setMaximized(true);
    stage.show();
  }
}