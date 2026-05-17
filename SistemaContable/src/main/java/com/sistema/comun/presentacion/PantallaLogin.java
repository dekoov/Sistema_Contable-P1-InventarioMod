package com.sistema.comun.presentacion;

import com.sistema.comun.modelo.Usuario;
import com.sistema.comun.negocio.NegocioUsuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Date;

public class PantallaLogin {

    private NegocioUsuario negocio = new NegocioUsuario();

    public void mostrar(Stage stage) {
        stage.setTitle("Sistema Contable | Login");

        // Crear admin por defecto si no existe
        if (!negocio.existeAdministrador()) {
            Usuario admin = new Usuario();
            admin.setIdUsuario(1);
            admin.setUsername("admin");
            admin.setContrasena("admin123");
            admin.setRol("ADMIN");
            admin.setFechaCreacion(new Date());
            negocio.insertar(admin);
        }

        Label lblTitulo = new Label("SISTEMA CONTABLE");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1F4E79;");

        Label lblSubtitulo = new Label("Iniciar Sesión");
        lblSubtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #2E75B6;");

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");
        txtUsuario.setPrefWidth(250);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setPrefWidth(250);

        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setPrefWidth(250);
        btnIngresar.setStyle(
                "-fx-background-color: #1F4E79; -fx-text-fill: white; -fx-font-size: 14px;");

        VBox layout = new VBox(12,
                lblTitulo, lblSubtitulo,
                new Label("Usuario:"), txtUsuario,
                new Label("Contraseña:"), txtPassword,
                lblMensaje,
                btnIngresar
        );
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);

        // ── Evento login ──
        btnIngresar.setOnAction(e -> {
            String usr = txtUsuario.getText().trim();
            String pwd = txtPassword.getText().trim();

            if (usr.isEmpty() || pwd.isEmpty()) {
                lblMensaje.setText("Ingrese usuario y contraseña.");
                return;
            }

            Usuario u = negocio.login(usr, pwd);
            if (u != null) {
                stage.close();
                new MenuPrincipal().mostrar(new Stage(), u);
            } else {
                lblMensaje.setText("Usuario o contraseña incorrectos.");
                txtPassword.clear();
            }
        });

        txtPassword.setOnAction(e -> btnIngresar.fire());

        Scene scene = new Scene(layout, 380, 420);
        stage.setScene(scene);
        stage.show();
    }
}