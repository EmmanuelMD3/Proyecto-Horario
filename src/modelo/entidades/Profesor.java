package modelo.entidades;

public class Profesor
{

    private int idProfesor;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String correo;
    private String telefono;
    private int horasDescarga;
    private boolean activo;

    public Profesor()
    {
    }

    public Profesor(int idProfesor, String nombre, String apellidoP, String apellidoM, String correo, String telefono, int horasDescarga, boolean activo)
    {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.correo = correo;
        this.telefono = telefono;
        this.horasDescarga = horasDescarga;
        this.activo = activo;
    }

    public int getIdProfesor()
    {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor)
    {
        this.idProfesor = idProfesor;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellidoP()
    {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP)
    {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM()
    {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM)
    {
        this.apellidoM = apellidoM;
    }

    public String getCorreo()
    {
        return correo;
    }

    public void setCorreo(String correo)
    {
        this.correo = correo;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public int getHorasDescarga()
    {
        return horasDescarga;
    }

    public void setHorasDescarga(int horasDescarga)
    {
        this.horasDescarga = horasDescarga;
    }

    public boolean isActivo()
    {
        return activo;
    }

    public void setActivo(boolean activo)
    {
        this.activo = activo;
    }
}
