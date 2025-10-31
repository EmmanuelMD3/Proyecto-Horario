package dao.interfaces;

import java.util.List;
import modelo.entidades.Profesor;

public interface IProfesorDAO
{

    boolean agregarProfesor(Profesor profesor);

    boolean actualizarProfesor(Profesor profesor);

    boolean eliminarProfesor(int idProfesor);

    Profesor buscarPorId(int idProfesor);

    List<Profesor> listarProfesores();
}
