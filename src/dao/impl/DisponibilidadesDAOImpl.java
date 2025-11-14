/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.interfaces.IDisponibilidadesDAO;
import conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import modelo.entidades.Disponibilidades;

import java.sql.*;

/**
 *
 * @author chemo
 */
public class DisponibilidadesDAOImpl implements IDisponibilidadesDAO
{
    private Connection conn;

    public DisponibilidadesDAOImpl()
    {
        conn = ConexionBD.conectar();
    }

    @Override
    public boolean guardarDisponibilidades(List<Disponibilidades> lista)
    {
        String sql = "INSERT INTO disponibilidades (idProfesor, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql))
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

    public List<Disponibilidades> obtenerPorProfesor(int idProfesor)
    {
        List<Disponibilidades> lista = new ArrayList<>();
        String sql = "SELECT dia, hora_inicio, hora_fin FROM Disponibilidades WHERE idProfesor = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                String dia = rs.getString("dia");
                String horaInicio = rs.getString("hora_inicio").substring(0, 5);
                String horaFin = rs.getString("hora_fin").substring(0, 5);

                Disponibilidades d = new Disponibilidades(
                        idProfesor,
                        dia,
                        horaInicio,
                        horaFin
                );

                lista.add(d);
            }

        } catch (SQLException e)
        {
            System.err.println("Error al obtener disponibilidades: " + e.getMessage());
        }
        return lista;
    }
}

