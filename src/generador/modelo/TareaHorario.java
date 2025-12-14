package generador.modelo;

import modelo.entidades.Materias;
import modelo.entidades.Profesores;
import modelo.entidades.Grupos;
import modelo.entidades.Descargas;

public class TareaHorario
{

    public enum Tipo
    {CLASE, DESCARGA}

    private Tipo tipo;
    private int duracion;
    private Profesores profesor;
    private Grupos grupo;
    private Materias materia;
    private Descargas descarga;

    // ubicación final en el horario
    private int dia = -1;          // 0 = lunes ... 5 = sábado, -1 = no asignado
    private int horaInicio = -1;   // hora de inicio asignada, -1 = no asignado

    public TareaHorario(Tipo tipo, int duracion,
                        Profesores profesor,
                        Grupos grupo,
                        Materias materia,
                        Descargas descarga)
    {

        this.tipo = tipo;
        this.duracion = duracion;
        this.profesor = profesor;
        this.grupo = grupo;
        this.materia = materia;
        this.descarga = descarga;
    }

    // GETTERS
    public Tipo getTipo()
    {
        return tipo;
    }

    public int getDuracion()
    {
        return duracion;
    }

    public Profesores getProfesor()
    {
        return profesor;
    }

    public Grupos getGrupo()
    {
        return grupo;
    }

    public Materias getMateria()
    {
        return materia;
    }

    public Descargas getDescarga()
    {
        return descarga;
    }

    public int getDia()
    {
        return dia;
    }

    public int getHoraInicio()
    {
        return horaInicio;
    }

    // SETTERS USADOS POR EL ASIGNADOR
    public void setDia(int dia)
    {
        this.dia = dia;
    }

    public void setHoraInicio(int horaInicio)
    {
        this.horaInicio = horaInicio;
    }

    // CONSULTA ÚTIL PARA REPORTES Y VALIDACIONES
    public boolean estaAsignada()
    {
        return dia != -1 && horaInicio != -1;
    }
}
