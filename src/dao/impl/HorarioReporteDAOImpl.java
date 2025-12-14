package dao.impl;

import conexion.ConexionBD;
import dao.interfaces.IHorarioReporteDAO; // Asumiendo tu interfaz
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.secundarias.ReporteGrupoCabecera;

import java.time.LocalTime;
import java.sql.Time;
import modelo.secundarias.ReporteGrupoHorario;

public class HorarioReporteDAOImpl implements IHorarioReporteDAO {

    private Connection conn;

    public HorarioReporteDAOImpl() {
        conn = ConexionBD.conectar();
    }

    String sql = """
            SELECT
                A.idAsignacion,
                G.idGrupo,
                C.nombre AS nombreCarrera,
                S.numero AS numeroSemestre,
                G.nombre AS nombreGrupo,
                M.nombre AS nombreMateria,
                M.horas_semana AS horasSemana,
                CONCAT(P.nombre, ' ', P.apellidoP, ' ', P.apellidoM) AS nombreProfesor,
                CONCAT(DATE_FORMAT(CI.fecha_inicio, '%y'), '-', DATE_FORMAT(CI.fecha_fin, '%y')) AS anioCicloConcatenado
            FROM Grupos G
            JOIN Carreras C ON G.idCarrera = C.idCarrera
            JOIN Semestres S ON G.idSemestre = S.idSemestre
            JOIN Asignaciones A ON G.idGrupo = A.idGrupo
            JOIN Materias M ON A.idMateria = M.idMateria
            JOIN Profesores P ON A.idProfesor = P.idProfesor
            JOIN ciclos CI ON G.idCiclo = CI.idCiclo
            WHERE G.idCiclo = ?
            ORDER BY
            G.idGrupo, M.nombre
            """;

    @Override
    public List<ReporteGrupoCabecera> asignacionesDetallePorCiclo(int idCiclo) {

        List<ReporteGrupoCabecera> listaDetalle = new ArrayList<>();
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCiclo);

            // Ejecutamos la consulta
            rs = ps.executeQuery();

            // Mapeo de resultados
            while (rs.next()) {

                ReporteGrupoCabecera detalle = new ReporteGrupoCabecera();

                detalle.setIdAsignacion(rs.getInt("idAsignacion"));

                detalle.setIdGrupo(rs.getInt("idGrupo"));
                detalle.setNombreCarrera(rs.getString("nombreCarrera"));
                detalle.setNumeroSemestre(rs.getInt("numeroSemestre"));

                detalle.setNombreGrupo(rs.getString("nombreGrupo"));
                detalle.setNombreMateria(rs.getString("nombreMateria"));
                detalle.setHorasSemana(rs.getInt("horasSemana"));
                detalle.setNombreProfesor(rs.getString("nombreProfesor"));

                detalle.setAnioCicloConcatenado(rs.getString("anioCicloConcatenado"));

                listaDetalle.add(detalle);
            }

        } catch (SQLException e) {
            System.err.println("ERROR obtenerAsignacionesDetallePorCiclo(): " + e.getMessage());
        }

        return listaDetalle;
    }

    @Override
    public List<ReporteGrupoHorario> obtenerBloquesPorAsignacion(int idAsignacion) {
        List<ReporteGrupoHorario> bloques = new ArrayList<>();
        ResultSet rs = null;

        String sqlBloques = """
            SELECT dia, hora_inicio, hora_fin, tipo 
            FROM Horarios 
            WHERE idAsignacion = ? 
            ORDER BY FIELD(dia, 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'), hora_inicio
        """;

        try (PreparedStatement ps = conn.prepareStatement(sqlBloques)) {
            ps.setInt(1, idAsignacion);
            rs = ps.executeQuery();

            while (rs.next()) {
                ReporteGrupoHorario bloque = new ReporteGrupoHorario();

                bloque.setDia(rs.getString("dia"));

                // Conversión de java.sql.Time a java.time.LocalTime
                Time sqlTimeInicio = rs.getTime("hora_inicio");
                if (sqlTimeInicio != null) {
                    bloque.setHoraInicio(sqlTimeInicio.toLocalTime());
                }

                Time sqlTimeFin = rs.getTime("hora_fin");
                if (sqlTimeFin != null) {
                    bloque.setHoraFin(sqlTimeFin.toLocalTime());
                }

                // El campo 'nombreBloque' (si existe en tu modelo) puede ser un identificador del aula o el tipo.
                // Aquí usamos 'tipo' como ejemplo.
                bloque.setNombreBloque(rs.getString("tipo"));

                bloques.add(bloque);
            }

        } catch (SQLException e) {
            System.err.println("ERROR obtenerBloquesPorAsignacion(): " + e.getMessage());
        }
        return bloques;
    }

}
