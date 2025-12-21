package controlador.contLogica;

import dao.impl.BloquesHorarioDAOImpl;
import dao.impl.HorarioReporteDAOImpl;
import modelo.secundarias.ReporteGrupoCabecera;
import modelo.secundarias.ReporteGrupoHorario;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import modelo.secundarias.ReporteDescargas;

public class HorarioGrupalService {

    private HorarioReporteDAOImpl horarioDAO = new HorarioReporteDAOImpl();
    private BloquesHorarioDAOImpl bloquesDAO = new BloquesHorarioDAOImpl();
    private int ID_CICLO_ACTUAL = 1;

    public Map<Integer, List<ReporteGrupoCabecera>> asignacionesDetallePorCiclo() {

        List<ReporteGrupoCabecera> listaPlana = horarioDAO.asignacionesDetallePorCiclo(ID_CICLO_ACTUAL);

        if (listaPlana.isEmpty()) {
            return new HashMap<>();
        }

        // 1. Iterar y adjuntar los bloques de horario
        for (ReporteGrupoCabecera asignacion : listaPlana) {

            int idAsignacion = asignacion.getIdAsignacion();

            // Llama al DAO existente para obtener la lista de bloques
            List<ReporteGrupoHorario> bloques = bloquesDAO.obtenerBloquesPorAsignacion(idAsignacion);

            // Adjuntar la lista al objeto cabecera
            asignacion.setBloquesHorario(bloques);
        }

        // 2. Agrupar la lista completa (ahora incluye los horarios)
        return listaPlana.stream()
                .collect(Collectors.groupingBy(ReporteGrupoCabecera::getIdGrupo));
    }

    public Map<Integer, List<ReporteGrupoCabecera>> asignacionesDetallePorProfesor() {

        List<ReporteGrupoCabecera> listaPlana = horarioDAO.todasAsignacionesPorCiclo(ID_CICLO_ACTUAL);

        if (listaPlana.isEmpty()) {
            return new HashMap<>();
        }

        for (ReporteGrupoCabecera asignacion : listaPlana) {
            int idAsignacion = asignacion.getIdAsignacion();
            List<ReporteGrupoHorario> bloques = bloquesDAO.obtenerBloquesPorAsignacion(idAsignacion);
            asignacion.setBloquesHorario(bloques);
        }

        Map<Integer, List<ReporteGrupoCabecera>> mapaPorProfesor = listaPlana.stream()
                .collect(Collectors.groupingBy(ReporteGrupoCabecera::getIdProfesor));

        mapaPorProfesor.forEach((idProfesor, asignacionesDelProfe) -> {

            List<ReporteDescargas> listaDescargas = horarioDAO.obtenerDescargasPorDocente(idProfesor);

            for (ReporteDescargas desc : listaDescargas) {
                // BUSCAR LOS BLOQUES DE CADA DESCARGA (Donde el tipo en la BD es 'DESCARGA')
                List<ReporteGrupoHorario> bloquesDesc = bloquesDAO.obtenerBloquesDescarga(idProfesor, desc.getNombre());
                desc.setBloquesHorario(bloquesDesc);
            }

            for (ReporteGrupoCabecera cabecera : asignacionesDelProfe) {
                cabecera.setDescargas(listaDescargas);
            }
        });

        return mapaPorProfesor;
    }

}
