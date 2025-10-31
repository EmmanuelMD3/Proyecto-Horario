package dao.impl;

import conexion.ConexionBD;
import dao.interfaces.IProfesorDAO;
import modelo.entidades.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de los métodos DAO para la tabla Profesores.
 */
public class ProfesorDAOImpl implements IProfesorDAO
{

    private Connection conn;

    public ProfesorDAOImpl()
    {
        conn = ConexionBD.conectar(); 
    }

    @Override
    public boolean agregarProfesor(Profesor profesor)
    {
        String sql = "INSERT INTO Profesores (nombre, apellidoP, apellidoM, correo, telefono, horas_descarga, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellidoP());
            ps.setString(3, profesor.getApellidoM());
            ps.setString(4, profesor.getCorreo());
            ps.setString(5, profesor.getTelefono());
            ps.setInt(6, profesor.getHorasDescarga());
            ps.setBoolean(7, profesor.isActivo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e)
        {
            System.err.println("Error al insertar profesor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarProfesor(Profesor profesor)
    {
        String sql = "UPDATE Profesores SET nombre=?, apellidoP=?, apellidoM=?, correo=?, telefono=?, horas_descarga=?, activo=? "
                + "WHERE idProfesor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellidoP());
            ps.setString(3, profesor.getApellidoM());
            ps.setString(4, profesor.getCorreo());
            ps.setString(5, profesor.getTelefono());
            ps.setInt(6, profesor.getHorasDescarga());
            ps.setBoolean(7, profesor.isActivo());
            ps.setInt(8, profesor.getIdProfesor());

            return ps.executeUpdate() > 0;
        } catch (SQLException e)
        {
            System.err.println("Error al actualizar profesor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarProfesor(int idProfesor)
    {
        String sql = "DELETE FROM Profesores WHERE idProfesor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e)
        {
            System.err.println("Error al eliminar profesor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Profesor buscarPorId(int idProfesor)
    {
        String sql = "SELECT * FROM Profesores WHERE idProfesor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                Profesor p = new Profesor();
                p.setIdProfesor(rs.getInt("idProfesor"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidoP(rs.getString("apellidoP"));
                p.setApellidoM(rs.getString("apellidoM"));
                p.setCorreo(rs.getString("correo"));
                p.setTelefono(rs.getString("telefono"));
                p.setHorasDescarga(rs.getInt("horas_descarga"));
                p.setActivo(rs.getBoolean("activo"));
                return p;
            }
        } catch (SQLException e)
        {
            System.err.println("Error al buscar profesor: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Profesor> listarProfesores()
    {
        List<Profesor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Profesores ORDER BY nombre ASC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql))
        {
            while (rs.next())
            {
                Profesor p = new Profesor();
                p.setIdProfesor(rs.getInt("idProfesor"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidoP(rs.getString("apellidoP"));
                p.setApellidoM(rs.getString("apellidoM"));
                p.setCorreo(rs.getString("correo"));
                p.setTelefono(rs.getString("telefono"));
                p.setHorasDescarga(rs.getInt("horas_descarga"));
                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }
        } catch (SQLException e)
        {
            System.err.println("Error al listar profesores: " + e.getMessage());
        }
        return lista;
    }
}
