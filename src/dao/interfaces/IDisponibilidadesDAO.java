/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.util.List;
import pruebas.conexion.modelo.Disponibilidad;

/**
 *
 * @author chemo
 */
public interface IDisponibilidadesDAO 
{
    boolean guardarDisponibilidades(List<Disponibilidad> lista);
}
