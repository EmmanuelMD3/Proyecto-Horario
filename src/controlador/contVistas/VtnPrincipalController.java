package controlador.contVistas;

import controlador.contLogica.HorarioControlador;
import controlador.contLogica.HorarioDocente;
import controlador.contLogica.HorarioGrupal;
import controlador.contLogica.HorarioVista;
import dao.impl.*;
import generador.motor.GeneradorHorarios;
import java.io.IOException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import modelo.entidades.*;
import modelo.secundarias.MateriasProfesorTemp;
import util.Validadores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class VtnPrincipalController implements Initializable
{
    // ==========================================
    // VARIABLES
    // ==========================================

    private int profesorSeleccionadoId = -1;
    private Profesores profesorSeleccionado;

    private ObservableList<MateriasProfesorTemp> materiasAsignadasTemp =
            FXCollections.observableArrayList();

    private HorarioControlador horarioControlador;

    // ==========================================
    // FXML
    // ==========================================

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
    private TableColumn<Profesores, Boolean> colActivo;

    @FXML
    private Tab tabProfesores;
    @FXML
    private Button btnGuardarDisponibilidad;
    @FXML
    private TextField txtBuscar;
    @FXML
    private AnchorPane panelHorario;

    @FXML
    private ComboBox<Carreras> comboCarreras;
    @FXML
    private ComboBox<Semestres> comboSemestres;
    @FXML
    private ComboBox<Materias> comboMaterias;

    @FXML
    private TableView<MateriasProfesorTemp> tablaMaterias;
    @FXML
    private Button agregarMateria;
    @FXML
    private Button guardarMaterias;
    @FXML
    private Button eliminarMateria;
    
    @FXML
    private Button btnGenerarHorario;

    @FXML
    private ComboBox<Descargas> comboDescargas;

    @FXML
    private Spinner<Integer> spinnerHorasDescarga;

    
    // ==========================================
    // INICIALIZAR
    // ==========================================

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        inicializarFiltrosTexto();
        inicializarTablaProfesores();
        inicializarEventosTabla();

        cargarCarreras();
        inicializarCombos();
        inicializarTablaAsignaciones();
        inicializarSpinnerDescarga();
        cargarDescargas();
    }

    // ==========================================
    // INICIALIZAR TABLA DE MATERIAS TEMPORALES
    // ==========================================
    private void inicializarSpinnerDescarga()
    {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);

        spinnerHorasDescarga.setValueFactory(valueFactory);
    }

    private void cargarDescargas()
    {
        DescargaProfesorDAOImpl dao = new DescargaProfesorDAOImpl();
        comboDescargas.getItems().setAll(dao.listarDescargas());
    }


    private void inicializarTablaAsignaciones()
    {
        TableColumn<MateriasProfesorTemp, String> colNo = new TableColumn<>("No.");
        colNo.setPrefWidth(40);
        colNo.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(String.valueOf(
                        materiasAsignadasTemp.indexOf(c.getValue()) + 1
                ))
        );

        TableColumn<MateriasProfesorTemp, String> colMateria = new TableColumn<>("Materia");
        colMateria.setPrefWidth(220);
        colMateria.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(c.getValue().getNombreMateria())
        );

        TableColumn<MateriasProfesorTemp, String> colCarrera = new TableColumn<>("Carrera");
        colCarrera.setPrefWidth(180);
        colCarrera.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(c.getValue().getCarrera())
        );

        TableColumn<MateriasProfesorTemp, String> colSemestre = new TableColumn<>("Semestre");
        colSemestre.setPrefWidth(80);
        colSemestre.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(String.valueOf(c.getValue().getSemestre()))
        );

        tablaMaterias.getColumns().clear();
        tablaMaterias.getColumns().addAll(colNo, colMateria, colCarrera, colSemestre);
        tablaMaterias.setItems(materiasAsignadasTemp);
    }


    // ==========================================
    // INICIALIZAR FILTROS Y TABLAS
    // ==========================================

    private void inicializarFiltrosTexto()
    {
        txtIdentificador.textProperty().addListener((obs, old, nuevo) ->
        {
            if (nuevo != null) txtIdentificador.setText(nuevo.toUpperCase());
        });

        Validadores.aplicarFiltroSoloLetras(txtNombre);
        Validadores.aplicarFiltroSoloLetras(txtApellidoPaterno);
        Validadores.aplicarFiltroSoloLetras(txtApellidoMaterno);

        comboEstado.getItems().addAll("Activo", "Inactivo");
        comboEstado.setValue("Seleccione una opción");
    }

    private void inicializarTablaProfesores()
    {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProfesor"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoP"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoM"));
        colIdentificador.setCellValueFactory(new PropertyValueFactory<>("identificador"));
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

    private void inicializarEventosTabla()
    {
        tblBuscar.setRowFactory(tv ->
        {
            TableRow<Profesores> row = new TableRow<>();

            row.setOnMouseClicked(event ->
            {
                if (!row.isEmpty() && event.getClickCount() == 2)
                {
                    profesorSeleccionado = row.getItem();
                    profesorSeleccionadoId = profesorSeleccionado.getIdProfesor();


                    System.out.println("Profesor seleccionado (doble clic): " + profesorSeleccionadoId);

                    cargarDatosEnFormulario(profesorSeleccionado);
                }
            });

            return row;
        });

        // ESTE LISTENER SÍ SE QUEDA COMO LO TIENES
        tblBuscar.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->
        {
            if (newSel == null) return;

            // Esto funciona si seleccionas con 1 clic
            profesorSeleccionadoId = newSel.getIdProfesor();
            System.out.println("Profesor seleccionado (1 clic): " + profesorSeleccionadoId);

            cargarMateriasProfesor(profesorSeleccionadoId);

            DisponibilidadesDAOImpl dao = new DisponibilidadesDAOImpl();
            List<Disponibilidades> lista = dao.obtenerPorProfesor(profesorSeleccionadoId);

            horarioControlador.mostrarDisponibilidadesProfesor(lista);
            btnGuardarDisponibilidad.setDisable(!lista.isEmpty());
            horarioControlador.setBloqueoEdicion(!lista.isEmpty());
        });
    }


    // ==========================================
    // COMBOS
    // ==========================================

    private void inicializarCombos()
    {
        comboCarreras.setOnAction(e ->
        {
            cargarSemestres();
            comboMaterias.getItems().clear();
        });

        comboSemestres.setOnAction(e ->
        {
            Carreras c = comboCarreras.getValue();
            Semestres s = comboSemestres.getValue();

            if (c != null && s != null)
                cargarMaterias(s.getIdSemestre(), c.getIdCarrera());
        });
    }
    // ===============================
    //  ACCIONES
    // ===============================

    @FXML
    private void Aceptar()
    {
        try
        {

            String nombre = txtNombre.getText().trim();
            String apellidoP = txtApellidoPaterno.getText().trim();
            String apellidoM = txtApellidoMaterno.getText().trim();
            String identificador = txtIdentificador.getText().trim();
            String estadoTxt = comboEstado.getValue();

            if (nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() || identificador.isEmpty())
            {
                mostrarAlerta("Campos vacíos", "Por favor completa todos los campos.");
                return;
            }

            if (!identificador.matches("[A-Za-z0-9]+"))
            {
                mostrarAlerta("Identificador inválido", "Debe contener solo letras y números.");
                return;
            }

            boolean estado = estadoTxt.equals("Activo");

            Profesores nuevo = new Profesores(0, nombre, apellidoP, apellidoM, identificador, estado);

            ProfesorDAOImpl dao = new ProfesorDAOImpl();

            if (dao.agregarProfesor(nuevo))
            {
                mostrarAlerta("Éxito", "Profesor agregado correctamente.");
                cargarProfesores();
                limpiarCampos();
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
            mostrarAlerta("Seleccione un profesor", "Debe seleccionar un profesor.");
            return;
        }

        horarioControlador.guardarSeleccionadas(seleccionado.getIdProfesor());
        btnGuardarDisponibilidad.setDisable(true);
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

        if (nombreNuevo.isEmpty() || apellidoPNuevo.isEmpty()
                || apellidoMNuevo.isEmpty() || identificadorNuevo.isEmpty())
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
        agregarCambio(cambios, "Estado",
                profesorSeleccionado.isActivo() ? "Activo" : "Inactivo",
                activoNuevo ? "Activo" : "Inactivo");

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
    private void handleGenerarHorario(ActionEvent event) {
        try {
            HorarioGrupal.generarHorarioCompleto();
            HorarioDocente.generarHorarioGrupal();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("El horario ha sido generado correctamente y guardado como 'HORARIO GRUPALES FINALES.xlsx'.\n y 'HORARIOS POR DOCENTE FINALES.xlsx'.");
            alert.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fallo en la generación del horario.");
            alert.setContentText("Ocurrió un error al intentar generar el archivo Excel: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // ==========================================
    // BOTÓN AGREGAR MATERIA
    // ==========================================

    @FXML
    private void agregarMateria()
    {
        System.out.println("=== DEBUG AGREGAR MATERIA ===");
        System.out.println("profesorSeleccionadoId = " + profesorSeleccionadoId);
        System.out.println("Carrera = " + comboCarreras.getValue());
        System.out.println("Semestre = " + comboSemestres.getValue());
        System.out.println("Materia = " + comboMaterias.getValue());
        System.out.println("Tamaño temporal antes = " + materiasAsignadasTemp.size());

        if (profesorSeleccionadoId == -1)
        {
            mostrarAlerta("Error", "Seleccione un profesor antes de agregar materias.");
            return;
        }

        Carreras carreraSel = comboCarreras.getValue();
        Semestres semSel = comboSemestres.getValue();
        Materias matSel = comboMaterias.getValue();

        if (carreraSel == null || semSel == null || matSel == null)
        {
            mostrarAlerta("Error", "Seleccione carrera, semestre y materia.");
            return;
        }

        for (MateriasProfesorTemp m : materiasAsignadasTemp)
        {
            if (m.getIdMateria() == matSel.getIdMateria())
            {
                mostrarAlerta("Error", "La materia ya está asignada.");
                return;
            }
        }

        materiasAsignadasTemp.add(new MateriasProfesorTemp(
                matSel.getIdMateria(),
                matSel.getNombre(),
                carreraSel.getNombre(),
                semSel.getNumero()
        ));

        tablaMaterias.refresh();

        System.out.println("Materia agregada!");
        System.out.println("Tamaño temporal después = " + materiasAsignadasTemp.size());

    }

    // ==========================================
    // GUARDAR EN BD
    // ==========================================

    @FXML
    private void guardarMaterias()
    {
        if (profesorSeleccionadoId == -1)
        {
            mostrarAlerta("Error", "Seleccione un profesor.");
            return;
        }

        if (materiasAsignadasTemp.isEmpty())
        {
            mostrarAlerta("Error", "No hay materias para guardar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar guardado");
        confirm.setHeaderText("¿Deseas guardar las materias asignadas?");
        confirm.setContentText("Una vez guardadas, no podrás modificarlas.");

        if (confirm.showAndWait().get() != ButtonType.OK)
            return;

        MateriaProfesorDAOImpl dao = new MateriaProfesorDAOImpl();
        for (MateriasProfesorTemp m : materiasAsignadasTemp)
            dao.insertar(profesorSeleccionadoId, m.getIdMateria());

        mostrarInfo("Materias guardadas correctamente.");

        cargarMateriasProfesor(profesorSeleccionadoId);

        Descargas selDesc = comboDescargas.getValue();

        if (selDesc != null)
        {
            int horas = spinnerHorasDescarga.getValue();

            if (horas < 1)
            {
                mostrarAlerta("Error", "Las horas de descarga deben ser mínimo 1.");
                return;
            }

            DescargaProfesorDAOImpl daoDesc = new DescargaProfesorDAOImpl();
            daoDesc.insertar(profesorSeleccionadoId, selDesc.getIdDescarga(), horas);
        }

        agregarMateria.setDisable(true);
        guardarMaterias.setDisable(true);
        eliminarMateria.setDisable(true); // ← CORRECTO

        comboCarreras.getSelectionModel().clearSelection();
        comboSemestres.getSelectionModel().clearSelection();
        comboMaterias.getSelectionModel().clearSelection();

        comboDescargas.getSelectionModel().clearSelection();
        comboDescargas.setPromptText("Seleccione una descarga");

        spinnerHorasDescarga.getValueFactory().setValue(1);
    }

    // ==========================================
    // CARGA DE DATOS
    // ==========================================

    private void cargarProfesores()
    {
        ProfesorDAOImpl dao = new ProfesorDAOImpl();
        ObservableList<Profesores> lista =
                FXCollections.observableArrayList(dao.listarProfesores());

        FilteredList<Profesores> filtro = new FilteredList<>(lista, p -> true);

        txtBuscar.textProperty().addListener((obs, old, nuevo) ->
        {
            filtro.setPredicate(p ->
            {
                if (nuevo == null || nuevo.isEmpty()) return true;

                String f = nuevo.toLowerCase();
                return p.getNombre().toLowerCase().contains(f)
                        || p.getApellidoP().toLowerCase().contains(f)
                        || p.getApellidoM().toLowerCase().contains(f)
                        || p.getIdentificador().toLowerCase().contains(f);
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
            HorarioVista vista = new HorarioVista();
            horarioControlador = new HorarioControlador(vista);
            panelHorario.getChildren().setAll(vista.getRoot());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void cargarDatosEnFormulario(Profesores profesor)
    {
        txtNombre.setText(profesor.getNombre());
        txtApellidoPaterno.setText(profesor.getApellidoP());
        txtApellidoMaterno.setText(profesor.getApellidoM());
        txtIdentificador.setText(profesor.getIdentificador());
        comboEstado.setValue(profesor.isActivo() ? "Activo" : "Inactivo");
    }

    private void cargarCarreras()
    {
        CarreraDAOImpl dao = new CarreraDAOImpl();
        comboCarreras.getItems().setAll(dao.listarCarreras());
    }

    private void cargarSemestres()
    {
        SemestreDAOImpl dao = new SemestreDAOImpl();
        comboSemestres.getItems().setAll(dao.listarTodos());
    }

    private void cargarMaterias(int idSemestre, int idCarrera)
    {
        MateriaDAOImpl dao = new MateriaDAOImpl();
        comboMaterias.getItems().setAll(dao.listarPorSemestreYCarrera(idSemestre, idCarrera));
    }

    // ==========================================
    // UTILIDADES
    // ==========================================

    private void mostrarAlerta(String titulo, String mensaje)
    {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    private void mostrarInfo(String mensaje)
    {
        mostrarAlerta("Información", mensaje);
    }

    private void limpiarCampos()
    {
        txtNombre.clear();
        txtApellidoPaterno.clear();
        txtApellidoMaterno.clear();
        txtIdentificador.clear();
    }

    @FXML
    private void Cancelar()
    {
        limpiarCampos();
    }

    private void cargarMateriasProfesor(int idProfesor)
    {
        MateriaProfesorDAOImpl dao = new MateriaProfesorDAOImpl();
        List<MateriasProfesorTemp> lista = dao.listarMateriasPorProfesor(idProfesor);

        materiasAsignadasTemp.clear();

        if (!lista.isEmpty())
        {
            materiasAsignadasTemp.addAll(lista);

            tablaMaterias.refresh();

            agregarMateria.setDisable(true);
            guardarMaterias.setDisable(true);
            eliminarMateria.setDisable(true);
        }
        else
        {
            materiasAsignadasTemp.clear();
            tablaMaterias.refresh();

            agregarMateria.setDisable(false);
            guardarMaterias.setDisable(false);
            eliminarMateria.setDisable(false);
        }
    }

    @FXML
    private void eliminarMateria()
    {

        if (agregarMateria.isDisable() || guardarMaterias.isDisable())
        {
            mostrarAlerta("Acción no permitida", "No puedes eliminar materias después de guardarlas.");
            return;
        }

        MateriasProfesorTemp seleccion = tablaMaterias.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            mostrarAlerta("Sin selección", "Seleccione una materia de la tabla para eliminarla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Deseas eliminar esta materia antes de guardar?");
        confirm.setContentText("Materia: " + seleccion.getNombreMateria());

        if (confirm.showAndWait().get() != ButtonType.OK)
            return;

        materiasAsignadasTemp.remove(seleccion);
        tablaMaterias.refresh();
    }

    @FXML
    private void generarHorario()
    {
        GeneradorHorarios generador = new GeneradorHorarios();
        generador.generarHorariosAutomaticos();
        
    }

}
