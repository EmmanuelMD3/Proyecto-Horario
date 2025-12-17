package dao.impl;

import conexion.ConexionBD;
import dao.interfaces.IHorarioReporteDAO; // Asumiendo tu interfaz
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.secundarias.ReporteDescargas;
import modelo.secundarias.ReporteGrupoCabecera;

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
    public List<ReporteGrupoCabecera> todasAsignacionesPorCiclo(int idCiclo) {

        String sqlTodasAsignaciones = """
             SELECT
                 A.idAsignacion,
                 A.idProfesor,
                 G.idGrupo,
                 C.nombre AS nombreCarrera,
                 S.numero AS numeroSemestre,
                 G.nombre AS nombreGrupo,
                 M.nombre AS nombreMateria,
                 M.horas_semana AS horasSemana,
                 CONCAT(P.apellidoP, ' ', P.apellidoM, ' ', P.nombre) AS nombreProfesor,
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
             P.idProfesor, G.idGrupo, M.nombre
             """;

        List<ReporteGrupoCabecera> listaDetalle = new ArrayList<>();
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(sqlTodasAsignaciones)) {

            ps.setInt(1, idCiclo);

            rs = ps.executeQuery();

            while (rs.next()) {

                ReporteGrupoCabecera detalle = new ReporteGrupoCabecera();

                // Mapeo de IDs
                detalle.setIdAsignacion(rs.getInt("idAsignacion"));
                detalle.setIdProfesor(rs.getInt("idProfesor"));
                detalle.setIdGrupo(rs.getInt("idGrupo"));

                // Mapeo de Cabecera y Nombres
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
            System.err.println("ERROR todasAsignacionesPorCiclo(): " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.err.println("Error al cerrar ResultSet: " + ex.getMessage());
                }
            }
        }

        return listaDetalle;
    }

    @Override
    public List<ReporteDescargas> obtenerDescargasPorDocente(int idProfesor) {
        List<ReporteDescargas> lista = new ArrayList<>();
        // Unimos la tabla de asignación (DescargaProfesor) con la de catálogo (Descargas)
        String sql = """
                SELECT 
                   d.nombre, 
                   dp.horas_asignadas 
                FROM DescargaProfesor dp
                JOIN Descargas d ON dp.idDescarga = d.idDescarga
                WHERE dp.idProfesor = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProfesor);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReporteDescargas rd = new ReporteDescargas();
                rd.setNombre(rs.getString("nombre"));
                rd.setHoras(rs.getInt("horas_asignadas"));
                lista.add(rd);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerDescargasPorDocente: " + e.getMessage());
        }
        return lista;
    }

}
