package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de establecer y cerrar la conexión con la base de datos
 * MySQL.
 */
public class ConexionBD
{

    private static Connection conexion = null;

    /**
     * Método para conectar a la base de datos.
     *
     * @return Objeto Connection si la conexión fue exitosa, null si hubo error.
     */
    public static Connection conectar()
    {
        try
        {
            if (conexion == null || conexion.isClosed())
            {
                conexion = DriverManager.getConnection(
                        ConfiguracionDB.getUrl(),
                        ConfiguracionDB.USUARIO,
                        ConfiguracionDB.CONTRASENIA
                );
                System.out.println("Conexion establecida con la base de datos " + ConfiguracionDB.BASE_DATOS);
            }
        } catch (SQLException e)
        {
            System.err.println("Error al conectar con la base de datos:");
            System.err.println(e.getMessage());
        }
        return conexion;
    }

    /**
     * Método para cerrar la conexión actual.
     */
    public static void cerrarConexion()
    {
        try
        {
            if (conexion != null && !conexion.isClosed())
            {
                conexion.close();
                System.out.println("Conexion cerrada correctamente.");
            }
        } catch (SQLException e)
        {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    /**
     * Devuelve la conexión actual sin volver a crearla.
     *
     * @return conexión activa o null si no está conectada.
     */
    public static Connection getConexion()
    {
        return conexion;
    }
}
