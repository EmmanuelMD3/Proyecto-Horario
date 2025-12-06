package pruebas;

import modelo.entidades.Carreras;
import modelo.entidades.Materias;
import modelo.entidades.Semestres;

public class ProfesorMateriaTemp
{
    private Materias materia;
    private Carreras carrera;
    private Semestres semestre;

    public ProfesorMateriaTemp(Materias materia, Carreras carrera, Semestres semestre)
    {
        this.materia = materia;
        this.carrera = carrera;
        this.semestre = semestre;
    }


    public Materias getMateria() {
        return materia;
    }

    public void setMateria(Materias materia) {
        this.materia = materia;
    }

    public Carreras getCarrera() {
        return carrera;
    }

    public void setCarrera(Carreras carrera) {
        this.carrera = carrera;
    }

    public Semestres getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestres semestre) {
        this.semestre = semestre;
    }
}
