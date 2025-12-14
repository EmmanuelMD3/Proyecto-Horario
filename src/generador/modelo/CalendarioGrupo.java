package generador.modelo;

public class CalendarioGrupo
{

    private boolean[][] ocupado = new boolean[5][11];

    public boolean estaLibre(DiaSemana dia, int horaInicio, int duracion)
    {
        int d = dia.ordinal();
        int i = horaInicio - 7;

        for (int h = 0; h < duracion; h++)
        {
            if (i + h >= 11 || ocupado[d][i + h]) return false;
        }

        return true;
    }

    public void ocupar(DiaSemana dia, int horaInicio, int duracion)
    {
        int d = dia.ordinal();
        int i = horaInicio - 7;

        for (int h = 0; h < duracion; h++)
        {
            ocupado[d][i + h] = true;
        }
    }
}
