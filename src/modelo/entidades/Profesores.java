package modelo.entidades;

public class Profesores
{

    private int idProfesor;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String identificador;
    //private int horasDescarga;
    private boolean activo;

    public Profesores()
    {
    }

    public Profesores(int idProfesor, String nombre, String apellidoP, String apellidoM, String identificador, boolean activo)
    {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.identificador = identificador;
        //this.horasDescarga = horasDescarga;
        this.activo = activo;
    }


    public int getIdProfesor()
    {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor)
    {
        this.idProfesor = idProfesor;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellidoP()
    {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP)
    {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM()
    {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM)
    {
        this.apellidoM = apellidoM;
    }

    /*public int getHorasDescarga()
    {
        return horasDescarga;
    }

    public void setHorasDescarga(int horasDescarga)
    {
        this.horasDescarga = horasDescarga;
    }*/

    public boolean isActivo()
    {
        return activo;
    }

    public void setActivo(boolean activo)
    {
        this.activo = activo;
    }

    /**
     * @return the identificador
     */
    public String getIdentificador()
    {
        return identificador;
    }

    /**
     * @param identificador the identificador to set
     */
    public void setIdentificador(String identificador)
    {
        this.identificador = identificador;
    }

    @Override
    public String toString()
    {
        return "Profesores{" + "idProfesor=" + idProfesor + ", nombre=" + nombre + ", apellidoP=" + apellidoP + ", apellidoM=" + apellidoM + ", identificador=" + identificador + ", activo=" + activo + '}';
    }

    public int getDisponibilidadTotal()
    {
        // Este método solo se usará para priorizar profesores
        // Puedes mejorarlo después para calcularlo desde DisponibilidadesDAO

        return 40; // por ahora todos iguales
    }

}
