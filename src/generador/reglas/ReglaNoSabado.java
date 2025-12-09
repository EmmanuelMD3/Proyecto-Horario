package generador.reglas;

import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;

public class ReglaNoSabado implements ReglaHorario
{

    @Override
    public boolean validar(CalendarioProfesor cal, TareaHorario tarea, int horaInicio, int dia)
    {
        return dia != 5; // 5 = sábado
    }
}
