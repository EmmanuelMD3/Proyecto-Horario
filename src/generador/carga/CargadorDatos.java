package generador.carga;

import generador.modelo.DiaSemana;
import generador.modelo.TareaHorario;
import modelo.entidades.*;
import dao.impl.*;

import java.util.ArrayList;
import java.util.List;

public class CargadorDatos
{

    private MateriaProfesorDAOImpl daoMatProf = new MateriaProfesorDAOImpl();
    private DescargaProfesorDAOImpl daoDescProf = new DescargaProfesorDAOImpl();
    private DescargasDAOImpl daoDescargas = new DescargasDAOImpl();
    private GruposDAOImpl daoGrupos = new GruposDAOImpl();
    private MateriaDAOImpl daoMaterias = new MateriaDAOImpl();
    private ProfesorDAOImpl daoProfesores = new ProfesorDAOImpl();

    /**
     * Carga todas las tareas desde la base de datos:
     * - Clases por grupo (según mapa curricular)
     * - Horas de descarga por profesor
     */
    public List<TareaHorario> cargarTareas()
    {

        List<TareaHorario> tareas = new ArrayList<>();

        // ======================================================
        // 1) Cargar tareas de CLASE (materias del mapa curricular)
        // ======================================================
        List<Grupos> grupos = daoGrupos.listarTodos();

        for (Grupos g : grupos)
        {

            // materias del semestre/carrera del grupo
            List<Materias> materiasGrupo =
                    daoMaterias.listarPorSemestreYCarrera(
                            g.getIdSemestre(), g.getIdCarrera()
                    );

            for (Materias mat : materiasGrupo)
            {
                List<Profesores> profesAsignables =
                        daoMatProf.buscarProfesoresParaMateria(mat.getIdMateria());

                if (profesAsignables.isEmpty())
                {
                    System.out.println("[ADVERTENCIA] Ningún profesor puede dar la materia: " + mat.getNombre());
                    continue;
                }

                // Elegir profesor (simple por ahora: primero de la lista)
                Profesores profesor = profesAsignables.get(0);

                // Convertir horas_semana a bloques de 2h + 1h
                int horas = mat.getHoras_semana();

                while (horas > 2)
                {
                    tareas.add(new TareaHorario(
                            TareaHorario.Tipo.CLASE,
                            2, profesor, g, mat, null
                    ));
                    horas -= 2;
                }

                if (horas > 0)
                {
                    tareas.add(new TareaHorario(
                            TareaHorario.Tipo.CLASE,
                            horas, profesor, g, mat, null
                    ));
                }
            }
        }


        List<DescargaProfesor> listaDesc = daoDescProf.listarTodas();

        for (DescargaProfesor dp : listaDesc)
        {

            Profesores profesor = daoProfesores.buscarProfesorPorId(dp.getIdProfesor());
            Descargas descarga = daoDescargas.buscarPorId(dp.getIdDescarga());

            for (int i = 0; i < dp.getHoras_asignadas(); i++)
            {

                tareas.add(new TareaHorario(
                        TareaHorario.Tipo.DESCARGA,
                        1,
                        profesor,
                        null,
                        null,
                        descarga
                ));
            }
        }

        return tareas;
    }
}
