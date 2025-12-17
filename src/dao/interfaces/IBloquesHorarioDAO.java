package dao.interfaces;

import java.util.List;

import modelo.secundarias.ReporteGrupoHorario;


public interface IBloquesHorarioDAO
{
    
    List<ReporteGrupoHorario> obtenerBloquesPorAsignacion(int idAsignacion);

}
