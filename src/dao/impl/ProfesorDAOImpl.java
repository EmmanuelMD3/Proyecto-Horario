package dao.impl;

import conexion.ConexionBD;
import dao.interfaces.IProfesorDAO;
import modelo.entidades.Profesores;

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
    public boolean agregarProfesor(Profesores profesor)
    {
        String sql = "INSERT INTO Profesores (nombre, apellidoP, apellidoM, identificador, horas_descarga, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellidoP());
            ps.setString(3, profesor.getApellidoM());
            ps.setString(4, profesor.getIdentificador());
            ps.setInt(5, profesor.getHorasDescarga());
            ps.setBoolean(6, profesor.isActivo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e)
        {
            System.err.println("Error al insertar profesor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarProfesor(Profesores profesor)
    {
        String sql = "UPDATE Profesores SET nombre=?, apellidoP=?, apellidoM=?, identificador=?, horas_descarga=?, activo=? "
                + "WHERE idProfesor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellidoP());
            ps.setString(3, profesor.getApellidoM());
            ps.setString(4, profesor.getIdentificador());
            ps.setInt(5, profesor.getHorasDescarga());
            ps.setBoolean(6, profesor.isActivo());
            ps.setInt(7, profesor.getIdProfesor());
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
    public Profesores buscarPorId(int idProfesor)
    {
        String sql = "SELECT * FROM Profesores WHERE idProfesor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idProfesor);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                Profesores p = new Profesores();
                p.setIdProfesor(rs.getInt("idProfesor"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidoP(rs.getString("apellidoP"));
                p.setApellidoM(rs.getString("apellidoM"));
                p.setIdentificador(rs.getString("identificador"));
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
    public List<Profesores> listarProfesores()
    {
        List<Profesores> lista = new ArrayList<>();
        String sql = "SELECT * FROM Profesores";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql))
        {
            while (rs.next())
            {
                Profesores p = new Profesores();
                p.setIdProfesor(rs.getInt("idProfesor"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidoP(rs.getString("apellidoP"));
                p.setApellidoM(rs.getString("apellidoM"));
                p.setIdentificador(rs.getString("identificador"));
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
