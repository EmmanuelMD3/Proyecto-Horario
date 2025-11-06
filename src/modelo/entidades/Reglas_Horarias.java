package modelo.entidades;

import com.sun.media.jfxmedia.control.VideoRenderControl;

public class Reglas_Horarias
{
    int idRegla;
    String clave;
    String descripcion;
    String valor;

    public Reglas_Horarias()
    {
    }

    public Reglas_Horarias(int idRegla, String clave, String descripcion, String valor)
    {
        this.idRegla = idRegla;
        this.clave = clave;
        this.descripcion = descripcion;
        this.valor = valor;
    }

    public int getIdRegla()
    {
        return idRegla;
    }

    public void setIdRegla(int idRegla)
    {
        this.idRegla = idRegla;
    }

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getValor()
    {
        return valor;
    }

    public void setValor(String valor)
    {
        this.valor = valor;
    }

    @Override
    public String toString()
    {
        return "Reglas_Horarias{" +
                "idRegla=" + idRegla +
                ", clave='" + clave + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}
