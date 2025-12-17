package dao.impl;

import conexion.ConexionBD;
import dao.interfaces.IBloquesHorarioDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import modelo.secundarias.ReporteGrupoHorario;

public class BloquesHorarioDAOImpl implements IBloquesHorarioDAO{

    private Connection conn;

    public BloquesHorarioDAOImpl() {
        conn = ConexionBD.conectar();
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

                Time sqlTimeInicio = rs.getTime("hora_inicio");
                if (sqlTimeInicio != null) {
                    bloque.setHoraInicio(sqlTimeInicio.toLocalTime());
                }

                Time sqlTimeFin = rs.getTime("hora_fin");
                if (sqlTimeFin != null) {
                    bloque.setHoraFin(sqlTimeFin.toLocalTime());
                }

                bloque.setNombreBloque(rs.getString("tipo"));

                bloques.add(bloque);
            }

        } catch (SQLException e) {
            System.err.println("ERROR obtenerBloquesPorAsignacion(): " + e.getMessage());
        }
        return bloques;
    }
}
