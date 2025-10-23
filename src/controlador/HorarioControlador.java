package controlador;

import dao.DisponibilidadDAO;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import modelo.Disponibilidad;
import vista.HorarioVista;

import java.util.ArrayList;
import java.util.List;

public class HorarioControlador
{

    private final HorarioVista vista;
    private final List<Disponibilidad> seleccionadas = new ArrayList<>();
    private final int idProfesor = 1; 

    public HorarioControlador(HorarioVista vista)
    {
        this.vista = vista;
        inicializarCeldas();
        configurarBotonGuardar();
    }

    private void inicializarCeldas()
    {
        String[] dias = vista.getDias();
        String[][] rangos = vista.getRangos();
        GridPane grid = vista.getGrid();

        for (int row = 0; row < rangos.length; row++)
        {
            String horaInicio = rangos[row][0];
            String horaFin = rangos[row][1];

            for (int col = 0; col < dias.length; col++)
            {
                String dia = dias[col];
                Disponibilidad disp = new Disponibilidad(idProfesor, dia, horaInicio, horaFin);
                StackPane stack = vista.crearCeldaVisual(disp);
                javafx.scene.shape.Rectangle rect = (javafx.scene.shape.Rectangle) stack.getUserData();

                stack.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
                {
                    if (rect.getFill().equals(Color.WHITE))
                    {
                        rect.setFill(Color.web("#A5D6A7"));
                        seleccionadas.add(disp);
                    } else
                    {
                        rect.setFill(Color.WHITE);
                        seleccionadas.removeIf(d
                                -> d.getDia().equals(dia)
                                && d.getHoraInicio().equals(horaInicio)
                                && d.getHoraFin().equals(horaFin)
                        );
                    }
                });

                grid.add(stack, col + 1, row + 1);
            }
        }
    }

    private void configurarBotonGuardar()
    {
        vista.getBtnGuardar().setOnAction(e ->
        {
            if (seleccionadas.isEmpty())
            {
                mostrarAlerta("Sin selección", "No hay disponibilidades seleccionadas para guardar.", Alert.AlertType.WARNING);
            } else
            {
                DisponibilidadDAO.guardarDisponibilidades(seleccionadas);
                mostrarAlerta("Éxito", "Disponibilidades guardadas correctamente.", Alert.AlertType.INFORMATION);
                seleccionadas.clear();
            }
        });
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo)
    {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
