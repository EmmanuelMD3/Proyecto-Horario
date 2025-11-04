/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.interfaces.IDisponibilidadesDAO;
import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import pruebas.conexion.modelo.Disponibilidad;
import java.sql.*;

/**
 *
 * @author chemo
 */
public class DisponibilidadesDAO implements IDisponibilidadesDAO
{
    private Connection conn;

    public DisponibilidadesDAO()
    {
        conn = ConexionBD.conectar(); 
    }
    
    @Override
    public boolean guardarDisponibilidades(List<Disponibilidad> lista)
    {
        String sql = "INSERT INTO disponibilidades (idProfesor, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql))
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
            return true;

        } catch (SQLException e)
        {
            System.err.println("Error al guardar disponibilidades: " + e.getMessage());
            return false;
        }
    }
    
}
