package modelo.entidades;

public class Carreras
{
    int idCarrera;
    String nombre;
    String clave;

    public Carreras()
    {

    }

    public Carreras(int idCarrera, String nombre, String clave)
    {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.clave = clave;
    }

    public Carreras(int idCarrera, String nombre)
    {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
    }

    public int getIdCarrera()
    {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera)
    {
        this.idCarrera = idCarrera;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    @Override
    public String toString()
    {
        return nombre;
    }
}
