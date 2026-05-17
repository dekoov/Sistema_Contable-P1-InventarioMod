package com.sistema.comun.presentacion;

import com.sistema.comun.modelo.Usuario;
import com.sistema.comun.negocio.NegocioUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Date;

public class PantallaUsuarios {

    private NegocioUsuario negocio = new NegocioUsuario();

    private TextField  txtId       = new TextField();
    private TextField  txtUsername  = new TextField();
    private PasswordField txtPassword = new PasswordField();
    private ComboBox<String> cmbRol  = new ComboBox<>();
    private Label      lblMensaje   = new Label();

    private TableView<Usuario> tabla = new TableView<>();
    private ObservableList<Usuario> datos = FXCollections.observableArrayList();

    public void mostrar(Stage stage) {
        stage.setTitle("Administración de Usuarios");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(15));

        txtId.setEditable(false);
        txtId.setStyle("-fx-background-color: #f0f0f0;");
        cmbRol.setItems(FXCollections.observableArrayList("ADMIN", "USUARIO"));

        form.add(new Label("ID:"),          0, 0); form.add(txtId,       1, 0);
        form.add(new Label("Username:"),    0, 1); form.add(txtUsername,  1, 1);
        form.add(new Label("Contraseña:"),  0, 2); form.add(txtPassword,  1, 2);
        form.add(new Label("Rol:"),         0, 3); form.add(cmbRol,       1, 3);

        Button btnNuevo     = new Button("Nuevo");
        Button btnInsertar  = new Button("Insertar");
        Button btnModificar = new Button("Modificar");
        Button btnEliminar  = new Button("Eliminar");
        Button btnLimpiar   = new Button("Limpiar");

        HBox botones = new HBox(8, btnNuevo, btnInsertar, btnModificar, btnEliminar, btnLimpiar);
        botones.setPadding(new Insets(5));
        botones.setAlignment(Pos.CENTER);

        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colId.setPrefWidth(50);

        TableColumn<Usuario, String> colUser = new TableColumn<>("Username");
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUser.setPrefWidth(150);

        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colRol.setPrefWidth(100);

        tabla.getColumns().addAll(colId, colUser, colRol);
        tabla.setItems(datos);
        tabla.setPrefHeight(250);

        lblMensaje.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        VBox layout = new VBox(10, form, botones, lblMensaje, tabla);
        layout.setPadding(new Insets(10));

        // ── Eventos ──
        btnNuevo.setOnAction(e -> {
            txtId.setText(String.valueOf(negocio.obtenerSiguienteId()));
            txtUsername.clear();
            txtPassword.clear();
            cmbRol.setValue("USUARIO");
            lblMensaje.setText("");
        });

        btnInsertar.setOnAction(e -> {
            if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                lblMensaje.setText("Username y contraseña son obligatorios.");
                return;
            }
            Usuario u = new Usuario();
            u.setIdUsuario(Integer.parseInt(txtId.getText()));
            u.setUsername(txtUsername.getText());
            u.setContrasena(txtPassword.getText());
            u.setRol(cmbRol.getValue() != null ? cmbRol.getValue() : "USUARIO");
            u.setFechaCreacion(new Date());
            if (negocio.insertar(u) == 1) {
                lblMensaje.setText("Usuario creado correctamente.");
                cargarTabla();
                limpiar();
            } else {
                lblMensaje.setText("Error al crear usuario.");
            }
        });

        btnModificar.setOnAction(e -> {
            if (txtId.getText().isEmpty()) {
                lblMensaje.setText("Seleccione un usuario primero.");
                return;
            }
            Usuario u = new Usuario();
            u.setIdUsuario(Integer.parseInt(txtId.getText()));
            u.setUsername(txtUsername.getText());
            u.setContrasena(txtPassword.getText());
            u.setRol(cmbRol.getValue());
            if (negocio.modificar(u) == 1) {
                lblMensaje.setText("Usuario modificado correctamente.");
                cargarTabla();
                limpiar();
            } else {
                lblMensaje.setText("Error al modificar usuario.");
            }
        });

        btnEliminar.setOnAction(e -> {
            if (txtId.getText().isEmpty()) {
                lblMensaje.setText("Seleccione un usuario primero.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar este usuario?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(r -> {
                if (r == ButtonType.YES) {
                    if (negocio.eliminar(Integer.parseInt(txtId.getText())) == 1) {
                        lblMensaje.setText("Usuario eliminado.");
                        cargarTabla();
                        limpiar();
                    } else {
                        lblMensaje.setText("Error al eliminar usuario.");
                    }
                }
            });
        });

        btnLimpiar.setOnAction(e -> limpiar());

        tabla.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> {
                    if (newVal != null) {
                        txtId.setText(String.valueOf(newVal.getIdUsuario()));
                        txtUsername.setText(newVal.getUsername());
                        txtPassword.setText(newVal.getContrasena());
                        cmbRol.setValue(newVal.getRol());
                        lblMensaje.setText("");
                    }
                });

        cargarTabla();

        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarTabla() {
        datos.clear();
        var lista = negocio.listarTodos();
        if (lista != null) datos.addAll(lista);
    }

    private void limpiar() {
        txtId.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbRol.setValue(null);
        lblMensaje.setText("");
        tabla.getSelectionModel().clearSelection();
    }
}