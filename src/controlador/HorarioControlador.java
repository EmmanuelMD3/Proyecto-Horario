package controlador;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import modelo.Disponibilidad;
import vista.HorarioVista;

public class HorarioControlador
{

    private final HorarioVista vista;

    public HorarioControlador(HorarioVista vista)
    {
        this.vista = vista;
        inicializarCeldas();
    }

    private void inicializarCeldas()
    {
        String[] dias =
        {
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
        };
        String[] horas =
        {
            "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"
        };

        GridPane grid = vista.getGrid();

        for (int row = 0; row < horas.length; row++)
        {
            for (int col = 0; col < dias.length; col++)
            {
                Disponibilidad celda = new Disponibilidad(dias[col], horas[row]);
                StackPane stack = vista.crearCeldaVisual(celda);

                stack.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
                {
                    celda.alternarSeleccion();
                    javafx.scene.shape.Rectangle rect = (javafx.scene.shape.Rectangle) stack.getUserData();
                    if (celda.isSeleccionada())
                    {
                        rect.setFill(Color.web("#A5D6A7"));
                    } else
                    {
                        rect.setFill(Color.WHITE);
                    }
                });

                grid.add(stack, col + 1, row + 1);
            }
        }
    }
}
