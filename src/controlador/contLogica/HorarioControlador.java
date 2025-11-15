package controlador.contLogica;

import dao.impl.DisponibilidadesDAOImpl;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import modelo.entidades.Disponibilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorarioControlador
{
    private final Map<String, StackPane> mapaCeldas = new HashMap<>();

    private final HorarioVista vista;
    private final List<Disponibilidades> seleccionadas = new ArrayList<>();
    private boolean bloqueoEdicion = false;

    public void setBloqueoEdicion(boolean bloqueo)
    {
        this.bloqueoEdicion = bloqueo;
    }


    public HorarioControlador(HorarioVista vista)
    {
        this.vista = vista;
        inicializarCeldas();
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

                StackPane stack = vista.crearCeldaVisual(null);
                Rectangle rect = (Rectangle) stack.getUserData();

                String clave = generarClave(dia, horaInicio, horaFin);
                mapaCeldas.put(clave, stack);

                System.out.println("Clave generada: " + clave);

                stack.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
                {
                    if (bloqueoEdicion)
                    {
                        return;
                    }

                    if (rect.getFill().equals(Color.WHITE))
                    {
                        rect.setFill(Color.web("#A5D6A7"));

                        seleccionadas.add(new Disponibilidades(
                                0, dia, horaInicio, horaFin
                        ));
                    }
                    else
                    {
                        rect.setFill(Color.WHITE);

                        seleccionadas.removeIf(d ->
                                d.getDia().equals(dia) &&
                                        d.getHoraInicio().equals(horaInicio) &&
                                        d.getHoraFin().equals(horaFin)
                        );
                    }
                });

                grid.add(stack, col + 1, row + 1);
            }
        }
    }

    private String normalizarHora(String h)
    {
        if (h == null) return "";
        if (h.length() >= 5) return h.substring(0, 5);
        return h;
    }

    private String generarClave(String dia, String inicio, String fin)
    {
        return dia + "-" + normalizarHora(inicio) + "-" + normalizarHora(fin);
    }

    public void guardarSeleccionadas(int idProfesor)
    {

        if (seleccionadas.isEmpty())
        {
            mostrarAlerta("Sin selección", "No hay disponibilidades seleccionadas para guardar.",
                    Alert.AlertType.WARNING);
            return;
        }

        seleccionadas.forEach(d -> d.setIdProfesor(idProfesor));

        new DisponibilidadesDAOImpl().guardarDisponibilidades(seleccionadas);

        mostrarAlerta("Éxito", "Disponibilidad guardada correctamente para el profesor.",
                Alert.AlertType.INFORMATION);

        seleccionadas.clear();
    }

    public void mostrarDisponibilidadesProfesor(List<Disponibilidades> lista)
    {


        mapaCeldas.values().forEach(stack ->
        {
            Rectangle r = (Rectangle) stack.getUserData();
            r.setFill(Color.WHITE);
        });


        for (Disponibilidades d : lista)
        {

            String clave = generarClave(d.getDia(), d.getHoraInicio(), d.getHoraFin());
            System.out.println("Clave BD normalizada: " + clave);

            StackPane celda = mapaCeldas.get(clave);

            if (celda != null)
            {
                Rectangle rect = (Rectangle) celda.getUserData();
                rect.setFill(Color.web("#A5D6A7"));
            }
        }
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
