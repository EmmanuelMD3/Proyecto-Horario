package modelo.entidades;

public class Grupos
{

    private int idGrupo;
    private String nombre;
    private int idCarrera;
    private int idSemestre;
    private int idCiclo;

    public Grupos()
    {
    }

    public Grupos(int idGrupo, String nombre,
                  int idCarrera, int idSemestre, int idCiclo)
    {
        this.idGrupo = idGrupo;
        this.nombre = nombre;
        this.idCarrera = idCarrera;
        this.idSemestre = idSemestre;
        this.idCiclo = idCiclo;
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

    public int getIdCarrera()
    {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera)
    {
        this.idCarrera = idCarrera;
    }

    public int getIdSemestre()
    {
        return idSemestre;
    }

    public void setIdSemestre(int idSemestre)
    {
        this.idSemestre = idSemestre;
    }

    public int getIdCiclo()
    {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo)
    {
        this.idCiclo = idCiclo;
    }

    @Override
    public String toString()
    {
        return nombre;
    }
}
