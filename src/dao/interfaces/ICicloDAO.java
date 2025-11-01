package dao.interfaces;

import java.util.List;

import modelo.entidades.Ciclos;

public interface ICicloDAO
{

    boolean agregarCiclo(Ciclos ciclo);

    boolean actualizarCiclo(Ciclos ciclo);

    boolean eliminarCiclo(int idCiclo);

    Ciclos buscarPorId(int idCiclo);

    List<Ciclos> listarCiclos();

    Ciclos obtenerCicloActivo();

    boolean desactivarTodos();
}
