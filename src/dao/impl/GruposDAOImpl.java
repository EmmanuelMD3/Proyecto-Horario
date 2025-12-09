package dao.impl;

import modelo.entidades.Grupos;
import conexion.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GruposDAOImpl
{

    // ❌ Eliminamos el Connection de clase
    // private Connection conn;

    public GruposDAOImpl()
    {
        // ❌ Ya no necesitamos abrir conexión aquí
    }

    // ==========================================================
    // LISTAR TODOS LOS GRUPOS
    // ==========================================================
    public List<Grupos> listarTodos()
    {
        List<Grupos> lista = new ArrayList<>();

        String sql = "SELECT * FROM Grupos ORDER BY idCarrera, idSemestre, nombre";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                lista.add(new Grupos(
                        rs.getInt("idGrupo"),
                        rs.getString("nombre"),
                        rs.getInt("idCarrera"),
                        rs.getInt("idSemestre"),
                        rs.getInt("idCiclo")
                ));
            }

        } catch (SQLException e)
        {
            System.out.println("Error listarTodos Grupos: " + e.getMessage());
        }

        return lista;
    }

    // ==========================================================
    // BUSCAR GRUPO POR CARRERA + SEMESTRE
    // ==========================================================
    public Grupos buscarPorCarreraSemestre(int idCarrera, int idSemestre)
    {
        String sql = "SELECT * FROM Grupos WHERE idCarrera = ? AND idSemestre = ? LIMIT 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idCarrera);
            ps.setInt(2, idSemestre);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return new Grupos(
                        rs.getInt("idGrupo"),
                        rs.getString("nombre"),
                        rs.getInt("idCarrera"),
                        rs.getInt("idSemestre"),
                        rs.getInt("idCiclo")
                );
            }

        } catch (SQLException e)
        {
            System.err.println("Error al buscar grupo: " + e.getMessage());
        }

        return null;
    }
}
