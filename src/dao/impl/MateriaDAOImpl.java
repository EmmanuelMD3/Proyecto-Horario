package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.Materias;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAOImpl
{

    private Connection conn;

    public MateriaDAOImpl()
    {
        conn = ConexionBD.conectar();
    }

    public List<Materias> listarPorSemestreYCarrera(int idSemestre, int idCarrera)
    {
        List<Materias> lista = new ArrayList<>();

        String sql = """
            SELECT m.idMateria, m.nombre, m.horas_semana 
            FROM Materias m
            JOIN Semestres s ON m.idSemestre = s.idSemestre
            WHERE m.idSemestre = ? AND s.idCarrera = ?
            ORDER BY m.nombre
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idSemestre);
            ps.setInt(2, idCarrera);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Materias mat = new Materias(
                        rs.getInt("idMateria"),
                        rs.getString("nombre"),
                        rs.getInt("horas_semana")
                );
                lista.add(mat);
            }

        } catch (SQLException e)
        {
            System.out.println("Error al listar materias: " + e.getMessage());
        }
        return lista;
    }
}
