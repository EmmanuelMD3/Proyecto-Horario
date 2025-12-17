package dao.interfaces;

import java.util.List;
import modelo.secundarias.ReporteDescargas;
import modelo.secundarias.ReporteGrupoCabecera;
import modelo.secundarias.ReporteGrupoHorario;

public interface IHorarioReporteDAO {
    
    //Metodos para el horario grupal
    List<ReporteGrupoCabecera> asignacionesDetallePorCiclo(int idCiclo);
    
    //Metodos para el horario por docente
    
    List<ReporteGrupoCabecera> todasAsignacionesPorCiclo(int idCiclo);
    List<ReporteDescargas> obtenerDescargasPorDocente(int idProfesor);
    
    
}
