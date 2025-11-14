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

    public void setIdProfesor(int idProfesor)
    {
        this.idProfesor = idProfesor;
    }

    public String getDia()
    {
        return dia;
    }

    public void setDia(String dia)
    {
        this.dia = dia;
    }

    public String getHoraInicio()
    {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio)
    {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin()
    {
        return horaFin;
    }

    public void setHoraFin(String horaFin)
    {
        this.horaFin = horaFin;
    }

    @Override
    public String toString()
    {
        return dia + " " + horaInicio + "-" + horaFin;
    }
}
