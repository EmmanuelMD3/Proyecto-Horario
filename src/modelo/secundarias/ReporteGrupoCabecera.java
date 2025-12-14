package modelo.secundarias;

import java.util.List;
import java.util.ArrayList;

public class ReporteGrupoCabecera {
    
    private int idGrupo;
    private String nombreCarrera;
    private int numeroSemestre;
    
    private String nombreGrupo;
    private String nombreMateria;
    private int horasSemana;
    private String nombreProfesor;
    
    private String anioCicloConcatenado;
    
    private int idAsignacion;
    private List<ReporteGrupoHorario> bloquesHorario;
    
    
    public ReporteGrupoCabecera() {
        
    }

    public ReporteGrupoCabecera(int idGrupo, String nombreCarrera, int numeroSemestre, String nombreGrupo, String nombreMateria, int horasSemana, String nombreProfesor, String anioCicloConcatenado, int idAsignacion) {
        this.idGrupo = idGrupo;
        this.nombreCarrera = nombreCarrera;
        this.numeroSemestre = numeroSemestre;
        this.nombreGrupo = nombreGrupo;
        this.nombreMateria = nombreMateria;
        this.horasSemana = horasSemana;
        this.nombreProfesor = nombreProfesor;
        this.anioCicloConcatenado = anioCicloConcatenado;
        this.idAsignacion = idAsignacion;
        this.bloquesHorario = new ArrayList<>();
    }

    //----
    
    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    //----
    
    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    //----
    
    public int getNumeroSemestre() {
        return numeroSemestre;
    }

    public void setNumeroSemestre(int numeroSemestre) {
        this.numeroSemestre = numeroSemestre;
    }
    
    //----

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    //----
    
    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    //----
    
    public int getHorasSemana() {
        return horasSemana;
    }

    public void setHorasSemana(int horasSemana) {
        this.horasSemana = horasSemana;
    }

    //----
    
    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    //----
    
    public String getAnioCicloConcatenado() {
        return anioCicloConcatenado;
    }

    public void setAnioCicloConcatenado(String anioCicloConcatenado) {
        this.anioCicloConcatenado = anioCicloConcatenado;
    }
    
    //----
    
    public List<ReporteGrupoHorario> getBloquesHorario() {
        return bloquesHorario;
    }
    
    public void setBloquesHorario(List<ReporteGrupoHorario> bloquesHorario) {
        this.bloquesHorario = bloquesHorario;
    }
    
    public void addBloqueHorario(ReporteGrupoHorario bloque) {
        this.bloquesHorario.add(bloque);
    }

    //----
    
    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }
    
}
