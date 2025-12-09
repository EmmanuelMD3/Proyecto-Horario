package generador.reglas;

import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;

public class ReglaGrupoLibre implements ReglaHorario
{

    @Override
    public boolean validar(CalendarioProfesor calendario, TareaHorario tarea, int horaInicio, int dia)
    {

        if (tarea.getGrupo() == null)
            return true;

        return calendario.estaLibreGrupo(
                tarea.getGrupo(),
                dia,
                horaInicio,
                tarea.getDuracion()
        );
    }
}
