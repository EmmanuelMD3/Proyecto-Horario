package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.Materias;
import modelo.secundarias.MateriasProfesorTemp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MateriaProfesorDAOImpl
{

    private Connection conn;

    public MateriaProfesorDAOImpl()
    {
        conn = ConexionBD.conectar();
    }


    public List<MateriasProfesorTemp> listarMateriasPorProfesor(int idProfesor)
    {
        List<MateriasProfesorTemp> lista = new ArrayList<>();

        String sql = """
                SELECT 
                    m.idMateria,
                    m.nombre AS materiaNombre,
                    c.nombre AS carreraNombre,
                    s.numero AS semestreNumero
                FROM materiasprofesor mp
                JOIN materias m ON mp.idMateria = m.idMateria
                JOIN carreras c ON m.idCarrera = c.idCarrera
                JOIN semestres s ON m.idSemestre = s.idSemestre
                WHERE mp.idProfesor = ?
                ORDER BY s.numero, m.nombre
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                lista.add(new MateriasProfesorTemp(
                        rs.getInt("idMateria"),
                        rs.getString("materiaNombre"),
                        rs.getString("carreraNombre"),
                        rs.getInt("semestreNumero")
                ));
            }

        } catch (SQLException e)
        {
            System.out.println("ERROR listarMateriasPorProfesor(): " + e.getMessage());
        }

        return lista;
    }


    public boolean insertar(int idProfesor, int idMateria)
    {
        String sql = "INSERT INTO materiasprofesor (idProfesor, idMateria) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            ps.setInt(2, idMateria);

            ps.executeUpdate();
            return true;

        } catch (SQLException e)
        {
            if (e.getErrorCode() == 1062)
            {
                System.out.println("Materia duplicada para profesor, se omite insert.");
                return true;
            }

            System.out.println("ERROR insertar() MateriaProfesor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idProfesor, int idMateria)
    {
        String sql = "DELETE FROM materiasprofesor WHERE idProfesor = ? AND idMateria = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            ps.setInt(2, idMateria);

            return ps.executeUpdate() > 0;

        } catch (SQLException e)
        {
            System.out.println("ERROR eliminar MateriaProfesor: " + e.getMessage());
            return false;
        }
    }



}
