package com.sistema.facturacion.presentacion;

import com.sistema.facturacion.modelo.CiudadEntrega;
import com.sistema.facturacion.negocio.NegocioCiudad;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PantallaCiudad {

    private NegocioCiudad negocio = new NegocioCiudad();

    private TextField txtId     = new TextField();
    private TextField txtNombre = new TextField();
    private Label     lblMensaje = new Label();

    private TableView<CiudadEntrega> tabla = new TableView<>();
    private ObservableList<CiudadEntrega> datos = FXCollections.observableArrayList();

    public void mostrar(Stage stage) {
        stage.setTitle("Facturación | Ciudad de Entrega");

        // ── Formulario ──
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));

        form.add(new Label("ID:"),      0, 0);
        form.add(txtId,                 1, 0);
        form.add(new Label("Nombre:"),  0, 1);
        form.add(txtNombre,             1, 1);

        txtId.setEditable(false);
        txtId.setStyle("-fx-background-color: #f0f0f0;");

        // ── Botones ──
        Button btnNuevo     = new Button("Nuevo");
        Button btnInsertar  = new Button("Insertar");
        Button btnModificar = new Button("Modificar");
        Button btnEliminar  = new Button("Eliminar");
        Button btnBuscar    = new Button("Buscar");
        Button btnLimpiar   = new Button("Limpiar");

        HBox botones = new HBox(10, btnNuevo, btnInsertar,
                btnModificar, btnEliminar, btnBuscar, btnLimpiar);
        botones.setPadding(new Insets(10));
        botones.setAlignment(Pos.CENTER);

        // ── Tabla ──
        TableColumn<CiudadEntrega, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCiudad"));
        colId.setPrefWidth(80);

        TableColumn<CiudadEntrega, String> colNombre =
                new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(300);

        tabla.getColumns().addAll(colId, colNombre);
        tabla.setItems(datos);
        tabla.setPrefHeight(250);

        // ── Mensaje ──
        lblMensaje.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        // ── Layout ──
        VBox layout = new VBox(10, form, botones, lblMensaje, tabla);
        layout.setPadding(new Insets(10));

        // ── Eventos ──
        btnNuevo.setOnAction(e -> {
            txtId.setText(String.valueOf(negocio.obtenerSiguienteId()));
            txtNombre.clear();
            lblMensaje.setText("");
        });

        btnInsertar.setOnAction(e -> {
            if (txtNombre.getText().isEmpty()) {
                lblMensaje.setText("El nombre es obligatorio.");
                return;
            }
            CiudadEntrega c = new CiudadEntrega();
            c.setIdCiudad(Integer.parseInt(txtId.getText()));
            c.setNombre(txtNombre.getText());
            if (negocio.insertar(c) == 1) {
                lblMensaje.setText("Ciudad insertada correctamente.");
                cargarTabla();
                limpiar();
            } else {
                lblMensaje.setText("Error al insertar ciudad.");
            }
        });

        btnModificar.setOnAction(e -> {
            if (txtId.getText().isEmpty()) {
                lblMensaje.setText("Seleccione una ciudad primero.");
                return;
            }
            CiudadEntrega c = new CiudadEntrega();
            c.setIdCiudad(Integer.parseInt(txtId.getText()));
            c.setNombre(txtNombre.getText());
            if (negocio.modificar(c) == 1) {
                lblMensaje.setText("Ciudad modificada correctamente.");
                cargarTabla();
                limpiar();
            } else {
                lblMensaje.setText("Error al modificar ciudad.");
            }
        });

        btnEliminar.setOnAction(e -> {
            if (txtId.getText().isEmpty()) {
                lblMensaje.setText("Seleccione una ciudad primero.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar esta ciudad?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    if (negocio.eliminar(Integer.parseInt(txtId.getText())) == 1) {
                        lblMensaje.setText("Ciudad eliminada correctamente.");
                        cargarTabla();
                        limpiar();
                    } else {
                        lblMensaje.setText("Error al eliminar ciudad.");
                    }
                }
            });
        });

        btnBuscar.setOnAction(e -> {
            if (txtId.getText().isEmpty()) {
                lblMensaje.setText("Ingrese un ID para buscar.");
                return;
            }
            CiudadEntrega c = negocio.buscar(Integer.parseInt(txtId.getText()));
            if (c != null) {
                txtNombre.setText(c.getNombre());
                lblMensaje.setText("Ciudad encontrada.");
            } else {
                lblMensaje.setText("Ciudad no encontrada.");
            }
        });

        btnLimpiar.setOnAction(e -> limpiar());

        tabla.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        txtId.setText(String.valueOf(newVal.getIdCiudad()));
                        txtNombre.setText(newVal.getNombre());
                        lblMensaje.setText("");
                    }
                });

        cargarTabla();

        Scene scene = new Scene(layout, 500, 450);
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
        txtNombre.clear();
        lblMensaje.setText("");
        tabla.getSelectionModel().clearSelection();
    }
}