package modelo.entidades;

public class Disponibilidades
{

    private int idDisponibilidad;
    private int idProfesor;
    private String dia;
    private String horaInicio;
    private String horaFin;

    public Disponibilidades(int idProfesor, String dia, String horaInicio, String horaFin)
    {
        this.idProfesor = idProfesor;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getIdDisponibilidad()
    {
        return idDisponibilidad;
    }

    public void setIdDisponibilidad(int idDisponibilidad)
    {
        this.idDisponibilidad = idDisponibilidad;
    }

    public int getIdProfesor()
    {
        return idProfesor;
    }

    public String getDia()
    {
        return dia;
    }

    public String getHoraInicio()
    {
        return horaInicio;
    }

    public String getHoraFin()
    {
        return horaFin;
    }

    @Override
    public String toString()
    {
        return dia + " " + horaInicio + "-" + horaFin;
    }
}
