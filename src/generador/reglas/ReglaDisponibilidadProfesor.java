package generador.reglas;

import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;

public class ReglaDisponibilidadProfesor implements ReglaHorario
{

    @Override
    public boolean validar(CalendarioProfesor calendario, TareaHorario tarea, int horaInicio, int dia)
    {

        return calendario.estaDisponible(
                dia,
                horaInicio,
                tarea.getDuracion()
        );
    }
}
