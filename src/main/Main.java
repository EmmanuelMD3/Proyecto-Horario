package main;

import controlador.HorarioControlador;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vista.HorarioVista;

public class Main extends Application
{

    @Override
    public void start(Stage stage)
    {
        HorarioVista vista = new HorarioVista();
        new HorarioControlador(vista);

        Scene scene = new Scene(vista.getRoot(), 800, 600);
        stage.setTitle("Horario de Disponibilidad - MVC + DAO");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
