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
    // ==========================================================
    // CONFIG (IMPORTANTE)
    // ==========================================================
    private static final int START_HOUR = 7;   // 07:00
    private static final int END_HOUR = 19;  // 19:00 (no incluido)
    private static final int SLOTS_PER_DAY = END_HOUR - START_HOUR; // 12 slots: 7..18

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
        List<TareaHorario> tareas = construirTareas();

        // 3) Configurar el asignador con reglas
        AsignadorHorario asignador = new AsignadorHorario();

        // registrar calendarios
        for (CalendarioProfesor cal : calendarios.values())
        {
            asignador.agregarCalendario(cal);
        }

        asignador.agregarRegla(new ReglaDisponibilidadProfesor());
        asignador.agregarRegla(new ReglaProfesorNoDuplicado());
        asignador.agregarRegla(new ReglaLimiteHorario());
        asignador.agregarRegla(new ReglaLimiteHorasProfesor());

// DESACTIVADAS POR AHORA
// asignador.agregarRegla(new ReglaGrupoLibre());
// asignador.agregarRegla(new ReglaEvitarDosBloquesSeguidos());

        // 4) Asignar
        asignador.asignar(tareas);

        // 5) Guardar resultado en la BD
        guardarResultados(asignador.getTareasColocadas());

        // Reporte
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

            // IMPORTANTE:
            // Tu calendario trabaja con índices 0..11 (12 slots),
            // NO con horas reales 7..18.
            for (int d = 0; d < 6; d++)
            {
                for (int slot = 0; slot < SLOTS_PER_DAY; slot++)
                {
                    cal.marcarNoDisponible(d, slot);
                }
            }

            List<Disponibilidades> disp = daoDisp.obtenerPorProfesor(prof.getIdProfesor());

            for (Disponibilidades d : disp)
            {
                int diaIndex = mapearDia(d.getDia());

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
            // "07:00" -> 7
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
    private List<TareaHorario> construirTareas()
    {
        List<TareaHorario> tareas = new ArrayList<>();

        // ============================
        // 1) CLASES (MateriasProfesor)
        // ============================
        List<MateriasProfesores> listaMatProf = daoMatProf.listarTodas();

        for (MateriasProfesores mp : listaMatProf)
        {
            Profesores profesor = daoProfesores.buscarPorId(mp.getIdProfesor());
            Materias materia = daoMaterias.buscarPorId(mp.getIdMateria());

            if (profesor == null || materia == null) continue;

            Grupos grupo = daoGrupos.buscarPorCarreraSemestre(
                    materia.getIdCarrera(),
                    materia.getIdSemestre()
            );
            if (grupo == null) continue;

            int horas = materia.getHoras_semana();
            if (horas <= 0) continue;

            // 5 -> 2 + 2 + 1
            // 4 -> 2 + 2
            // 3 -> 2 + 1 (puede quedar 1+2 luego en asignación si lo decides)
            // 2 -> 2
            // 1 -> 1
            List<Integer> bloques = new ArrayList<>();
            if (horas >= 5)
            {
                bloques.add(2);
                bloques.add(2);
                bloques.add(horas - 4); // normalmente 1
            }
            else
                if (horas == 4)
                {
                    bloques.add(2);
                    bloques.add(2);
                }
                else
                    if (horas == 3)
                    {
                        bloques.add(2);
                        bloques.add(1);
                    }
                    else
                        if (horas == 2)
                        {
                            bloques.add(2);
                        }
                        else
                        {
                            bloques.add(1);
                        }

            for (int bloque : bloques)
            {
                tareas.add(new TareaHorario(
                        TareaHorario.Tipo.CLASE,
                        bloque,
                        profesor,
                        grupo,
                        materia,
                        null
                ));
            }
        }

        // ============================
        // 2) DESCARGAS
        // ============================
        List<DescargaProfesor> listaDesc = daoDescProf.listarTodas();

        for (DescargaProfesor dp : listaDesc)
        {
            Profesores profesor = daoProfesores.buscarPorId(dp.getIdProfesor());
            Descargas descarga = daoDescargas.buscarPorId(dp.getIdDescarga());

            if (profesor == null || descarga == null) continue;

            int horas = dp.getHoras_asignadas();
            if (horas <= 0) continue;

            // Puedes dividir descarga si quieres, por ahora va como bloque total
            tareas.add(new TareaHorario(
                    TareaHorario.Tipo.DESCARGA,
                    horas,
                    profesor,
                    null,
                    null,
                    descarga
            ));
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
            String diaStr = diaToString(t.getDia());

            // OJO: t.getHoraInicio() es SLOT 0..11, hay que convertir a hora real
            int horaRealInicio = START_HOUR + t.getHoraInicio();
            int horaRealFin = horaRealInicio + t.getDuracion();

            // Seguridad (no pasarse de 19:00)
            if (horaRealInicio < START_HOUR) horaRealInicio = START_HOUR;
            if (horaRealFin > END_HOUR) horaRealFin = END_HOUR;

            if (t.getDuracion() <= 0)
                continue;

            LocalTime inicio = LocalTime.of(horaRealInicio, 0);
            LocalTime fin = LocalTime.of(horaRealFin, 0);

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
