package modelo.entidades;

public class Descargas
{
    private int idDescarga;
    private String nombre;

    public Descargas()
    {
    }

    public Descargas(int idDescarga, String nombre)
    {
        this.idDescarga = idDescarga;
        this.nombre = nombre;
    }

    public int getIdDescarga()
    {
        return idDescarga;
    }

    public void setIdDescarga(int idDescarga)
    {
        this.idDescarga = idDescarga;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }


    @Override
    public String toString()
    {
        return nombre;
    }
}
