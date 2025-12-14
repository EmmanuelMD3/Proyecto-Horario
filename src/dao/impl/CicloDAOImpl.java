package dao.impl;

import conexion.ConexionBD;
import dao.interfaces.ICicloDAO;
import modelo.entidades.Ciclos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CicloDAOImpl implements ICicloDAO
{

    public CicloDAOImpl()
    {
        // NO se guarda ninguna conexión aquí
    }

    // ========================================================
    // INSERTAR
    // ========================================================
    @Override
    public boolean agregarCiclo(Ciclos ciclo)
    {
        String sql = "INSERT INTO Ciclos (nombre, tipo, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setString(1, ciclo.getNombre());
            ps.setString(2, ciclo.getTipo());
            ps.setDate(3, ciclo.getFechaInicio());
            ps.setDate(4, ciclo.getFechaFin());

            return ps.executeUpdate() > 0;

        } catch (SQLException e)
        {
            System.err.println("Error al agregar ciclo: " + e.getMessage());
            return false;
        }
    }

    // ========================================================
    // ACTUALIZAR
    // ========================================================
    @Override
    public boolean actualizarCiclo(Ciclos ciclo)
    {
        String sql = "UPDATE Ciclos SET nombre=?, tipo=?, fecha_inicio=?, fecha_fin=? WHERE idCiclo=?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setString(1, ciclo.getNombre());
            ps.setString(2, ciclo.getTipo());
            ps.setDate(3, ciclo.getFechaInicio());
            ps.setDate(4, ciclo.getFechaFin());
            ps.setInt(5, ciclo.getIdCiclo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e)
        {
            System.err.println("Error al actualizar ciclo: " + e.getMessage());
            return false;
        }
    }

    // ========================================================
    // ELIMINAR
    // ========================================================
    @Override
    public boolean eliminarCiclo(int idCiclo)
    {
        String sql = "DELETE FROM Ciclos WHERE idCiclo = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idCiclo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e)
        {
            System.err.println("Error al eliminar ciclo: " + e.getMessage());
            return false;
        }
    }

    // ========================================================
    // BUSCAR POR ID
    // ========================================================
    @Override
    public Ciclos buscarPorId(int idCiclo)
    {
        String sql = "SELECT * FROM Ciclos WHERE idCiclo = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, idCiclo);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return new Ciclos(
                        rs.getInt("idCiclo"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin")
                );
            }

        } catch (SQLException e)
        {
            System.err.println("Error al buscar ciclo: " + e.getMessage());
        }

        return null;
    }

    // ========================================================
    // LISTAR TODOS
    // ========================================================
    @Override
    public List<Ciclos> listarCiclos()
    {
        List<Ciclos> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ciclos ORDER BY idCiclo DESC";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                lista.add(new Ciclos(
                        rs.getInt("idCiclo"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin")
                ));
            }

        } catch (SQLException e)
        {
            System.err.println("Error al listar ciclos: " + e.getMessage());
        }

        return lista;
    }

    // ========================================================
    // OBTENER CICLO ACTIVO
    // ========================================================
    @Override
    public Ciclos obtenerCicloActivo()
    {
        String sql = "SELECT * FROM Ciclos WHERE CURDATE() BETWEEN fecha_inicio AND fecha_fin LIMIT 1";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

            if (rs.next())
            {
                return new Ciclos(
                        rs.getInt("idCiclo"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin")
                );
            }

        } catch (SQLException e)
        {
            System.err.println("Error al obtener ciclo activo: " + e.getMessage());
        }

        return null;
    }

    // ========================================================
    // DESACTIVAR TODOS
    // ========================================================
    @Override
    public boolean desactivarTodos()
    {
        String sql = "UPDATE Ciclos SET tipo='inactivo'";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.executeUpdate();
            return true;

        } catch (SQLException e)
        {
            System.err.println("Error al desactivar ciclos: " + e.getMessage());
            return false;
        }
    }
}
