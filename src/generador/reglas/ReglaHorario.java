package generador.reglas;

import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;

public interface ReglaHorario
{

    /**
     * Valida si se puede colocar una tarea en un día y hora.
     *
     * @param calendario calendario del profesor
     * @param tarea      tarea a colocar
     * @param horaInicio hora donde quiere colocarse
     * @param dia        día 0= Lunes ... 5 = Sábado
     * @return true si es válido, false si la regla prohíbe esta asignación
     */
    boolean validar(CalendarioProfesor calendario, TareaHorario tarea, int horaInicio, int dia);
}
