package dao.impl;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import modelo.secundarias.ReporteGrupoHorario;

public class BloquesHorarioDAOImpl {

    private Connection conn;

    public BloquesHorarioDAOImpl() {
        conn = ConexionBD.conectar();
    }
    
    
    String sql="""
            SELECT
                H.dia,
                H.hora_inicio,
                H.hora_fin,
            CASE
                WHEN H.tipo = 'CLASE' THEN M.nombre
                WHEN H.tipo = 'DESCARGA' THEN D.nombre
                ELSE 'Bloque Desconocido'
            END AS nombreBloque
            FROM Horarios H
                LEFT JOIN Asignaciones A ON H.idAsignacion = A.idAsignacion
                LEFT JOIN Materias M ON A.idMateria = M.idMateria
                LEFT JOIN DescargaProfesor DP ON H.idProfesor = DP.idProfesor AND H.tipo = 'DESCARGA'
                LEFT JOIN Descargas D ON DP.idDescarga = D.idDescarga
            WHERE A.idGrupo = ? OR
                (H.tipo = 'DESCARGA' 
            AND H.idProfesor IN (SELECT idProfesor FROM Asignaciones WHERE idGrupo = ?))
            ORDER BY H.hora_inicio, H.dia
            """;

    public List<ReporteGrupoHorario> bloquesHorariosPorGrupo(int idGrupo) {

        List<ReporteGrupoHorario> listaBloques = new ArrayList<>();
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 1. Establecer el parámetro del grupo (usado dos veces en el WHERE)
            ps.setInt(1, idGrupo);
            ps.setInt(2, idGrupo);

            rs = ps.executeQuery();

            while (rs.next()) {

                // Convertir java.sql.Time a java.time.LocalTime (Práctica recomendada en Java moderno)
                Time sqlTimeInicio = rs.getTime("hora_inicio");
                Time sqlTimeFin = rs.getTime("hora_fin");

                LocalTime horaInicio = (sqlTimeInicio != null) ? sqlTimeInicio.toLocalTime() : null;
                LocalTime horaFin = (sqlTimeFin != null) ? sqlTimeFin.toLocalTime() : null;

                // Mapear al modelo ReporteBloqueHorario usando Setters
                ReporteGrupoHorario bloque = new ReporteGrupoHorario();

                bloque.setDia(rs.getString("dia"));
                bloque.setHoraInicio(horaInicio);
                bloque.setHoraFin(horaFin);
                bloque.setNombreBloque(rs.getString("nombreBloque")); // Contenido de la celda

                listaBloques.add(bloque);
            }

        } catch (SQLException e) {
            System.err.println("ERROR obtenerBloquesHorariosPorGrupo(): " + e.getMessage());
        }

        return listaBloques;
    }
}
