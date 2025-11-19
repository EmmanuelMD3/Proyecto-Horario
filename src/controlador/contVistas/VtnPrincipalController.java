package controlador.contVistas;

import controlador.contLogica.HorarioControlador;
import controlador.contLogica.HorarioVista;
import dao.impl.DisponibilidadesDAOImpl;
import dao.impl.ProfesorDAOImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import modelo.entidades.Disponibilidades;
import modelo.entidades.Profesores;
import util.Validadores;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import javax.swing.*;

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
    @FXML
    private Button btnGuardarDisponibilidad;
    @FXML
    private TextField txtBuscar;
    @FXML
    private AnchorPane panelHorario;
    private controlador.contLogica.HorarioControlador horarioControlador;

    private Profesores profesorSeleccionado;

    @FXML
    private TextField txtBuscar1;


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        txtIdentificador.textProperty().addListener((obs, oldText, newText) ->
        {
            if (newText != null && !newText.matches("[A-Za-z0-9]*"))
            {
                txtIdentificador.setText(oldText.toUpperCase());
            }
            else
            {
                txtIdentificador.setText(newText.toUpperCase());
            }
        });


        txtIdentificador.textProperty().addListener((obs, oldText, newText) ->
        {
            if (newText != null)
            {
                txtIdentificador.setText(newText.toUpperCase());
            }
        });


        tblBuscar.setRowFactory(tv ->
        {
            TableRow<Profesores> row = new TableRow<>();

            row.setOnMouseClicked(event ->
            {
                if (!row.isEmpty() && event.getClickCount() == 2)
                {

                    profesorSeleccionado = row.getItem();

                    cargarDatosEnFormulario(profesorSeleccionado);
                }
            });

            return row;
        });


        tblBuscar.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->
        {
            if (newSel != null)
            {

                int idProfesor = newSel.getIdProfesor();

                DisponibilidadesDAOImpl dao = new DisponibilidadesDAOImpl();
                List<Disponibilidades> lista = dao.obtenerPorProfesor(idProfesor);

                horarioControlador.mostrarDisponibilidadesProfesor(lista);

                if (lista.isEmpty())
                {
                    btnGuardarDisponibilidad.setDisable(false);
                    horarioControlador.setBloqueoEdicion(false);
                    System.out.println("Profesor sin disponibilidad → puede registrar.");
                }
                else
                {
                    btnGuardarDisponibilidad.setDisable(true);
                    horarioControlador.setBloqueoEdicion(true);
                    System.out.println("Profesor YA tiene disponibilidad → edición bloqueada.");
                }
            }
        });


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

        inicializaTabla();
    }

    private void inicializaTabla()
    {
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

            if (!identificador.matches("[A-Za-z0-9]+"))
            {
                mostrarAlerta("Identificador inválido", "El identificador debe contener solo letras y números (A-Z, 0-9).");
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
            }
            else
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
        Profesores seleccionado = tblBuscar.getSelectionModel().getSelectedItem();

        if (seleccionado == null)
        {
            mostrarAlerta("Seleccione un profesor", "Debe seleccionar un profesor antes de guardar disponibilidad.");
            return;
        }

        int idProfesor = seleccionado.getIdProfesor();

        if (horarioControlador != null)
        {
            horarioControlador.guardarSeleccionadas(idProfesor);

            DisponibilidadesDAOImpl dao = new DisponibilidadesDAOImpl();
            List<Disponibilidades> lista = dao.obtenerPorProfesor(idProfesor);

            horarioControlador.mostrarDisponibilidadesProfesor(lista);

            btnGuardarDisponibilidad.setDisable(true);
            horarioControlador.setBloqueoEdicion(true);

            System.out.println("Disponibilidad guardada → Edición bloqueada.");
        }
        else
        {
            System.err.println("El controlador de horario no está inicializado.");
        }
    }


    @FXML
    private void Modificar()
    {
        if (profesorSeleccionado == null)
        {
            mostrarAlerta("Sin selección", "Debe hacer doble clic en un profesor para modificarlo.");
            return;
        }

        String nombreNuevo = txtNombre.getText().trim();
        String apellidoPNuevo = txtApellidoPaterno.getText().trim();
        String apellidoMNuevo = txtApellidoMaterno.getText().trim();
        String identificadorNuevo = txtIdentificador.getText().trim();
        boolean activoNuevo = comboEstado.getValue().equals("Activo");
        int horasDescargaNueva = checkAsignar.isSelected() ? 1 : 0;

        if (nombreNuevo.isEmpty() || apellidoPNuevo.isEmpty() || apellidoMNuevo.isEmpty() || identificadorNuevo.isEmpty())
        {
            mostrarAlerta("Campos incompletos", "Debe llenar todos los campos antes de modificar.");
            return;
        }

        if (!Validadores.soloLetrasValidas(nombreNuevo) ||
                !Validadores.soloLetrasValidas(apellidoPNuevo) ||
                !Validadores.soloLetrasValidas(apellidoMNuevo))
        {
            mostrarAlerta("Formato inválido", "Nombre y apellidos deben contener solo letras.");
            return;
        }

        if (!identificadorNuevo.matches("[A-Za-z0-9]+"))
        {
            mostrarAlerta("Identificador inválido", "Debe contener solo letras y números.");
            return;
        }

        StringBuilder cambios = new StringBuilder("Cambios detectados:\n\n");

        agregarCambio(cambios, "Nombre", profesorSeleccionado.getNombre(), nombreNuevo);
        agregarCambio(cambios, "Apellido Paterno", profesorSeleccionado.getApellidoP(), apellidoPNuevo);
        agregarCambio(cambios, "Apellido Materno", profesorSeleccionado.getApellidoM(), apellidoMNuevo);
        agregarCambio(cambios, "Identificador", profesorSeleccionado.getIdentificador(), identificadorNuevo);
        agregarCambio(cambios, "Estado", profesorSeleccionado.isActivo() ? "Activo" : "Inactivo",
                activoNuevo ? "Activo" : "Inactivo");
        agregarCambio(cambios, "Horas de Descarga",
                profesorSeleccionado.getHorasDescarga() == 1 ? "Asignar" : "No Asignar",
                horasDescargaNueva == 1 ? "Asignar" : "No Asignar");

        if (cambios.toString().equals("Cambios detectados:\n\n"))
        {
            mostrarAlerta("Sin cambios", "No modificaste ningún dato.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar modificación");
        alert.setHeaderText("¿Deseas modificar este profesor?");
        alert.setContentText(cambios.toString());

        if (alert.showAndWait().get() != ButtonType.OK)
        {
            return;
        }

        profesorSeleccionado.setNombre(nombreNuevo);
        profesorSeleccionado.setApellidoP(apellidoPNuevo);
        profesorSeleccionado.setApellidoM(apellidoMNuevo);
        profesorSeleccionado.setIdentificador(identificadorNuevo);
        profesorSeleccionado.setActivo(activoNuevo);
        profesorSeleccionado.setHorasDescarga(horasDescargaNueva);

        ProfesorDAOImpl dao = new ProfesorDAOImpl();
        dao.actualizarProfesor(profesorSeleccionado);

        mostrarAlerta("Realizado", "Los datos del profesor fueron modificados.");
        cargarProfesores();
    }

    private void agregarCambio(StringBuilder sb, String campo, String viejo, String nuevo)
    {
        if (!viejo.equals(nuevo))
        {
            sb.append(campo)
                    .append(":\n   • Antes: ").append(viejo)
                    .append("\n   • Ahora:  ").append(nuevo)
                    .append("\n\n");
        }
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
                }
                else
                    if (profesor.getApellidoP().toLowerCase().contains(lowerCaseFilter))
                    {
                        return true;
                    }
                    else
                        if (profesor.getApellidoM().toLowerCase().contains(lowerCaseFilter))
                        {
                            return true;
                        }
                        else
                            if (profesor.getIdentificador().toLowerCase().contains(lowerCaseFilter))
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

    private void cargarDatosEnFormulario(Profesores profesor)
    {
        txtNombre.setText(profesor.getNombre());
        txtApellidoPaterno.setText(profesor.getApellidoP());
        txtApellidoMaterno.setText(profesor.getApellidoM());
        txtIdentificador.setText(profesor.getIdentificador());

        comboEstado.setValue(profesor.isActivo() ? "Activo" : "Inactivo");

        if (profesor.getHorasDescarga() == 1)
        {
            checkAsignar.setSelected(true);
            checkNoAsignar.setSelected(false);
        }
        else
        {
            checkAsignar.setSelected(false);
            checkNoAsignar.setSelected(true);
        }
    }


    private void mostrarDialogoModificar(Profesores profesor)
    {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modificar Profesor");
        alert.setHeaderText("¿Deseas modificar los datos de este profesor?");
        alert.setContentText(profesor.getNombre() + " " + profesor.getApellidoP());

        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");

        alert.getButtonTypes().setAll(botonSi, botonNo);

        alert.showAndWait().ifPresent(tipo ->
        {
            if (tipo == botonSi)
            {
                profesorSeleccionado = profesor;
                cargarDatosEnFormulario(profesor);
            }
        });
    }
}
