package dao.impl;

import conexion.ConexionBD;
import dao.interfaces.ICicloDAO;
import modelo.entidades.Ciclos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CicloDAOImpl implements ICicloDAO
{

    private Connection conn;

    public CicloDAOImpl()
    {
        conn = ConexionBD.conectar();
    }

    @Override
    public boolean agregarCiclo(Ciclos ciclo)
    {
        String sql = "INSERT INTO Ciclos (nombre, tipo, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql))
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

    @Override
    public boolean actualizarCiclo(Ciclos ciclo)
    {
        String sql = "UPDATE Ciclos SET nombre=?, tipo=?, fecha_inicio=?, fecha_fin=? WHERE idCiclo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql))
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

    @Override
    public boolean eliminarCiclo(int idCiclo)
    {
        String sql = "DELETE FROM Ciclos WHERE idCiclo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, idCiclo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e)
        {
            System.err.println("Error al eliminar ciclo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Ciclos buscarPorId(int idCiclo)
    {
        String sql = "SELECT * FROM Ciclos WHERE idCiclo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql))
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
            System.err.println("sError al buscar ciclo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Ciclos> listarCiclos()
    {
        List<Ciclos> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ciclos ORDER BY idCiclo DESC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql))
        {
            while (rs.next())
            {
                Ciclos c = new Ciclos(
                        rs.getInt("idCiclo"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin")
                );
                lista.add(c);
            }
        } catch (SQLException e)
        {
            System.err.println("Error al listar ciclos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Ciclos obtenerCicloActivo()
    {
        String sql = "SELECT * FROM Ciclos WHERE CURDATE() BETWEEN fecha_inicio AND fecha_fin LIMIT 1";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql))
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

    @Override
    public boolean desactivarTodos()
    {
        String sql = "UPDATE Ciclos SET tipo='inactivo'";
        try (Statement st = conn.createStatement())
        {
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e)
        {
            System.err.println("Error al desactivar ciclos: " + e.getMessage());
            return false;
        }
    }
}
