package modelo.secundarias;

import java.time.LocalTime;

public class ReporteGrupoHorario {
    private String dia;
    private LocalTime horaInicio;
    private LocalTime horaFin; 
    private String nombreBloque;

    
    public ReporteGrupoHorario() {
        
    }
    
    public ReporteGrupoHorario(String dia, LocalTime horaInicio, LocalTime horaFin, String nombreBloque) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.nombreBloque = nombreBloque;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getNombreBloque() {
        return nombreBloque;
    }

    public void setNombreBloque(String nombreBloque) {
        this.nombreBloque = nombreBloque;
    }
    
}
