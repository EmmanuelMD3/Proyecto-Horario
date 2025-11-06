package modelo.entidades;

import java.sql.Date;

public class Horarios_Generados
{
    int idHorario;
    int idAsignacion;
    int dia;
    Date hora_inicio;
    Date hora_fin;
    String aula;
    int tipo_bloque;
    int estado;

    public Horarios_Generados()
    {
    }

    public Horarios_Generados(int idHorario, int idAsignacion, int dia, Date hora_inicio, Date hora_fin, int tipo_bloque, int estado, String aula)
    {
        this.idHorario = idHorario;
        this.idAsignacion = idAsignacion;
        this.dia = dia;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.tipo_bloque = tipo_bloque;
        this.estado = estado;
        this.aula = aula;
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

    public int getDia()
    {
        return dia;
    }

    public void setDia(int dia)
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

    public String getAula()
    {
        return aula;
    }

    public void setAula(String aula)
    {
        this.aula = aula;
    }

    public int getTipo_bloque()
    {
        return tipo_bloque;
    }

    public void setTipo_bloque(int tipo_bloque)
    {
        this.tipo_bloque = tipo_bloque;
    }

    public int getEstado()
    {
        return estado;
    }

    public void setEstado(int estado)
    {
        this.estado = estado;
    }

    @Override
    public String toString()
    {
        return "Horarios_Generados{" +
                "idHorario=" + idHorario +
                ", idAsignacion=" + idAsignacion +
                ", dia=" + dia +
                ", hora_inicio=" + hora_inicio +
                ", hora_fin=" + hora_fin +
                ", aula='" + aula + '\'' +
                ", tipo_bloque=" + tipo_bloque +
                ", estado=" + estado +
                '}';
    }
}
