package modelo.entidades;

public class Materias
{
    int idMateria;
    String nombre;
    int idSemestre;
    int horas_semana;

    public Materias()
    {
    }

    public Materias(int idMateria, String nombre, int idSemestre, int horas_semana)
    {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.idSemestre = idSemestre;
        this.horas_semana = horas_semana;
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
        return "Materias{" +
                "idMateria=" + idMateria +
                ", nombre='" + nombre + '\'' +
                ", idSemestre=" + idSemestre +
                ", horas_semana=" + horas_semana +
                '}';
    }
}
