package dao.impl;

import conexion.ConexionBD;

import java.sql.*;

public class HorariosDAOImpl
{

    public HorariosDAOImpl()
    {

    }

    public void insertarHorarioSimple(
            int idProfesor,
            String dia,
            Time horaInicio,
            Time horaFin,
            String tipo
    )
    {
        String sql = """
                INSERT INTO Horarios (idProfesor, dia, hora_inicio, hora_fin, tipo)
                VALUES (?, ?, ?, ?, ?)
                """;

        // ✔ Abrimos la conexión aquí, solo para este método
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idProfesor);
            ps.setString(2, dia);
            ps.setTime(3, horaInicio);
            ps.setTime(4, horaFin);
            ps.setString(5, tipo);

            ps.executeUpdate();

        } catch (SQLException e)
        {
            System.err.println("Error insertando horario: " + e.getMessage());
        }
    }
}
