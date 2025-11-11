package controlador.contLogica;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import modelo.entidades.Disponibilidades;
public class HorarioVista
{

    private final String[] dias =
    {
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    };

    private final String[][] rangos =
    {
        {
            "07:00", "08:00"
        }, 
        {
            "08:00", "09:00"
        }, 
        {
            "09:00", "10:00"
        },
        {
            "10:00", "11:00"
        }, 
        {
            "11:00", "12:00"
        }, 
        {
            "12:00", "13:00"
        },
        {
            "13:00", "14:00"
        }, 
        {
            "14:00", "15:00"
        }, 
        {
            "15:00", "16:00"
        },
        {
            "16:00", "17:00"
        }, 
        {
            "17:00", "18:00"
        }
    };

    private final GridPane grid = new GridPane();
    private final BorderPane root = new BorderPane();
    //private final Button btnGuardar = new Button("Guardar disponibilidad");

    public HorarioVista()
    {
        grid.setHgap(5);
        grid.setVgap(5);
        construirEncabezados();

        root.setCenter(grid);
        //root.setBottom(btnGuardar);
        //BorderPane.setAlignment(btnGuardar, Pos.CENTER);
    }

    private void construirEncabezados()
    {
        for (int i = 0; i < dias.length; i++)
        {
            Label diaLabel = new Label(dias[i]);
            diaLabel.setFont(Font.font(14));
            grid.add(diaLabel, i + 1, 0);
        }

        for (int i = 0; i < rangos.length; i++)
        {
            String textoRango = rangos[i][0] + " - " + rangos[i][1];
            Label horaLabel = new Label(textoRango);
            horaLabel.setFont(Font.font(14));
            grid.add(horaLabel, 0, i + 1);
        }
    }

    public BorderPane getRoot()
    {
        return root;
    }

    public GridPane getGrid()
    {
        return grid;
    }

//    public Button getBtnGuardar()
//    {
//        return btnGuardar;
//    }

    public StackPane crearCeldaVisual(Disponibilidades disp)
    {
        Rectangle rect = new Rectangle(80, 30);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.LIGHTGRAY);
        rect.setArcWidth(6);
        rect.setArcHeight(6);

        StackPane stack = new StackPane(rect);
        stack.setUserData(rect);
        return stack;
    }

    public String[][] getRangos()
    {
        return rangos;
    }

    public String[] getDias()
    {
        return dias;
    }
}
