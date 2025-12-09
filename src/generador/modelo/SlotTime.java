package generador.modelo;

public class SlotTime
{

    private DiaSemana dia;
    private int horaInicio;

    public SlotTime(DiaSemana dia, int horaInicio)
    {
        this.dia = dia;
        this.horaInicio = horaInicio;
    }

    public DiaSemana getDia()
    {
        return dia;
    }

    public int getHoraInicio()
    {
        return horaInicio;
    }

    @Override
    public String toString()
    {
        return dia + " " + horaInicio + ":00";
    }
}
