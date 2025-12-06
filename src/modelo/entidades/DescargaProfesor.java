package modelo.entidades;

public class DescargaProfesor
{
    private int idDescargaProfesor;
    private int idProfesor;
    private int idDescarga;
    private int horas_asignadas;

    public DescargaProfesor()
    {
    }

    public DescargaProfesor(int idDescargaProfesor, int idProfesor, int idDescarga, int horas_asignadas)
    {
        this.idDescargaProfesor = idDescargaProfesor;
        this.idProfesor = idProfesor;
        this.idDescarga = idDescarga;
        this.horas_asignadas = horas_asignadas;
    }

    public int getIdDescargaProfesor()
    {
        return idDescargaProfesor;
    }

    public void setIdDescargaProfesor(int idDescargaProfesor)
    {
        this.idDescargaProfesor = idDescargaProfesor;
    }

    public int getIdProfesor()
    {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor)
    {
        this.idProfesor = idProfesor;
    }

    public int getIdDescarga()
    {
        return idDescarga;
    }

    public void setIdDescarga(int idDescarga)
    {
        this.idDescarga = idDescarga;
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
        return "DescargaProfesor{" +
                "idDescargaProfesor=" + idDescargaProfesor +
                ", idProfesor=" + idProfesor +
                ", idDescarga=" + idDescarga +
                ", horas_asignadas=" + horas_asignadas +
                '}';
    }
}
