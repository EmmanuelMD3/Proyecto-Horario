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

    public List<Semestres> listarPorCarrera(int idCarrera)
    {
        List<Semestres> lista = new ArrayList<>();

        String sql = "SELECT idSemestre, numero FROM Semestres WHERE idCarrera = ? ORDER BY numero";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCarrera);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Semestres s = new Semestres(
                        rs.getInt("idSemestre"),
                        rs.getInt("numero")
                );
                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar semestres: " + e.getMessage());
        }

        return lista;
    }
}
