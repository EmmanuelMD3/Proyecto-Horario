package pruebas;

import dao.impl.CicloDAOImpl;
import dao.impl.ProfesorDAOImpl;    
import modelo.entidades.Ciclos;
import modelo.entidades.Profesores;

import java.sql.Date;
import java.util.List;

public class TestProfesorDAO
{

    public static void main(String[] args)
    {

        CicloDAOImpl dao = new CicloDAOImpl();

        Ciclos nuevo = new Ciclos(0, "2025-A", "Par",
                Date.valueOf("2025-01-15"), Date.valueOf("2025-06-30"));
        dao.agregarCiclo(nuevo);

        List<Ciclos> ciclos = dao.listarCiclos();
        for (Ciclos c : ciclos)
        {
            System.out.println(c);
        }

        Ciclos buscado = dao.buscarPorId(1);
        if (buscado != null)
        {
            System.out.println("Encontrado: " + buscado);
        }

        if (buscado != null)
        {
            buscado.setNombre("2025-B");
            dao.actualizarCiclo(buscado);
        }

//        ProfesorDAOImpl dao = new ProfesorDAOImpl();
//
//        List<Profesores> profesores = dao.listarProfesores();
//
//        if(profesores != null)
//        {
//            for (Profesores profesor : profesores)
//            {
//                System.out.println(profesor.toString());
//            }
//        }
//        else
//        {
//            System.out.println("Profesor no encontrado");
//        }

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
