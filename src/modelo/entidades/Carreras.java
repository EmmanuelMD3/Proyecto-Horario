package modelo.entidades;

public class Carreras
{
    int idCarrera;
    String nombre;
    String clave;
    String descripcion;

    public Carreras()
    {

    }

    public Carreras(int idCarrera, String nombre, String clave, String descripcion)
    {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.clave = clave;
        this.descripcion = descripcion;
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

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    @Override
    public String toString()
    {
        return "Carreras{" +
                "idCarrera=" + idCarrera +
                ", nombre='" + nombre + '\'' +
                ", clave='" + clave + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
