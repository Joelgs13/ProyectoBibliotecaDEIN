package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import joel.dein.proyectobibliotecadein.DAO.PrestamoDao;
import joel.dein.proyectobibliotecadein.MODEL.AlumnoModel;
import joel.dein.proyectobibliotecadein.MODEL.LibroModel;
import joel.dein.proyectobibliotecadein.MODEL.PrestamoModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrestamoController {

    @FXML
    private ComboBox<AlumnoModel> cbEstudiante;

    @FXML
    private ComboBox<LibroModel> cbLibro;

    @FXML
    private DatePicker doFechaPrestamo;

    private Runnable onCloseCallback;
    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }
    /**
     * Método que se ejecuta al inicializar la vista de préstamo.
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
     * Cancela la operación y cierra la ventana.
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
     * Guarda los cambios realizando las validaciones necesarias.
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

        PrestamoModel nuevoPrestamo = new PrestamoModel(
                0,
                alumnoSeleccionado,
                libroSeleccionado,
                fechaSeleccionada.atStartOfDay()
        );

        boolean exito = PrestamoDao.insertPrestamo(nuevoPrestamo);
        if (exito) {
            mostrarInformacion("Éxito", "Préstamo registrado correctamente.");
            cancelar(event); // Cierra la ventana tras guardar correctamente
        } else {
            mostrarError("Error", "No se pudo registrar el préstamo. Intente nuevamente.");
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


        /*Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
        generarReporte("/JASPERREPORTS/proyectoInforme1.jasper", parameters);
        parameters.put("dni_alumno", selectedDni); // DNI del estudiante
        parameters.put("codigo_libro", selectedCodigoLibro); // Código del libro
        parameters.put("fecha_prestamo", selectedDate); // Fecha del préstamo*/