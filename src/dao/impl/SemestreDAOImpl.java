package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.Semestres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SemestreDAOImpl
{

    private Connection conn;

    public SemestreDAOImpl()
    {
        conn = ConexionBD.conectar();
    }

    public List<Semestres> listarTodos()
    {
        List<Semestres> lista = new ArrayList<>();

        String sql = "SELECT idSemestre, numero FROM Semestres ORDER BY numero";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                Semestres s = new Semestres(
                        rs.getInt("idSemestre"),
                        rs.getInt("numero")
                );
                lista.add(s);
            }

            System.out.println("[SemestreDAOImpl] Semestres encontrados: " + lista.size());

        } catch (SQLException e)
        {
            System.out.println("Error al listar semestres: " + e.getMessage());
        }
        return lista;
    }
}
