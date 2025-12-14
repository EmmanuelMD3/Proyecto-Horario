package conexion;

import java.sql.Connection;

/**
 * Clase de prueba para verificar la conexión con la base de datos.
 */
public class TestConexion
{

    public static void main(String[] args)
    {
        Connection conn = ConexionBD.conectar();
        if (conn != null)
        {
            System.out.println("Conexion verificada exitosamente.");
            ConexionBD.conectar();
        } else
        {
            System.out.println("No se pudo establecer la conexión.");
        }
    }
}
