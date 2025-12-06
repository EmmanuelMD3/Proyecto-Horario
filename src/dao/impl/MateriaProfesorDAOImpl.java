package dao.impl;

import conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MateriaProfesorDAOImpl
{

    private Connection conn;

    public MateriaProfesorDAOImpl()
    {
        conn = ConexionBD.conectar();
    }

    public boolean insertar(int idProfesor, int idMateria)
    {
        String sql = "INSERT INTO MateriasProfesor (idProfesor, idMateria) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            ps.setInt(2, idMateria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e)
        {
            System.out.println("Error al insertar materia profesor: " + e.getMessage());
            return false;
        }
    }

}
