package dao.impl;

import dao.interfaces.IDisponibilidadesDAO;
import conexion.ConexionBD;
import modelo.entidades.Disponibilidades;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisponibilidadesDAOImpl
{

    public DisponibilidadesDAOImpl()
    {
        // NO almacenar Connection aquí
    }

    // ==============================================================
    // GUARDAR DISPONIBILIDADES (INSERT BATCH)

    public boolean guardarDisponibilidades(List<Disponibilidades> lista)
    {

        String sql = "INSERT INTO Disponibilidades (idProfesor, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            for (Disponibilidades d : lista)
            {
                stmt.setInt(1, d.getIdProfesor());
                stmt.setString(2, d.getDia());
                stmt.setString(3, d.getHoraInicio());
                stmt.setString(4, d.getHoraFin());
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Disponibilidades guardadas correctamente.");
            return true;

        } catch (SQLException e)
        {
            System.err.println("Error al guardar disponibilidades: " + e.getMessage());
            return false;
        }
    }

    // ==============================================================
    // OBTENER DISPONIBILIDAD POR PROFESOR

    public List<Disponibilidades> obtenerPorProfesor(int idProfesor)
    {

        List<Disponibilidades> lista = new ArrayList<>();
        String sql = "SELECT dia, hora_inicio, hora_fin FROM Disponibilidades WHERE idProfesor = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idProfesor);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {

                // Convertimos TIME → "07:00"
                String horaInicio = rs.getString("hora_inicio").substring(0, 5);
                String horaFin = rs.getString("hora_fin").substring(0, 5);

                lista.add(new Disponibilidades(
                        idProfesor,
                        rs.getString("dia"),
                        horaInicio,
                        horaFin
                ));
            }

        } catch (SQLException e)
        {
            System.err.println("Error al obtener disponibilidades: " + e.getMessage());
        }

        return lista;
    }
}
