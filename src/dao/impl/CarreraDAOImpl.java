package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.Carreras;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarreraDAOImpl
{

    private Connection conn;

    public CarreraDAOImpl()
    {
        conn = ConexionBD.conectar();
    }


    public List<Carreras> listarCarreras()
    {
        List<Carreras> lista = new ArrayList<>();

        String sql = "SELECT idCarrera, nombre FROM Carreras ORDER BY nombre";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                Carreras carrera = new Carreras(
                        rs.getInt("idCarrera"),
                        rs.getString("nombre")
                );
                lista.add(carrera);
            }

        } catch (SQLException e)
        {
            System.out.println("Error al listar carreras: " + e.getMessage());
        }

        return lista;
    }
}
