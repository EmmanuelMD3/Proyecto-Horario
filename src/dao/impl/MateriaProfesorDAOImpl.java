package dao.impl;

import conexion.ConexionBD;
import modelo.entidades.MateriasProfesores;
import modelo.secundarias.MateriasProfesorTemp;
import modelo.entidades.Profesores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaProfesorDAOImpl
{

    public MateriaProfesorDAOImpl()
    {

    }

    // ===============================
    // LISTAR MATERIAS DE UN PROFESOR
    // ===============================
    public List<MateriasProfesorTemp> listarMateriasPorProfesor(int idProfesor)
    {
        List<MateriasProfesorTemp> lista = new ArrayList<>();

        String sql = """
                SELECT 
                    m.idMateria,
                    m.nombre AS materiaNombre,
                    c.nombre AS carreraNombre,
                    s.numero AS semestreNumero
                FROM MateriasProfesor mp
                JOIN Materias m ON mp.idMateria = m.idMateria
                JOIN Carreras c ON m.idCarrera = c.idCarrera
                JOIN Semestres s ON m.idSemestre = s.idSemestre
                WHERE mp.idProfesor = ?
                ORDER BY s.numero, m.nombre
                """;

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
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

    // ===============================
    // INSERTAR
    // ===============================
    public boolean insertar(int idProfesor, int idMateria)
    {
        String sql = "INSERT INTO MateriasProfesor (idProfesor, idMateria) VALUES (?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
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

    // ===============================
    // ELIMINAR
    // ===============================
    public boolean eliminar(int idProfesor, int idMateria)
    {
        String sql = "DELETE FROM MateriasProfesor WHERE idProfesor = ? AND idMateria = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
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

    // ======================================
    // BUSCAR PROFESORES ASIGNADOS A MATERIA
    // ======================================
    public List<Profesores> buscarProfesoresParaMateria(int idMateria)
    {

        List<Profesores> lista = new ArrayList<>();

        String sql = """
                SELECT p.idProfesor, p.nombre, p.apellidoP, p.apellidoM, p.identificador, p.activo
                FROM MateriasProfesor mp
                JOIN Profesores p ON mp.idProfesor = p.idProfesor
                WHERE mp.idMateria = ?
                """;

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idMateria);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Profesores p = new Profesores(
                        rs.getInt("idProfesor"),
                        rs.getString("nombre"),
                        rs.getString("apellidoP"),
                        rs.getString("apellidoM"),
                        rs.getString("identificador"),
                        rs.getBoolean("activo")
                );
                lista.add(p);
            }

        } catch (SQLException e)
        {
            System.out.println("Error buscarProfesoresParaMateria: " + e.getMessage());
        }

        return lista;
    }

    // ===============================
    // LISTAR TODAS
    // ===============================
    public List<MateriasProfesores> listarTodas()
    {
        List<MateriasProfesores> lista = new ArrayList<>();

        String sql = "SELECT idMatProf, idProfesor, idMateria FROM MateriasProfesor";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                lista.add(new MateriasProfesores(
                        rs.getInt("idMatProf"),
                        rs.getInt("idProfesor"),
                        rs.getInt("idMateria")
                ));
            }

        } catch (SQLException e)
        {
            System.out.println("Error al listar MateriasProfesor: " + e.getMessage());
        }

        return lista;
    }

}
