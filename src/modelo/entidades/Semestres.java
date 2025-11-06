package modelo.entidades;

import java.sql.Date;

public class Semestres
{
    int idCiclo;
    String nombre;
    int tipo;
    Date fecha_inicio;
    Date fecha_final;

    public Semestres()
    {
    }

    public Semestres(int idCiclo, String nombre, int tipo, Date fecha_inicio, Date fecha_final)
    {
        this.idCiclo = idCiclo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
    }

    public int getIdCiclo()
    {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo)
    {
        this.idCiclo = idCiclo;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public int getTipo()
    {
        return tipo;
    }

    public void setTipo(int tipo)
    {
        this.tipo = tipo;
    }

    public Date getFecha_inicio()
    {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio)
    {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_final()
    {
        return fecha_final;
    }

    public void setFecha_final(Date fecha_final)
    {
        this.fecha_final = fecha_final;
    }

    @Override
    public String toString()
    {
        return "Semestres{" +
                "idCiclo=" + idCiclo +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", fecha_inicio=" + fecha_inicio +
                ", fecha_final=" + fecha_final +
                '}';
    }
}
