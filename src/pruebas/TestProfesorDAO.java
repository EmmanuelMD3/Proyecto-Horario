package pruebas;

import dao.impl.ProfesorDAOImpl;
import modelo.entidades.Profesor;

public class TestProfesorDAO
{

    public static void main(String[] args)
    {
        ProfesorDAOImpl dao = new ProfesorDAOImpl();

        Profesor newProfesor = new Profesor(0, "Jesus", "Mares", "Montes", "jesus@gmail.com", "7224238662", 5, true);   

        if (dao.agregarProfesor(newProfesor))
        {
            System.out.println("Profesor agregado exitosamente");
        } else
        {
            System.out.println("Error al agregar profesor");
        }
    }
}
