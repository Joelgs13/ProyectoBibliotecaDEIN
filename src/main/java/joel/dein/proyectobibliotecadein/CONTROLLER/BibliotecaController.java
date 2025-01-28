package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private void cargarDatosTablas() {
        // Cargar datos de alumnos
        List<AlumnoModel> alumnos = AlumnosDao.getTodosAlumnos();
        tablaAlumnos.getItems().setAll(alumnos);

        // Cargar datos de libros con baja = 0
        List<LibroModel> libros = LibroDao.getTodosLibrosConBajaA0();
        tablaLibros.getItems().setAll(libros);

        // Cargar datos de préstamos
        List<PrestamoModel> prestamos = PrestamoDao.getTodosPrestamo();
        tablaPrestamos.getItems().setAll(prestamos);

        // Cargar datos del histórico de préstamos
        List<HistoricoPrestamoModel> historicoPrestamos = HistoricoPrestamoDao.getTodosHistorialPrestamo();
        tablaHistorico.getItems().setAll(historicoPrestamos);
    }

    /*
    * Metodo que sirve para cargar una nueva pantalla
    * */
    private void cargarPantalla(String rutaFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Aquí puedes añadir un diálogo de error si lo necesitas
        }
    }

    @FXML
    void aniadirLibro(ActionEvent event) {
        cargarPantalla("/JXML/libros.fxml", "Añadir Libro");
    }

    @FXML
    void aniadirAlumno(ActionEvent event) {
        cargarPantalla("/JXML/alumnos.fxml", "Modificar Alumno");
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
