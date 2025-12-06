package modelo.entidades;

import java.sql.Date;

public class Horarios
{
    int idHorario;
    int idAsignacion;
    int idProfesor;
    String dia;
    Date hora_inicio;
    Date hora_fin;
    String tipo;

    public Horarios()
    {
    }

    public Horarios(int idHorario, int idAsignacion, int idProfesor, String dia, Date hora_inicio, Date hora_fin, String tipo)
    {
        this.idHorario = idHorario;
        this.idAsignacion = idAsignacion;
        this.idProfesor = idProfesor;
        this.dia = dia;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.tipo = tipo;
    }

    public int getIdHorario()
    {
        return idHorario;
    }

    public void setIdHorario(int idHorario)
    {
        this.idHorario = idHorario;
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

    public String getDia()
    {
        return dia;
    }

    public void setDia(String dia)
    {
        this.dia = dia;
    }

    public Date getHora_inicio()
    {
        return hora_inicio;
    }

    public void setHora_inicio(Date hora_inicio)
    {
        this.hora_inicio = hora_inicio;
    }

    public Date getHora_fin()
    {
        return hora_fin;
    }

    public void setHora_fin(Date hora_fin)
    {
        this.hora_fin = hora_fin;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    @Override
    public String toString()
    {
        return "Horarios{" +
                "idHorario=" + idHorario +
                ", idAsignacion=" + idAsignacion +
                ", idProfesor=" + idProfesor +
                ", dia='" + dia + '\'' +
                ", hora_inicio=" + hora_inicio +
                ", hora_fin=" + hora_fin +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
