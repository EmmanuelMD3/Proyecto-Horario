package generador.reglas;

import generador.modelo.TareaHorario;
import generador.modelo.CalendarioProfesor;

public class ReglaLimiteHorario implements ReglaHorario
{

    @Override
    public boolean validar(CalendarioProfesor calendario, TareaHorario tarea, int horaInicio, int dia)
    {

        int fin = horaInicio + tarea.getDuracion();

        // CLASES
        if (tarea.getTipo() == TareaHorario.Tipo.CLASE && fin > 17)
            return false;

        // DESCARGAS
        if (tarea.getTipo() == TareaHorario.Tipo.DESCARGA && fin > 18)
            return false;

        return true;
    }
}
