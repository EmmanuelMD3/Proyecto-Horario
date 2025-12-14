package dao.impl;

import modelo.entidades.Descargas;
import conexion.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DescargasDAOImpl
{

    public DescargasDAOImpl()
    {
        // NO almacenar conexión aquí
    }

    // ======================================================
    // LISTAR DESCARGAS
    // ======================================================
    public List<Descargas> listarDescargas()
    {
        List<Descargas> lista = new ArrayList<>();

        String sql = "SELECT idDescarga, nombre FROM Descargas ORDER BY nombre";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                lista.add(new Descargas(
                        rs.getInt("idDescarga"),
                        rs.getString("nombre")
                ));
            }

        } catch (SQLException e)
        {
            System.err.println("Error listarDescargas(): " + e.getMessage());
        }

        return lista;
    }

    // ======================================================
    // BUSCAR DESCARGA POR ID
    // ======================================================
    public Descargas buscarPorId(int id)
    {

        String sql = "SELECT idDescarga, nombre FROM Descargas WHERE idDescarga = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return new Descargas(
                        rs.getInt("idDescarga"),
                        rs.getString("nombre")
                );
            }

        } catch (SQLException e)
        {
            System.err.println("Error buscarPorId Descargas: " + e.getMessage());
        }

        return null;
    }
}
