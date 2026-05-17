package com.sistema;

import com.sistema.comun.presentacion.PantallaLogin;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        new PantallaLogin().mostrar(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}