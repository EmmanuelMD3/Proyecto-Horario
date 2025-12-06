package modelo.entidades;

public class Descargas
{
    private int idDescarga;
    private String nombre;
    private int horas_semana;

    public Descargas()
    {
    }

    public Descargas(int idDescarga, String nombre, int horas_semana)
    {
        this.idDescarga = idDescarga;
        this.nombre = nombre;
        this.horas_semana = horas_semana;
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

    public int getHoras_semana()
    {
        return horas_semana;
    }

    public void setHoras_semana(int horas_semana)
    {
        this.horas_semana = horas_semana;
    }

    @Override
    public String toString()
    {
        return "Descargas{" +
                "idDescarga=" + idDescarga +
                ", nombre='" + nombre + '\'' +
                ", horas_semana=" + horas_semana +
                '}';
    }
}
