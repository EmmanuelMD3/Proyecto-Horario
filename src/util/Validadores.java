package util;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Validadores
{

    public static void aplicarFiltroSoloLetras(TextField textField)
    {
        textField.textProperty().addListener((obs, oldValue, newValue) ->
        {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*"))
            {
                textField.setText(oldValue);
            }
        });
    }

    public static void configurarCheckBoxes(CheckBox check1, CheckBox check2)
    {
        check1.selectedProperty().addListener((obs, oldValue, newValue) ->
        {
            if (newValue) check2.setSelected(false);
        });
        check2.selectedProperty().addListener((obs, oldValue, newValue) ->
        {
            if (newValue) check1.setSelected(false);
        });
    }

    public static boolean soloLetrasValidas(String texto)
    {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+");
    }

}
