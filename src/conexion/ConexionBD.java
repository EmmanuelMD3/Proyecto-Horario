package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD
{

    private static Connection conn = null;

    public static Connection conectar()
    {
        try
        {
            // Si nunca se creó o ya se cerró, la volvemos a abrir
            if (conn == null || conn.isClosed())
            {
                String url = ConfiguracionDB.getUrl();
                String user = ConfiguracionDB.USUARIO;
                String pass = ConfiguracionDB.CONTRASENIA;

                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("Conexion establecida con la base de datos ProyectoHorarios");
            }
        } catch (SQLException e)
        {
            System.err.println("Error al conectar con la BD: " + e.getMessage());
        }
        return conn;
    }

    public static void cerrar()
    {
        try
        {
            if (conn != null && !conn.isClosed())
            {
                conn.close();
            }
        } catch (SQLException e)
        {
            System.err.println("Error al cerrar BD: " + e.getMessage());
        }
    }
}
