package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import joel.dein.proyectobibliotecadein.BBDD.ConexionBBDD;
import joel.dein.proyectobibliotecadein.DAO.PrestamoDao;
import joel.dein.proyectobibliotecadein.MODEL.AlumnoModel;
import joel.dein.proyectobibliotecadein.MODEL.LibroModel;
import joel.dein.proyectobibliotecadein.MODEL.PrestamoModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de préstamos de libros. Permite registrar nuevos préstamos,
 * validar la información proporcionada por el usuario y generar un reporte con la información
 * del préstamo.
 */
public class PrestamoController {

    @FXML
    private ComboBox<AlumnoModel> cbEstudiante;

    @FXML
    private ComboBox<LibroModel> cbLibro;

    @FXML
    private DatePicker doFechaPrestamo;

    private Runnable onCloseCallback;

    /**
     * Establece el callback que se ejecutará cuando se cierre la ventana.
     *
     * @param onCloseCallback El callback que se ejecutará al cerrar la ventana.
     */
    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    /**
     * Metodo que se ejecuta al inicializar la vista de préstamo. Carga los estudiantes y los libros disponibles.
     */
    @FXML
    public void initialize() {
        cargarEstudiantes();
        cargarLibrosDisponibles();
    }

    /**
     * Carga todos los alumnos disponibles en el ComboBox cbEstudiante.
     */
    private void cargarEstudiantes() {
        cbEstudiante.getItems().setAll(joel.dein.proyectobibliotecadein.DAO.AlumnosDao.getTodosAlumnos());
    }

    /**
     * Carga los libros que no están actualmente en préstamo en el ComboBox cbLibro.
     */
    private void cargarLibrosDisponibles() {
        cbLibro.getItems().setAll(joel.dein.proyectobibliotecadein.DAO.LibroDao.getLibrosDisponibles());
    }

    /**
     * Cancela la operación de préstamo y cierra la ventana.
     *
     * @param event El evento que dispara la acción de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
        Stage stage = (Stage) cbEstudiante.getScene().getWindow();
        stage.close();
    }

    /**
     * Guarda el préstamo, realiza las validaciones necesarias y muestra los mensajes adecuados.
     *
     * @param event El evento que dispara la acción de guardar el préstamo.
     */
    @FXML
    void guardarCambios(ActionEvent event) {
        List<String> errores = new ArrayList<>();

        AlumnoModel alumnoSeleccionado = cbEstudiante.getValue();
        if (alumnoSeleccionado == null) {
            errores.add("Debe seleccionar un estudiante.");
        }

        LibroModel libroSeleccionado = cbLibro.getValue();
        if (libroSeleccionado == null) {
            errores.add("Debe seleccionar un libro disponible.");
        }

        LocalDate fechaSeleccionada = doFechaPrestamo.getValue();
        if (fechaSeleccionada == null) {
            errores.add("Debe seleccionar una fecha de préstamo.");
        } else if (fechaSeleccionada.isBefore(LocalDate.now())) {
            errores.add("La fecha de préstamo no puede ser anterior a la actual.");
        }

        if (!errores.isEmpty()) {
            mostrarError("Errores en el formulario", String.join("\n", errores));
            return;
        }

        // Asignamos los valores necesarios para el informe
        PrestamoModel nuevoPrestamo = new PrestamoModel(
                0,
                alumnoSeleccionado,
                libroSeleccionado,
                fechaSeleccionada.atStartOfDay()
        );

        boolean exito = PrestamoDao.insertPrestamo(nuevoPrestamo);
        if (exito) {
            mostrarInformacion("Éxito", "Préstamo registrado correctamente.");

            // Obtener el ID del préstamo recién creado
            int idPrestamo = nuevoPrestamo.getId_prestamo();  // Asegúrate de que este campo esté correctamente configurado en PrestamoModel

            // Preparar los parámetros para el reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
            parameters.put("ID_PRESTAMO", idPrestamo);  // Pasar el ID del préstamo

            // Generar el reporte
            generarReporte("/JASPERREPORTS/proyectoInforme1.jasper", parameters);

            cancelar(event); // Cierra la ventana tras guardar correctamente
        } else {
            mostrarError("Error", "No se pudo registrar el préstamo. Intente nuevamente.");
        }
    }

    /**
     * Genera un reporte en base al archivo JasperReport proporcionado y los parámetros dados.
     *
     * @param reportePath La ruta del archivo JasperReport.
     * @param parameters Los parámetros necesarios para el reporte.
     */
    private void generarReporte(String reportePath, Map<String, Object> parameters) {
        try {
            ConexionBBDD db = new ConexionBBDD();
            InputStream reportStream = getClass().getResourceAsStream(reportePath);

            if (reportStream == null) {
                System.out.println("El archivo no está ahí: " + reportePath);
                return;
            }

            JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);
            JasperPrint jprint = JasperFillManager.fillReport(report, parameters, db.getConnection());
            JasperViewer viewer = new JasperViewer(jprint, false);
            viewer.setVisible(true);

        } catch (SQLException | JRException e) {
            e.printStackTrace();
            mostrarError("Error en la base de datos", "No se pudo conectar a la base de datos o generar el informe.");
        }
    }

    /**
     * Muestra un cuadro de diálogo con un mensaje de error.
     *
     * @param titulo  El título del cuadro de diálogo.
     * @param mensaje El mensaje a mostrar en el cuadro de diálogo.
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un cuadro de diálogo con un mensaje informativo.
     *
     * @param titulo  El título del cuadro de diálogo.
     * @param mensaje El mensaje a mostrar en el cuadro de diálogo.
     */
    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
