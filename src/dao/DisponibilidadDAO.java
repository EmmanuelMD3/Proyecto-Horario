package dao;

import conexion.Conexion;
import modelo.Disponibilidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DisponibilidadDAO
{

    public static void guardarDisponibilidades(List<Disponibilidad> lista)
    {
        String sql = "INSERT INTO disponibilidades (idProfesor, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql))
        {

            for (Disponibilidad d : lista)
            {
                stmt.setInt(1, d.getIdProfesor());
                stmt.setString(2, d.getDia());
                stmt.setString(3, d.getHoraInicio());
                stmt.setString(4, d.getHoraFin());
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Disponibilidades guardadas correctamente.");

        } catch (SQLException e)
        {
            System.err.println("Error al guardar disponibilidades: " + e.getMessage());
        }
    }
}
