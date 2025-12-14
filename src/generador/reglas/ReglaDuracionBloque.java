package generador.reglas;

import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;

public class ReglaDuracionBloque implements ReglaHorario
{

    @Override
    public boolean validar(CalendarioProfesor calendario, TareaHorario tarea, int horaInicio, int dia)
    {

        int dur = tarea.getDuracion();

        // Nada especial que prohibir, sólo validar que haya espacio libre
        return calendario.estaLibre(dia, horaInicio, dur);
    }
}
