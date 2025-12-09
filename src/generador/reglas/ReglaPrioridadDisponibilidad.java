package generador.reglas;

import generador.modelo.TareaHorario;

public class ReglaPrioridadDisponibilidad
{

    public int calcularPrioridad(TareaHorario tarea)
    {

        int horas = tarea.getProfesor().getDisponibilidadTotal();

        return 100 - horas;
    }
}
