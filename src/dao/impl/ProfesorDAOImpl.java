package dao.impl;

import conexion.ConexionBD;
import dao.interfaces.IProfesorDAO;
import modelo.entidades.Profesores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAOImpl implements IProfesorDAO
{

    public ProfesorDAOImpl()
    {
        // ❌ No abrir conexión aquí
    }

    // =====================================
    // INSERTAR PROFESOR
    // =====================================
    @Override
    public boolean agregarProfesor(Profesores profesor)
    {
        String sql = """
                INSERT INTO Profesores (nombre, apellidoP, apellidoM, identificador, activo)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellidoP());
            ps.setString(3, profesor.getApellidoM());
            ps.setString(4, profesor.getIdentificador());
            ps.setBoolean(5, profesor.isActivo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e)
        {
            System.err.println("Error al insertar profesor: " + e.getMessage());
            return false;
        }
    }

    // =====================================
    // ACTUALIZAR PROFESOR
    // =====================================
    @Override
    public boolean actualizarProfesor(Profesores profesor)
    {
        String sql = """
                UPDATE Profesores 
                SET nombre=?, apellidoP=?, apellidoM=?, identificador=?, activo=?
                WHERE idProfesor=?
                """;

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellidoP());
            ps.setString(3, profesor.getApellidoM());
            ps.setString(4, profesor.getIdentificador());
            ps.setBoolean(5, profesor.isActivo());
            ps.setInt(6, profesor.getIdProfesor());

            return ps.executeUpdate() > 0;

        } catch (SQLException e)
        {
            System.err.println("Error al actualizar profesor: " + e.getMessage());
            return false;
        }
    }

    // =====================================
    // ELIMINAR PROFESOR
    // =====================================
    @Override
    public boolean eliminarProfesor(int idProfesor)
    {
        String sql = "DELETE FROM Profesores WHERE idProfesor=?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idProfesor);
            return ps.executeUpdate() > 0;

        } catch (SQLException e)
        {
            System.err.println("Error al eliminar profesor: " + e.getMessage());
            return false;
        }
    }

    // =====================================
    // BUSCAR POR ID
    // =====================================
    @Override
    public Profesores buscarPorId(int idProfesor)
    {
        String sql = "SELECT * FROM Profesores WHERE idProfesor=?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idProfesor);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return new Profesores(
                        rs.getInt("idProfesor"),
                        rs.getString("nombre"),
                        rs.getString("apellidoP"),
                        rs.getString("apellidoM"),
                        rs.getString("identificador"),
                        rs.getBoolean("activo")
                );
            }

        } catch (SQLException e)
        {
            System.err.println("Error al buscar profesor: " + e.getMessage());
        }

        return null;
    }

    // =====================================
    // LISTAR TODOS
    // =====================================
    @Override
    public List<Profesores> listarProfesores()
    {
        List<Profesores> lista = new ArrayList<>();
        String sql = "SELECT * FROM Profesores";

        try (Connection conn = ConexionBD.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql))
        {

            while (rs.next())
            {
                lista.add(new Profesores(
                        rs.getInt("idProfesor"),
                        rs.getString("nombre"),
                        rs.getString("apellidoP"),
                        rs.getString("apellidoM"),
                        rs.getString("identificador"),
                        rs.getBoolean("activo")
                ));
            }

        } catch (SQLException e)
        {
            System.err.println("Error al listar profesores: " + e.getMessage());
        }

        return lista;
    }

    // =====================================
    // BUSCAR PROFESOR POR ID (duplicado)
    // =====================================
    public Profesores buscarProfesorPorId(int idProfesor)
    {

        String sql = "SELECT * FROM Profesores WHERE idProfesor = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idProfesor);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return new Profesores(
                        rs.getInt("idProfesor"),
                        rs.getString("nombre"),
                        rs.getString("apellidoP"),
                        rs.getString("apellidoM"),
                        rs.getString("identificador"),
                        rs.getBoolean("activo")
                );
            }

        } catch (SQLException e)
        {
            System.out.println("Error buscarProfesorPorId: " + e.getMessage());
        }

        return null;
    }

}
