package modelo.entidades;

public class Grupos
{
    int idGrupo;
    String nombre;
    int idSemetre;
    int idCiclo;
    int idCarrera;

    public Grupos()
    {
    }

    public Grupos(int idGrupo, String nombre, int idSemetre, int idCiclo, int idCarrera)
    {
        this.idGrupo = idGrupo;
        this.nombre = nombre;
        this.idSemetre = idSemetre;
        this.idCiclo = idCiclo;
        this.idCarrera = idCarrera;
    }

    public int getIdGrupo()
    {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo)
    {
        this.idGrupo = idGrupo;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public int getIdSemetre()
    {
        return idSemetre;
    }

    public void setIdSemetre(int idSemetre)
    {
        this.idSemetre = idSemetre;
    }

    public int getIdCiclo()
    {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo)
    {
        this.idCiclo = idCiclo;
    }

    public int getIdCarrera()
    {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera)
    {
        this.idCarrera = idCarrera;
    }

    @Override
    public String toString()
    {
        return "Grupos{" +
                "idGrupo=" + idGrupo +
                ", nombre='" + nombre + '\'' +
                ", idSemetre=" + idSemetre +
                ", idCiclo=" + idCiclo +
                ", idCarrera=" + idCarrera +
                '}';
    }
}
