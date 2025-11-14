package controlador.contVistas;

import controlador.contLogica.HorarioControlador;
import controlador.contLogica.HorarioVista;
import dao.impl.ProfesorDAOImpl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import modelo.entidades.Profesores;
import util.Validadores;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 * FXML Controller class
 *
 * @author chemo
 */
public class VtnPrincipalController implements Initializable
{

    @FXML
    private ComboBox<String> comboEstado;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidoPaterno;
    @FXML
    private TextField txtApellidoMaterno;
    @FXML
    private TextField txtIdentificador;
    @FXML
    private CheckBox checkAsignar;
    @FXML
    private CheckBox checkNoAsignar;
    @FXML
    private TableView<Profesores> tblBuscar;
    @FXML
    private TableColumn<Profesores, Integer> colId;
    @FXML
    private TableColumn<Profesores, String> colNombre;
    @FXML
    private TableColumn<Profesores, String> colApellidoPaterno;
    @FXML
    private TableColumn<Profesores, String> colApellidoMaterno;
    @FXML
    private TableColumn<Profesores, String> colIdentificador;
    @FXML
    private TableColumn<Profesores, Integer> colHorasDescarga;
    @FXML
    private TableColumn<Profesores, Boolean> colActivo;
    @FXML
    private Tab tabProfesores;
    //@FXML
    //private TableView<Profesores> tablaProfesores;
    @FXML
    private TextField txtBuscar;
    @FXML
    private AnchorPane panelHorario;
    private controlador.contLogica.HorarioControlador horarioControlador;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        comboEstado.getItems().addAll(
                "Activo",
                "Inactivo"
        );
        comboEstado.setValue("Seleccione una opción");

        Validadores.aplicarFiltroSoloLetras(txtNombre);
        Validadores.aplicarFiltroSoloLetras(txtApellidoPaterno);
        Validadores.aplicarFiltroSoloLetras(txtApellidoMaterno);

        Validadores.configurarCheckBoxes(checkAsignar, checkNoAsignar);

        colId.setCellValueFactory(new PropertyValueFactory<>("idProfesor"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoP"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoM"));
        colIdentificador.setCellValueFactory(new PropertyValueFactory<>("identificador"));
        colHorasDescarga.setCellValueFactory(new PropertyValueFactory<>("horasDescarga"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        tabProfesores.setOnSelectionChanged(event ->
        {
            if (tabProfesores.isSelected())
            {
                cargarProfesores();
                cargarVistaHorario();
            }
        });

    }

    @FXML
    private void Aceptar()
    {
        try
        {
            int hrsdescarga = 0;
            String nombre = txtNombre.getText().trim();
            String apellidoP = txtApellidoPaterno.getText().trim();
            String apellidoM = txtApellidoMaterno.getText().trim();
            String identificador = txtIdentificador.getText().trim();
            String estadotxt = comboEstado.getValue();

            boolean asignar = checkAsignar.isSelected();
            boolean noAsignar = checkNoAsignar.isSelected();

            if (nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() || identificador.isEmpty())
            {
                mostrarAlerta("Campos vacíos", "Por favor completa todos los campos de texto.");
                return;
            }

            if (!Validadores.soloLetrasValidas(nombre)
                    || !Validadores.soloLetrasValidas(apellidoP)
                    || !Validadores.soloLetrasValidas(apellidoM))
            {
                mostrarAlerta("Formato inválido", "Los nombres y apellidos solo deben contener letras.");
                return;
            }

            if (!identificador.matches("\\d+"))
            {
                mostrarAlerta("Identificador inválido", "El identificador debe contener solo números.");
                return;
            }

            if (estadotxt == null || estadotxt.equals("Seleccione una opción"))
            {
                mostrarAlerta("Estado inválido", "Debes seleccionar un estado válido (Activo/Inactivo).");
                return;
            }

            if (!asignar && !noAsignar)
            {
                mostrarAlerta("Selección requerida", "Debes seleccionar si el profesor se asignará o no.");
                return;
            }
            if (asignar)
            {
                hrsdescarga = 1;
            }

            boolean estado = estadotxt.equals("Activo");

            Profesores nuevoProfesor = new Profesores(0, nombre, apellidoP, apellidoM, identificador, hrsdescarga, estado);
            ProfesorDAOImpl daoProfesor = new ProfesorDAOImpl();

            if (daoProfesor.agregarProfesor(nuevoProfesor))
            {
                mostrarAlerta("Éxito", "Profesor agregado correctamente.");
                cargarProfesores();
                limpiarCampos();
            } else
            {
                mostrarAlerta("Error", "No se pudo agregar el profesor.");
            }

        } catch (Exception e)
        {
            mostrarAlerta("Error inesperado", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarDisponibilidad()
    {
        if (horarioControlador != null)
        {
            horarioControlador.guardarSeleccionadas();
        } else
        {
            System.err.println("El controlador de horario no está inicializado.");
        }
    }

    @FXML
    private void Modificar()
    {

    }

    @FXML
    private void Cancelar()
    {
        limpiarCampos();
    }

    private void mostrarAlerta(String titulo, String mensaje)
    {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void limpiarCampos()
    {
        txtNombre.clear();
        txtApellidoPaterno.clear();
        txtApellidoMaterno.clear();
        txtIdentificador.clear();
        //comboEstado.setValue("Seleccione una opción");
        checkAsignar.setSelected(false);
        checkNoAsignar.setSelected(false);
    }

    private void cargarProfesores()
    {
        ProfesorDAOImpl daoProfesor = new ProfesorDAOImpl();
        ObservableList<Profesores> listaProfesores = FXCollections.observableArrayList(daoProfesor.listarProfesores());

        FilteredList<Profesores> filtro = new FilteredList<>(listaProfesores, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) ->
        {
            filtro.setPredicate(profesor ->
            {
                if (newValue == null || newValue.isEmpty())
                {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (profesor.getNombre().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                } else if (profesor.getApellidoP().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                } else if (profesor.getApellidoM().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                } else if (profesor.getIdentificador().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                return false;
            });
        });

        SortedList<Profesores> ordenada = new SortedList<>(filtro);
        ordenada.comparatorProperty().bind(tblBuscar.comparatorProperty());
        tblBuscar.setItems(ordenada);
    }

    private void cargarVistaHorario()
    {
        try
        {
            controlador.contLogica.HorarioVista vistaHorario = new controlador.contLogica.HorarioVista();
            horarioControlador = new controlador.contLogica.HorarioControlador(vistaHorario);

            panelHorario.getChildren().clear();
            panelHorario.getChildren().add(vistaHorario.getRoot());

            AnchorPane.setTopAnchor(vistaHorario.getRoot(), 0.0);
            AnchorPane.setBottomAnchor(vistaHorario.getRoot(), 0.0);
            AnchorPane.setLeftAnchor(vistaHorario.getRoot(), 0.0);
            AnchorPane.setRightAnchor(vistaHorario.getRoot(), 0.0);

        } catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Error al cargar horario: " + e.getMessage());
        }
    }

}
