package modelo.entidades;

import java.sql.Date;

public class Semestres
{
    int idSemestre;
    int numero;
    boolean activo;
    int idCiclo;
    int idCarrera;

    public Semestres()
    {
    }

    public Semestres(int idSemestre, int numero, boolean activo, int idCiclo, int idCarrera) {
        this.idSemestre = idSemestre;
        this.numero = numero;
        this.activo = activo;
        this.idCiclo = idCiclo;
        this.idCarrera = idCarrera;
    }

    public Semestres(int idSemestre, int numero)
    {
        this.idSemestre = idSemestre;
        this.numero = numero;
    }

    public int getIdSemestre() {
        return idSemestre;
    }

    public void setIdSemestre(int idSemestre) {
        this.idSemestre = idSemestre;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    @Override
    public String toString() {
        return "Semestre " + numero;
    }
}
