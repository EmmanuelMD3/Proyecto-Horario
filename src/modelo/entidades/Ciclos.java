package modelo.entidades;

import java.sql.Date;

public class Ciclos
{
    private int idCiclo;
    private String nombre;
    private String tipo;
    private Date fechaInicio;
    private Date fechaFin;

    public Ciclos()
    {
    }

    public Ciclos(int idCiclo, String nombre, String tipo, Date fechaInicio, Date fechaFin)
    {
        this.idCiclo = idCiclo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // 🔹 Getters y setters
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

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public Date getFechaInicio()
    {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio)
    {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin()
    {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin)
    {
    }

    @Override
    public String toString()
    {
        return "Ciclos{" + "idCiclo=" + idCiclo + ", nombre='" + nombre + '\'' + ", tipo='" + tipo + '\'' + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + '}';
    }
}