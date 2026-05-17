package com.sistema.comun.presentacion;

import com.sistema.comun.modelo.Usuario;
import com.sistema.facturacion.presentacion.PantallaCliente;
import com.sistema.facturacion.presentacion.PantallaCiudad;
import com.sistema.facturacion.presentacion.PantallaFactura;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuPrincipal {

    public void mostrar(Stage stage, Usuario usuarioActual) {
        stage.setTitle("Sistema Contable | Menú Principal");

        Label lblTitulo = new Label("SISTEMA CONTABLE");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1F4E79;");

        Label lblSubtitulo = new Label("Módulo de Facturación");
        lblSubtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #2E75B6;");

        Label lblUsuario = new Label("Usuario: " + usuarioActual.getUsername()
                + " | Rol: " + usuarioActual.getRol());
        lblUsuario.setStyle("-fx-font-size: 11px; -fx-text-fill: #888888;");

        // ── Estilo PRIMERO ──
        String estiloBtn = "-fx-pref-width: 250px; -fx-pref-height: 35px; -fx-font-size: 13px;";

        // ── Facturación ──
        Label lblFacturacion = new Label("── Facturación ──");
        lblFacturacion.setStyle("-fx-font-weight: bold; -fx-text-fill: #404040;");

        Button btnClientes   = new Button("Clientes");
        Button btnCiudades   = new Button("Ciudades de Entrega");
        Button btnFacturas   = new Button("Facturas");

        btnClientes.setStyle(estiloBtn);
        btnCiudades.setStyle(estiloBtn);
        btnFacturas.setStyle(estiloBtn);

        // ── Inventarios ──
        Label lblInventarios = new Label("── Inventarios ──");
        lblInventarios.setStyle("-fx-font-weight: bold; -fx-text-fill: #404040;");

        Button btnArticulos   = new Button("Artículos");
        Button btnMovimientos = new Button("Movimientos");

        btnArticulos.setStyle(estiloBtn + "-fx-opacity: 0.5;");
        btnMovimientos.setStyle(estiloBtn + "-fx-opacity: 0.5;");
        btnArticulos.setDisable(true);
        btnMovimientos.setDisable(true);

        // ── CxC ──
        Label lblCxc = new Label("── Cuentas por Cobrar ──");
        lblCxc.setStyle("-fx-font-weight: bold; -fx-text-fill: #404040;");

        Button btnRecibos = new Button("Recibos CxC");
        Button btnPagos   = new Button("Pagos");

        btnRecibos.setStyle(estiloBtn + "-fx-opacity: 0.5;");
        btnPagos.setStyle(estiloBtn + "-fx-opacity: 0.5;");
        btnRecibos.setDisable(true);
        btnPagos.setDisable(true);

        // ── Administración ──
        Label lblAdmin = new Label("── Administración ──");
        lblAdmin.setStyle("-fx-font-weight: bold; -fx-text-fill: #404040;");

        Button btnUsuarios = new Button("👥  Usuarios");
        btnUsuarios.setStyle(estiloBtn);

        // Solo ADMIN puede gestionar usuarios
        if (!"ADMIN".equals(usuarioActual.getRol())) {
            btnUsuarios.setDisable(true);
            btnUsuarios.setStyle(estiloBtn + "-fx-opacity: 0.5;");
        }

        // ── Salir ──
        Button btnSalir = new Button("Salir");
        btnSalir.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-pref-width: 250px;");

        // ── Layout ──
        VBox layout = new VBox(10,
                lblTitulo, lblSubtitulo, lblUsuario,
                new Label(""),
                lblFacturacion,
                btnClientes, btnCiudades, btnFacturas,
                new Label(""),
                lblInventarios,
                btnArticulos, btnMovimientos,
                new Label(""),
                lblCxc,
                btnRecibos, btnPagos,
                new Label(""),
                lblAdmin,
                btnUsuarios,
                new Label(""),
                btnSalir
        );
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        // ── Eventos ──
        btnClientes.setOnAction(e -> new PantallaCliente().mostrar(new Stage()));
        btnCiudades.setOnAction(e -> new PantallaCiudad().mostrar(new Stage()));
        btnFacturas.setOnAction(e -> new PantallaFactura().mostrar(new Stage()));
        btnUsuarios.setOnAction(e -> new PantallaUsuarios().mostrar(new Stage()));
        btnSalir.setOnAction(e -> stage.close());

        Scene scene = new Scene(layout, 350, 680);
        stage.setScene(scene);
        stage.show();
    }
}