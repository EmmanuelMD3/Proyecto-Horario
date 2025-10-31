package pruebas;

import dao.impl.ProfesorDAOImpl;
import modelo.entidades.Profesor;

public class TestProfesorDAO
{

    public static void main(String[] args)
    {

        ProfesorDAOImpl dao = new ProfesorDAOImpl();

        if(dao.eliminarProfesor(2))
        {
            System.out.println("Profesor eliminado");
        }
        else
        {
            System.out.println("Profesor no encontrado");
        }

        /*ProfesorDAOImpl dao = new ProfesorDAOImpl();

        Profesor newProfesor = new Profesor();

        //Profesor nuevo = new Profesor(0, "Arturo", "Martinez", "Rosales", "arturo@gmail.com", "7224238662", 6, true);
//
//        if (dao.agregarProfesor(newProfesor))
//        {
//            System.out.println("Profesor agregado exitosamente");
//        } else
//        {
//            System.out.println("Error al agregar profesor");
//        }

        newProfesor = dao.buscarPorId(1);
        /*newProfesor.setNombre("Jesus");
        newProfesor.setApellidoM("Martinez");

        var id = newProfesor.getIdProfesor();
        var nombre = "Benajmin";
        var apellidoP = "Sanchez";
        var apellidoM = "Gonzales";
        var correo = newProfesor.getCorreo();
        var telefono = newProfesor.getTelefono();
        var horasDescarga = newProfesor.getHorasDescarga();
        var activo = newProfesor.isActivo();

        Profesor nuevoProfesor = new Profesor(id,nombre,apellidoP,apellidoM,correo,telefono,horasDescarga,activo);

        if (dao.actualizarProfesor(nuevoProfesor))
        {
            System.out.println("Profesor actualizado");
        } else
        {
            System.out.println("Error al actualizar");
        }*/
    }
}
