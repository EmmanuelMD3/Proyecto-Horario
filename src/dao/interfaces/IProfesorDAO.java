package dao.interfaces;

import java.util.List;
import modelo.entidades.Profesores;

public interface IProfesorDAO
{

    boolean agregarProfesor(Profesores profesor);

    boolean actualizarProfesor(Profesores profesor);

    boolean eliminarProfesor(int idProfesor);

    Profesores buscarPorId(int idProfesor);

    List<Profesores> listarProfesores();
}
