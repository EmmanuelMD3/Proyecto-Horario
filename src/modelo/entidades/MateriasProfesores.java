package modelo.entidades;

public class MateriasProfesores
{
    private int idMatProf;
    private int idProfesor;
    private int idMateria;
    private int Preferencia;

    public MateriasProfesores()
    {
    }

    public MateriasProfesores(int idMatProf, int idProfesor, int idMateria, int preferencia)
    {
        this.idMatProf = idMatProf;
        this.idProfesor = idProfesor;
        this.idMateria = idMateria;
        Preferencia = preferencia;
    }

    public MateriasProfesores(int idMatProf, int idProfesor, int idMateria)
    {
        this.idMatProf = idMatProf;
        this.idProfesor = idProfesor;
        this.idMateria = idMateria;
    }

    public int getIdMatProf()
    {
        return idMatProf;
    }

    public void setIdMatProf(int idMatProf)
    {
        this.idMatProf = idMatProf;
    }

    public int getIdProfesor()
    {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor)
    {
        this.idProfesor = idProfesor;
    }

    public int getIdMateria()
    {
        return idMateria;
    }

    public void setIdMateria(int idMateria)
    {
        this.idMateria = idMateria;
    }

    public int getPreferencia()
    {
        return Preferencia;
    }

    public void setPreferencia(int preferencia)
    {
        Preferencia = preferencia;
    }

    @Override
    public String toString()
    {
        return "MateriasProfesores{" +
                "idMatProf=" + idMatProf +
                ", idProfesor=" + idProfesor +
                ", idMateria=" + idMateria +
                ", Preferencia=" + Preferencia +
                '}';
    }
}
