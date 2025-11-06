package modelo.entidades;

public class Asignaciones
{
    int idAsignacion;
    int idProfesor;
    int idMateria;
    int idGrupo;
    int horas_asignadas;

    public Asignaciones()
    {
    }

    public Asignaciones(int idProfesor, int idAsignacion, int idMateria, int idGrupo, int horas_asignadas)
    {
        this.idProfesor = idProfesor;
        this.idAsignacion = idAsignacion;
        this.idMateria = idMateria;
        this.idGrupo = idGrupo;
        this.horas_asignadas = horas_asignadas;
    }

    public int getIdAsignacion()
    {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion)
    {
        this.idAsignacion = idAsignacion;
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

    public int getIdGrupo()
    {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo)
    {
        this.idGrupo = idGrupo;
    }

    public int getHoras_asignadas()
    {
        return horas_asignadas;
    }

    public void setHoras_asignadas(int horas_asignadas)
    {
        this.horas_asignadas = horas_asignadas;
    }

    @Override
    public String toString()
    {
        return "Asignaciones{" +
                "idAsignacion=" + idAsignacion +
                ", idProfesor=" + idProfesor +
                ", idMateria=" + idMateria +
                ", idGrupo=" + idGrupo +
                ", horas_asignadas=" + horas_asignadas +
                '}';
    }


}
