package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.Materias;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAOImpl
{

    public MateriaDAOImpl()
    {

    }

    public List<Materias> listarPorSemestreYCarrera(int idSemestre, int idCarrera)
    {
        List<Materias> lista = new ArrayList<>();

        String sql = """
                SELECT idMateria, nombre, horas_semana
                FROM Materias
                WHERE idSemestre = ? AND idCarrera = ?
                ORDER BY nombre
                """;

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idSemestre);
            ps.setInt(2, idCarrera);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                lista.add(new Materias(
                        rs.getInt("idMateria"),
                        rs.getString("nombre"),
                        rs.getInt("horas_semana")
                ));
            }

            System.out.println("[MateriaDAOImpl] Materias encontradas: " + lista.size());

        } catch (SQLException e)
        {
            System.out.println("Error al listar materias: " + e.getMessage());
        }

        return lista;
    }

    public Materias buscarPorId(int idMateria)
    {
        Materias mat = null;

        String sql = "SELECT * FROM Materias WHERE idMateria = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idMateria);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                mat = new Materias(
                        rs.getInt("idMateria"),
                        rs.getString("nombre"),
                        rs.getInt("idCarrera"),
                        rs.getInt("idSemestre"),
                        rs.getInt("horas_semana")
                );
            }

        } catch (SQLException e)
        {
            System.out.println("Error al buscar materia por ID: " + e.getMessage());
        }

        return mat;
    }
}
