package dao;

import conexion.Conexion;
import java.sql.Connection;
import modelo.Disponibilidad;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.List;


public class DisponibilidadDAO
{


    public static void guardarDisponibilidades(List<Disponibilidad> lista)
    {
        String sql = "INSERT INTO disponibilidad (idProfesor, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql))
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
            e.printStackTrace();
        }
    }
}
