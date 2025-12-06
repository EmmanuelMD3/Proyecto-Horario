package modelo.secundarias;

public class MateriasProfesorTemp
{
    private int idMateria;
    private String nombreMateria;
    private String carrera;
    private int semestre;

    public MateriasProfesorTemp(int idMateria, String nombreMateria, String carrera, int semestre)
    {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
        this.carrera = carrera;
        this.semestre = semestre;
    }

    public int getIdMateria()
    {
        return idMateria;
    }

    public String getNombreMateria()
    {
        return nombreMateria;
    }

    public String getCarrera()
    {
        return carrera;
    }

    public int getSemestre()
    {
        return semestre;
    }
}
