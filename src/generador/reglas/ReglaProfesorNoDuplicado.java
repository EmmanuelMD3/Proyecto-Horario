package generador.reglas;

import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;

public class ReglaProfesorNoDuplicado implements ReglaHorario
{

    @Override
    public boolean validar(CalendarioProfesor calendario, TareaHorario tarea, int horaInicio, int dia)
    {

        return calendario.estaLibre(
                dia,
                horaInicio,
                tarea.getDuracion()
        );
    }
}
