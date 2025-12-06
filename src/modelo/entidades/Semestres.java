package modelo.entidades;

import java.sql.Date;

public class Semestres
{
    private int idSemestre;
    private int numero;


    public Semestres()
    {
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


    @Override
    public String toString() {
        return "Semestre " + numero;
    }
}
