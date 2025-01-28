package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import joel.dein.proyectobibliotecadein.DAO.AlumnosDao;
import joel.dein.proyectobibliotecadein.DAO.HistoricoPrestamoDao;
import joel.dein.proyectobibliotecadein.DAO.LibroDao;
import joel.dein.proyectobibliotecadein.DAO.PrestamoDao;
import joel.dein.proyectobibliotecadein.MODEL.AlumnoModel;
import joel.dein.proyectobibliotecadein.MODEL.HistoricoPrestamoModel;
import joel.dein.proyectobibliotecadein.MODEL.LibroModel;
import joel.dein.proyectobibliotecadein.MODEL.PrestamoModel;
import joel.dein.proyectobibliotecadein.BBDD.ConexionBBDD;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class BibliotecaController implements Initializable {


    @FXML
    private ComboBox<?> cbFiltroHistorico;

    @FXML
    private TableView<AlumnoModel> tablaAlumnos;

    @FXML
    private TableView<LibroModel> tablaLibros;

    @FXML
    private TableView<PrestamoModel> tablaPrestamos;

    @FXML
    private TableView<HistoricoPrestamoModel> tablaHistorico;

    @FXML
    private TextField tfFiltrarAlumno;

    @FXML
    private TextField tfFiltrarDevolucion;

    @FXML
    private TextField tfFiltrarLibros;

    @FXML
    private TextField tfFiltrarPrestamo;

    @FXML
    private TableColumn<HistoricoPrestamoModel, String> tcAlumnoTabHistoricoPrestamos;

    @FXML
    private TableColumn<HistoricoPrestamoModel, String> tcApellido1TabAlumnos;

    @FXML
    private TableColumn<HistoricoPrestamoModel, String> tcApellido2TabAlumnos;

    @FXML
    private TableColumn<LibroModel, String> tcAutorTabLibros;

    @FXML
    private TableColumn<LibroModel, Boolean> tcBajaTabLibros;

    @FXML
    private TableColumn<AlumnoModel, String> tcDNITabAlumnos;

    @FXML
    private TableColumn<LibroModel, String> tcEditorialTabLibros;

    @FXML
    private TableColumn<LibroModel, String> tcEstadoTabLibros;

    @FXML
    private TableColumn<HistoricoPrestamoModel, LocalDateTime> tcFechaDevolucionTabHistoricoPrestamos;

    @FXML
    private TableColumn<HistoricoPrestamoModel, LocalDateTime> tcFechaPrestamoTabHistoricoPrestamos;
    @FXML
    public TableColumn<PrestamoModel, String> tcAlumnoTabPrestamos;

    @FXML
    private TableColumn<PrestamoModel, LocalDateTime> tcFechaPrestamoTabPrestamos;

    @FXML
    private TableColumn<HistoricoPrestamoModel, String> tcLibroTabHistoricoPrestamos;

    @FXML
    private TableColumn<PrestamoModel, String> tcLibroTabPrestamos;

    @FXML
    private TableColumn<AlumnoModel, String> tcNombreTabAlumnos;

    @FXML
    private TableColumn<LibroModel, String> tcTituloTabLibros;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("iniciando...");
        try {
            ConexionBBDD con = new ConexionBBDD();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cargarDatosTablas();
    }

    void cargarDatosTablas() {
        // Tabla de alumnos
        List<AlumnoModel> alumnos = AlumnosDao.getTodosAlumnos();
        tablaAlumnos.getItems().setAll(alumnos);
        tcDNITabAlumnos.setCellValueFactory(new PropertyValueFactory<>("dni"));
        tcNombreTabAlumnos.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcApellido1TabAlumnos.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        tcApellido2TabAlumnos.setCellValueFactory(new PropertyValueFactory<>("apellido2"));

        // Tabla de libros (con baja = 0)
        List<LibroModel> libros = LibroDao.getTodosLibrosConBajaA0();
        tablaLibros.getItems().setAll(libros);
        tcTituloTabLibros.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tcAutorTabLibros.setCellValueFactory(new PropertyValueFactory<>("autor"));
        tcEditorialTabLibros.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        tcEstadoTabLibros.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tcBajaTabLibros.setCellValueFactory(new PropertyValueFactory<>("baja"));

        // Tabla de préstamos
        List<PrestamoModel> prestamos = PrestamoDao.getTodosPrestamo();
        tablaPrestamos.getItems().setAll(prestamos);
        tcFechaPrestamoTabPrestamos.setCellValueFactory(new PropertyValueFactory<>("fecha_prestamo"));
        tcAlumnoTabPrestamos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlumno().getDni()));
        tcLibroTabPrestamos.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLibro().getCodigo())));

        // Tabla de histórico de préstamos
        List<HistoricoPrestamoModel> historicoPrestamos = HistoricoPrestamoDao.getTodosHistorialPrestamo();
        tablaHistorico.getItems().setAll(historicoPrestamos);
        tcFechaPrestamoTabHistoricoPrestamos.setCellValueFactory(new PropertyValueFactory<>("fecha_prestamo"));
        tcFechaDevolucionTabHistoricoPrestamos.setCellValueFactory(new PropertyValueFactory<>("fecha_devolucion"));
        tcAlumnoTabHistoricoPrestamos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlumno().getDni()));
        tcLibroTabHistoricoPrestamos.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLibro().getCodigo())));
    }

    /*
    * Metodo que sirve para cargar una nueva pantalla
    * */
    private <T> T cargarPantalla(String rutaFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();

            // Devuelve el controlador asociado al archivo FXML
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Maneja el error devolviendo null
        }
    }


    @FXML
    void aniadirAlumno(ActionEvent event) {
        AlumnosController alumnosController = cargarPantalla("/JXML/alumnos.fxml", "Añadir Alumno");

        if (alumnosController != null) {
            // Configurar el callback para actualizar la tabla
            alumnosController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }

    public void aniadirLibro(ActionEvent event) {
        cargarPantalla("/JXML/libros.fxml", "Añadir Libro");
    }
    @FXML
    void prestarLibro(ActionEvent event) {
        cargarPantalla("/JXML/prestamo.fxml", "Prestar Libro");
    }

    @FXML
    void modificarAlumno(ActionEvent event) {
        // Este mtodo está vacío, indícalo si necesita alguna funcionalidad
    }

    @FXML
    void bajaLibro(ActionEvent event) {
        // Este mtodo está vacío, indícalo si necesita alguna funcionalidad
    }

    @FXML
    void devolverLibro(ActionEvent event) {
        // Este mtodo está vacío, indícalo si necesita alguna funcionalidad
    }

    @FXML
    void filtrarAlumno(ActionEvent event) {
        // Implementa aquí la funcionalidad de filtro para alumnos
    }

    @FXML
    void filtrarHistorico(ActionEvent event) {
        // Implementa aquí la funcionalidad de filtro para el histórico
    }

    @FXML
    void filtrarLibros(ActionEvent event) {
        // Implementa aquí la funcionalidad de filtro para libros
    }

    @FXML
    void filtrarPrestamo(ActionEvent event) {
        // Implementa aquí la funcionalidad de filtro para préstamos
    }

    @FXML
    void modificarLibro(ActionEvent event) {
        // Implementa aquí la funcionalidad para modificar un libro
    }

    public void idiomaEspaniol(ActionEvent event) {
        // Implementa el cambio de idioma a español
    }

    public void idiomaIngles(ActionEvent event) {
        // Implementa el cambio de idioma a inglés
    }

    public void cargarInforme2(ActionEvent event) {
        // Implementa la funcionalidad para cargar Informe 2
    }

    public void cargarInforme3(ActionEvent event) {
        // Implementa la funcionalidad para cargar Informe 3
    }

    public void cargarInforme4(ActionEvent event) {
        // Implementa la funcionalidad para cargar Informe 4
    }

    public void cargarGuia(ActionEvent event) {
        // Implementa la funcionalidad para cargar una guía
    }


}
