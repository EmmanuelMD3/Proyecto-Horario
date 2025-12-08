package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.Descargas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DescargaProfesorDAOImpl
{

    private Connection conn;

    public DescargaProfesorDAOImpl()
    {
        conn = ConexionBD.conectar();
    }

    public List<Descargas> listarDescargas()
    {
        List<Descargas> lista = new ArrayList<>();

        String sql = "SELECT idDescarga, nombre FROM Descargas ORDER BY nombre";

        try (PreparedStatement ps = conn.prepareStatement(sql);
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
            System.out.println("Error listarDescargas: " + e.getMessage());
        }

        return lista;
    }

    public boolean insertar(int idProfesor, int idDescarga, int horas)
    {
        String sql = """
                INSERT INTO DescargaProfesor (idProfesor, idDescarga, horas_asignadas)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            ps.setInt(2, idDescarga);
            ps.setInt(3, horas);
            ps.executeUpdate();
            return true;

        } catch (SQLException e)
        {
            if (e.getErrorCode() == 1062)
            {
                System.out.println("Descarga duplicada, saltando...");
                return true;
            }
            System.out.println("ERROR insertar descarga: " + e.getMessage());
            return false;
        }
    }
}
