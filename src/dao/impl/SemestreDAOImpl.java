package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.Semestres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SemestreDAOImpl
{

    public SemestreDAOImpl()
    {
        // ❌ No almacenar conexiones aquí
    }

    public List<Semestres> listarTodos()
    {
        List<Semestres> lista = new ArrayList<>();

        String sql = "SELECT idSemestre, numero FROM Semestres ORDER BY numero";

        // ✔ Abrimos conexión dentro del método
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                lista.add(new Semestres(
                        rs.getInt("idSemestre"),
                        rs.getInt("numero")
                ));
            }

            System.out.println("[SemestreDAOImpl] Semestres encontrados: " + lista.size());

        } catch (SQLException e)
        {
            System.out.println("Error al listar semestres: " + e.getMessage());
        }

        return lista;
    }
}
