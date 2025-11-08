package interfaz.vista;

import pruebas.controlador.HorarioControlador;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pruebas.vista.HorarioVista;

public class Main extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VtnPrincipal.fxml"));
        Scene scene = new Scene(loader.load());

        //scene.getStylesheets().add(getClass().getResource("/styles/estilo.css").toExternalForm());

        stage.setTitle("Proyecto JavaFX - Ejemplo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
