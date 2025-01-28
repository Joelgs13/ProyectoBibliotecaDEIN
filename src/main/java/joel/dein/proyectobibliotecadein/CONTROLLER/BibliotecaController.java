package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class BibliotecaController {

    @FXML
    private ComboBox<?> cbFiltroHistorico;

    @FXML
    private TableView<?> tablaAlumnos;

    @FXML
    private TableView<?> tablaDevoluciones;

    @FXML
    private TableView<?> tablaHistorico;

    @FXML
    private TableView<?> tablaLibros;

    @FXML
    private TableView<?> tablaPrestamos;

    @FXML
    private TextField tfFiltrarAlumno;

    @FXML
    private TextField tfFiltrarDevolucion;

    @FXML
    private TextField tfFiltrarLibros;

    @FXML
    private TextField tfFiltrarPrestamo;

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
    void borrarLibro(ActionEvent event) {
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
    void filtrarDevolucion(ActionEvent event) {
        // Implementa aquí la funcionalidad de filtro para devoluciones
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
