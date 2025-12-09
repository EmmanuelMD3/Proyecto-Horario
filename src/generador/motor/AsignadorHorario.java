package generador.motor;

import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;
import generador.reglas.ReglaHorario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AsignadorHorario
{

    private List<ReglaHorario> reglas = new ArrayList<>();

    // Mapa de calendarios por profesor
    private Map<Integer, CalendarioProfesor> calendarios = new HashMap<>();

    // Resultado final
    private List<TareaHorario> tareasColocadas = new ArrayList<>();
    private List<TareaHorario> tareasFallidas = new ArrayList<>();


    // ==========================================================
    // REGISTRAR REGLAS
    // ==========================================================
    public void agregarRegla(ReglaHorario r)
    {
        reglas.add(r);
    }

    public void agregarCalendario(CalendarioProfesor cal)
    {
        calendarios.put(cal.getProfesor().getIdProfesor(), cal);
    }


    // ==========================================================
    // MÉTODO PRINCIPAL QUE ASIGNA
    // ==========================================================
    public void asignar(List<TareaHorario> tareas)
    {

        for (TareaHorario tarea : tareas)
        {

            CalendarioProfesor calendario =
                    calendarios.get(tarea.getProfesor().getIdProfesor());

            boolean asignada = intentarColocar(tarea, calendario);

            if (!asignada)
            {
                tareasFallidas.add(tarea);
            }
        }
    }


    // ==========================================================
    // INTENTAR COLOCAR UNA TAREA EN TODAS LAS HORAS
    // ==========================================================
    private boolean intentarColocar(TareaHorario tarea, CalendarioProfesor calendario)
    {

        int dur = tarea.getDuracion();

        for (int dia = 0; dia < 6; dia++)
        {

            for (int hora = 7; hora <= 18 - dur; hora++)
            {

                if (validarReglas(calendario, tarea, hora, dia))
                {

                    colocar(calendario, tarea, hora, dia);
                    return true;
                }
            }
        }

        return false;
    }


    // ==========================================================
    // APLICAR TODAS LAS REGLAS
    // ==========================================================
    private boolean validarReglas(CalendarioProfesor calendario,
                                  TareaHorario tarea, int hora, int dia)
    {

        for (ReglaHorario r : reglas)
        {
            if (!r.validar(calendario, tarea, hora, dia))
                return false;
        }
        return true;
    }


    // ==========================================================
    // MARCAR OCUPACIÓN EN EL CALENDARIO DEL PROFESOR
    // ==========================================================
    private void colocar(CalendarioProfesor cal, TareaHorario tarea,
                         int horaInicio, int dia)
    {

        // Marcar ocupado en el profe
        cal.ocupar(dia, horaInicio, tarea.getDuracion());

        // Marcar ocupado para el grupo
        if (tarea.getGrupo() != null)
            cal.ocuparGrupo(tarea.getGrupo(), dia, horaInicio, tarea.getDuracion());

        // Registrar en tarea su posición final
        tarea.setDia(dia);
        tarea.setHoraInicio(horaInicio);

        tareasColocadas.add(tarea);
    }


    // ==========================================================
    // GETTERS PARA EL RESULTADO FINAL
    // ==========================================================

    public List<TareaHorario> getTareasColocadas()
    {
        return tareasColocadas;
    }

    public List<TareaHorario> getTareasFallidas()
    {
        return tareasFallidas;
    }
}
