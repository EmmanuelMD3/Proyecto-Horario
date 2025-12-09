package generador.motor;

import dao.impl.*;
import generador.modelo.CalendarioProfesor;
import generador.modelo.TareaHorario;
import generador.reglas.*;
import modelo.entidades.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public class GeneradorHorarios
{

    // ==============
    // DAOs
    // ==============
    private final ProfesorDAOImpl daoProfesores = new ProfesorDAOImpl();
    private final DisponibilidadesDAOImpl daoDisp = new DisponibilidadesDAOImpl();
    private final MateriaProfesorDAOImpl daoMatProf = new MateriaProfesorDAOImpl();
    private final MateriaDAOImpl daoMaterias = new MateriaDAOImpl();
    private final GruposDAOImpl daoGrupos = new GruposDAOImpl();
    private final DescargaProfesorDAOImpl daoDescProf = new DescargaProfesorDAOImpl();
    private final DescargasDAOImpl daoDescargas = new DescargasDAOImpl();
    private final HorariosDAOImpl daoHorarios = new HorariosDAOImpl();

    // =========================
    // MÉTODO PÚBLICO PRINCIPAL
    // =========================
    public void generarHorariosAutomaticos()
    {
        // 1) Construir calendarios por profesor con su disponibilidad
        Map<Integer, CalendarioProfesor> calendarios = construirCalendariosProfesores();

        // 2) Construir todas las tareas (materias + descargas)
        List<TareaHorario> tareas = construirTareas(calendarios);

        // 3) Configurar el asignador con reglas
        AsignadorHorario asignador = new AsignadorHorario();

        // registrar calendarios
        for (CalendarioProfesor cal : calendarios.values())
        {
            asignador.agregarCalendario(cal);
        }

        // registrar reglas de validación
        asignador.agregarRegla(new ReglaDisponibilidadProfesor());
        asignador.agregarRegla(new ReglaProfesorNoDuplicado());
        asignador.agregarRegla(new ReglaGrupoLibre());
        asignador.agregarRegla(new ReglaLimiteHorario());
        asignador.agregarRegla(new ReglaLimiteHorasProfesor());
        asignador.agregarRegla(new ReglaEvitarDosBloquesSeguidos());
        //asignador.agregarRegla(new ReglaNoSabado());
        // ReglaDuracionBloque es redundante si ya usas estaLibre en otras

        // 4) Asignar
        asignador.asignar(tareas);

        // 5) Guardar resultado en la BD
        guardarResultados(asignador.getTareasColocadas());

        // Opcional: revisar tareas que no pudieron colocarse
        if (!asignador.getTareasFallidas().isEmpty())
        {
            System.out.println("Tareas NO asignadas: " + asignador.getTareasFallidas().size());
            for (TareaHorario t : asignador.getTareasFallidas())
            {
                System.out.println(" - Profesor " + t.getProfesor().getIdProfesor()
                        + " - Materia " + (t.getMateria() != null ? t.getMateria().getNombre() : "DESCARGA"));
            }
        }
    }

    // ==========================================================
    // 1) CONSTRUIR CALENDARIOS DE PROFESORES
    // ==========================================================
    private Map<Integer, CalendarioProfesor> construirCalendariosProfesores()
    {
        Map<Integer, CalendarioProfesor> mapa = new HashMap<>();

        List<Profesores> listaProf = daoProfesores.listarProfesores();

        for (Profesores prof : listaProf)
        {
            CalendarioProfesor cal = new CalendarioProfesor(prof);

            // Primero marcamos TODO como no disponible, para usar solo lo que hay en la BD
            for (int d = 0; d < 6; d++)
            {
                for (int h = 7; h < 19; h++)   // 7–18
                {
                    cal.marcarNoDisponible(d, h);
                }
            }

            List<Disponibilidades> disp = daoDisp.obtenerPorProfesor(prof.getIdProfesor());

            for (Disponibilidades d : disp)
            {
                int diaIndex = mapearDia(d.getDia()); // "Lunes" -> 0, etc.

                int hIni = horaToInt(d.getHoraInicio());
                int hFin = horaToInt(d.getHoraFin());

                for (int h = hIni; h < hFin; h++)
                {
                    cal.marcarDisponible(diaIndex, h);
                }
            }

            mapa.put(prof.getIdProfesor(), cal);
        }

        return mapa;
    }

    private int mapearDia(String dia)
    {
        switch (dia)
        {
            case "Lunes":
                return 0;
            case "Martes":
                return 1;
            case "Miercoles":
            case "Miércoles":
                return 2;
            case "Jueves":
                return 3;
            case "Viernes":
                return 4;
            case "Sabado":
            case "Sábado":
                return 5;
            default:
                return 0;
        }
    }

    private int horaToInt(String horaStr)
    {
        try
        {
            return Integer.parseInt(horaStr.substring(0, 2));
        } catch (Exception e)
        {
            System.err.println("Error convirtiendo hora: " + horaStr);
            return -1;
        }
    }

    // ==========================================================
    // 2) CONSTRUIR TAREAS (CLASES + DESCARGAS)
    // ==========================================================
    private List<TareaHorario> construirTareas(Map<Integer, CalendarioProfesor> calendarios)
    {
        List<TareaHorario> tareas = new ArrayList<>();

        List<MateriasProfesores> listaMatProf = daoMatProf.listarTodas();
        for (MateriasProfesores mp : listaMatProf)
        {
            Profesores profesor = daoProfesores.buscarPorId(mp.getIdProfesor());
            Materias materia = daoMaterias.buscarPorId(mp.getIdMateria());

            if (profesor == null || materia == null)
                continue;

            // 1 solo grupo por carrera + semestre (tu diseño)
            Grupos grupo = daoGrupos.buscarPorCarreraSemestre(
                    materia.getIdCarrera(),
                    materia.getIdSemestre()
            );

            // Si no hay grupo, seguimos, pero puedes marcarlo como error
            if (grupo == null)
                continue;

            int horas = materia.getHoras_semana();

            // Aquí podrías descomponer en bloques de 2 + 1 si quieres,
            // de momento creamos una tarea con la duración total.
            TareaHorario tarea = new TareaHorario(
                    TareaHorario.Tipo.CLASE,
                    horas,
                    profesor,
                    grupo,
                    materia,
                    null
            );

            tareas.add(tarea);
        }

        // 2.2 Horas de descarga
        List<DescargaProfesor> listaDesc = daoDescProf.listarTodas();

        for (DescargaProfesor dp : listaDesc)
        {
            Profesores profesor = daoProfesores.buscarPorId(dp.getIdProfesor());
            Descargas descarga = daoDescargas.buscarPorId(dp.getIdDescarga());

            if (profesor == null || descarga == null)
                continue;

            int horas = dp.getHoras_asignadas();

            TareaHorario tarea = new TareaHorario(
                    TareaHorario.Tipo.DESCARGA,
                    horas,
                    profesor,
                    null,   // sin grupo
                    null,   // sin materia
                    descarga
            );

            tareas.add(tarea);
        }

        return tareas;
    }

    // ==========================================================
    // 3) GUARDAR RESULTADO EN LA TABLA HORARIOS
    // ==========================================================
    private void guardarResultados(List<TareaHorario> tareasColocadas)
    {
        for (TareaHorario t : tareasColocadas)
        {
            // Convertir int dia -> String "Lunes", etc.
            String diaStr = diaToString(t.getDia());

            LocalTime inicio = LocalTime.of(t.getHoraInicio(), 0);
            LocalTime fin = inicio.plusHours(t.getDuracion());

            Time sqlInicio = Time.valueOf(inicio);
            Time sqlFin = Time.valueOf(fin);

            String tipo = (t.getTipo() == TareaHorario.Tipo.CLASE) ? "CLASE" : "DESCARGA";

            daoHorarios.insertarHorarioSimple(
                    t.getProfesor().getIdProfesor(),
                    diaStr,
                    sqlInicio,
                    sqlFin,
                    tipo
            );
        }
    }

    private String diaToString(int dia)
    {
        switch (dia)
        {
            case 0:
                return "Lunes";
            case 1:
                return "Martes";
            case 2:
                return "Miercoles";
            case 3:
                return "Jueves";
            case 4:
                return "Viernes";
            case 5:
                return "Sabado";
            default:
                return "Lunes";
        }
    }
}
