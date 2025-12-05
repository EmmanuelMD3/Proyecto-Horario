package conexion;

/**
 * Clase que centraliza la configuración de conexión a la base de datos. Permite
 * cambiar credenciales y parámetros de forma sencilla.
 */
public class ConfiguracionDB
{
    public static final String HOST = "localhost";
    public static final String PUERTO = "3306";
    public static final String BASE_DATOS = "ProyectoHorarios";
    public static final String USUARIO = "root";
    public static final String CONTRASENIA = "Emmanuel360#";
    public static String getUrl()
    {
        return "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
                + "?useSSL=false&serverTimezone=UTC";
    }
}
