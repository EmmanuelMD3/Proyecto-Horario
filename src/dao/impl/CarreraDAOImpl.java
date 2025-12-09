package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.Carreras;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarreraDAOImpl
{

    public CarreraDAOImpl()
    {
        // Constructor vacío: NO se almacena conexión aquí
    }

    public List<Carreras> listarCarreras()
    {
        List<Carreras> lista = new ArrayList<>();

        String sql = "SELECT idCarrera, nombre FROM Carreras ORDER BY nombre";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                lista.add(new Carreras(
                        rs.getInt("idCarrera"),
                        rs.getString("nombre")
                ));
            }

        } catch (SQLException e)
        {
            System.err.println("Error al listar carreras: " + e.getMessage());
        }

        return lista;
    }
}
