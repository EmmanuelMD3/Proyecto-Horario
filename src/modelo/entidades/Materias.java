package modelo.entidades;

public class Materias
{
    private int idMateria;
    private String nombre;
    private int idCarrera;
    private int idSemestre;
    private int horas_semana;

    public Materias()
    {
    }

    public Materias(int idMateria, String nombre, int idCarrera, int idSemestre, int horas_semana)
    {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.idCarrera = idCarrera;
        this.idSemestre = idSemestre;
        this.horas_semana = horas_semana;
    }

    public Materias(int idMateria, String nombre, int horas_semana)
    {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.horas_semana = horas_semana;
    }

    public int getIdCarrera()
    {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera)
    {
        this.idCarrera = idCarrera;
    }

    public int getIdMateria()
    {
        return idMateria;
    }

    public void setIdMateria(int idMateria)
    {
        this.idMateria = idMateria;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public int getIdSemestre()
    {
        return idSemestre;
    }

    public void setIdSemestre(int idSemestre)
    {
        this.idSemestre = idSemestre;
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
        return nombre;
    }

}
