package dao.interfaces;

import java.util.List;
import modelo.secundarias.ReporteGrupoCabecera;
import modelo.secundarias.ReporteGrupoHorario;

public interface IHorarioReporteDAO {
    
    List<ReporteGrupoCabecera> asignacionesDetallePorCiclo(int idCiclo);
    
    List<ReporteGrupoHorario> obtenerBloquesPorAsignacion(int idAsignacion);
}
