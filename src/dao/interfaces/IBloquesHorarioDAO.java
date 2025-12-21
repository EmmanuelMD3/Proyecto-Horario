package dao.interfaces;

import java.util.List;

import modelo.secundarias.ReporteGrupoHorario;


public interface IBloquesHorarioDAO
{
    
    //Lista de todos los horarios
    List<ReporteGrupoHorario> obtenerBloquesPorAsignacion(int idAsignacion);
    //Lista de las descargas
    List<ReporteGrupoHorario> obtenerBloquesDescarga(int idProfesor, String nombreDescarga);
}
