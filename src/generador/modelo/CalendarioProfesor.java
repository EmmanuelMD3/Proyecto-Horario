package generador.modelo;

import modelo.entidades.Grupos;
import modelo.entidades.Profesores;

import java.util.HashMap;
import java.util.Map;

public class CalendarioProfesor
{

    private Profesores profesor;

    /**
     * Ahora manejamos horas 0–23 para evitar cualquier out-of-bounds.
     * Solo se usarán de 7 a 18, pero no importa, así nunca falla.
     */
    private boolean[][] disponibilidad = new boolean[6][24];
    private boolean[][] ocupado = new boolean[6][24];

    private Map<Integer, boolean[][]> ocupadoGrupo = new HashMap<>();

    private int horasTotales = 0;

    public CalendarioProfesor(Profesores profesor)
    {
        this.profesor = profesor;

        // Inicializamos todo como disponible por defecto
        for (int d = 0; d < 6; d++)
        {
            for (int h = 0; h < 24; h++)
            {
                disponibilidad[d][h] = true;
            }
        }
    }

    // ==========================================================
    // DISPONIBILIDAD REAL SEGÚN LA BD
    // ==========================================================

    public void marcarDisponible(int dia, int hora)
    {
        if (hora >= 0 && hora < 24)
            disponibilidad[dia][hora] = true;
    }

    public void marcarNoDisponible(int dia, int hora)
    {
        if (hora >= 0 && hora < 24)
            disponibilidad[dia][hora] = false;
    }

    // ==========================================================
    // MÉTODOS USADOS POR LAS REGLAS
    // ==========================================================

    public boolean estaDisponible(int dia, int horaInicio, int dur)
    {
        for (int h = horaInicio; h < horaInicio + dur; h++)
        {
            if (h < 0 || h >= 24) return false;
            if (!disponibilidad[dia][h]) return false;
        }
        return true;
    }

    public boolean estaLibre(int dia, int horaInicio, int dur)
    {
        for (int h = horaInicio; h < horaInicio + dur; h++)
        {
            if (h < 0 || h >= 24) return false;
            if (ocupado[dia][h]) return false;
        }
        return true;
    }

    public boolean estaLibreGrupo(Grupos g, int dia, int horaInicio, int dur)
    {
        boolean[][] mat = ocupadoGrupo.get(g.getIdGrupo());

        if (mat == null)
            return true;

        for (int h = horaInicio; h < horaInicio + dur; h++)
        {
            if (h < 0 || h >= 24) return false;
            if (mat[dia][h]) return false;
        }
        return true;
    }

    /**
     * Evita pegar bloques contiguos (solo si quieres esa regla)
     * horaInicio representa inicio del bloque
     */
    public boolean existeBloquePegado(Grupos g, int dia, int horaInicio)
    {
        boolean[][] mat = ocupadoGrupo.get(g.getIdGrupo());

        if (mat == null)
            return false;

        // Antes
        if (horaInicio - 2 >= 0 && mat[dia][horaInicio - 2]) return true;

        // Después
        if (horaInicio + 2 < 24 && mat[dia][horaInicio + 2]) return true;

        return false;
    }

    // ==========================================================
    // MARCAR OCUPADO
    // ==========================================================

    public void ocupar(int dia, int horaInicio, int dur)
    {
        for (int h = horaInicio; h < horaInicio + dur; h++)
        {
            if (h >= 0 && h < 24)
                ocupado[dia][h] = true;
        }
        horasTotales += dur;
    }

    public void ocuparGrupo(Grupos g, int dia, int horaInicio, int dur)
    {
        boolean[][] mat = ocupadoGrupo.get(g.getIdGrupo());

        if (mat == null)
        {
            mat = new boolean[6][24];
            ocupadoGrupo.put(g.getIdGrupo(), mat);
        }

        for (int h = horaInicio; h < horaInicio + dur; h++)
        {
            if (h >= 0 && h < 24)
                mat[dia][h] = true;
        }
    }

    // ==========================================================
    // CONSULTAS
    // ==========================================================

    public int getHorasTotales()
    {
        return horasTotales;
    }

    public boolean[][] getDisponibilidad()
    {
        return disponibilidad;
    }

    public Profesores getProfesor()
    {
        return profesor;
    }
}
