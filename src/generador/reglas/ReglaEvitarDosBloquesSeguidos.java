package generador.reglas;

import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;

public class ReglaEvitarDosBloquesSeguidos implements ReglaHorario
{

    @Override
    public boolean validar(CalendarioProfesor calendario, TareaHorario tarea, int horaInicio, int dia)
    {

        if (tarea.getGrupo() == null)
            return true;

        // evitar bloques pegados
        return !calendario.existeBloquePegado(
                tarea.getGrupo(),
                dia,
                horaInicio
        );
    }
}
