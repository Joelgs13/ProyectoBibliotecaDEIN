package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import joel.dein.proyectobibliotecadein.DAO.HistoricoPrestamoDao;
import joel.dein.proyectobibliotecadein.DAO.LibroDao;
import joel.dein.proyectobibliotecadein.DAO.PrestamoDao;
import joel.dein.proyectobibliotecadein.MODEL.HistoricoPrestamoModel;
import joel.dein.proyectobibliotecadein.MODEL.PrestamoModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Controlador para la devolución de libros en la biblioteca.
 * Este controlador maneja la interfaz de usuario para procesar la devolución de un libro,
 * actualizar su estado y registrar el evento en el historial de préstamos.
 */
public class DevolucionController {

    @FXML
    private ComboBox<String> cbEstadoLibro;

    @FXML
    private DatePicker doFechaDevolucion;

    private PrestamoModel prestamoSeleccionado;
    private Runnable onCloseCallback;

    /**
     * Inicializa el controlador, configurando los posibles estados de un libro.
     */
    @FXML
    public void initialize() {
        // Cargar los estados posibles del libro en el ComboBox
        cbEstadoLibro.getItems().addAll(
                "Nuevo", "Usado nuevo", "Usado seminuevo", "Usado estropeado", "Restaurado"
        );
    }

    /**
     * Establece el préstamo seleccionado en el controlador y ajusta la interfaz según los datos del préstamo.
     *
     * @param prestamo El préstamo seleccionado que se va a procesar.
     */
    public void setPrestamo(PrestamoModel prestamo) {
        this.prestamoSeleccionado = prestamo;
        if (prestamo != null) {
            // Establecer el estado actual del libro en el ComboBox
            cbEstadoLibro.setValue(prestamo.getLibro().getEstado());
        }
    }

    /**
     * Cancela la devolución y cierra la ventana actual.
     *
     * @param event El evento que dispara la acción de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    /**
     * Guarda los cambios realizados en la devolución del libro.
     * El proceso incluye validar la fecha de devolución, eliminar el préstamo,
     * actualizar el estado del libro y registrar la devolución en el historial de préstamos.
     *
     * @param event El evento que dispara la acción de guardar cambios.
     */
    @FXML
    void guardarCambios(ActionEvent event) {
        if (prestamoSeleccionado == null) {
            mostrarAlerta("Error", "No se ha seleccionado ningún préstamo.", Alert.AlertType.ERROR);
            return;
        }

        LocalDate fechaDevolucion = doFechaDevolucion.getValue();

        if (fechaDevolucion == null) {
            mostrarAlerta("Error", "Debe seleccionar una fecha de devolución.", Alert.AlertType.ERROR);
            return;
        }

        LocalDateTime fechaDevolucionDateTime = fechaDevolucion.atStartOfDay();

        if (fechaDevolucionDateTime.isBefore(prestamoSeleccionado.getFecha_prestamo())) {
            mostrarAlerta("Error", "La fecha de devolución no puede ser anterior a la fecha de préstamo.", Alert.AlertType.ERROR);
            return;
        }

        // Eliminar el préstamo de la base de datos
        boolean eliminado = PrestamoDao.deletePrestamo(prestamoSeleccionado.getId_prestamo());

        if (!eliminado) {
            mostrarAlerta("Error", "No se pudo eliminar el préstamo de la base de datos.", Alert.AlertType.ERROR);
            return;
        }

        // Si el estado del libro ha cambiado, actualizarlo en la base de datos
        String nuevoEstado = cbEstadoLibro.getValue();
        if (nuevoEstado != null && !nuevoEstado.equals(prestamoSeleccionado.getLibro().getEstado())) {
            boolean actualizado = LibroDao.updateLibroEstado(prestamoSeleccionado.getLibro().getCodigo(), nuevoEstado);
            if (!actualizado) {
                mostrarAlerta("Error", "No se pudo actualizar el estado del libro.", Alert.AlertType.ERROR);
                return;
            }
        }

        // Insertar en el historial de préstamos
        HistoricoPrestamoModel historial = new HistoricoPrestamoModel(
                prestamoSeleccionado.getId_prestamo(),
                prestamoSeleccionado.getAlumno(),
                prestamoSeleccionado.getLibro(),
                prestamoSeleccionado.getFecha_prestamo(),
                fechaDevolucionDateTime
        );

        boolean insertado = HistoricoPrestamoDao.insertHistorialPrestamo(historial);

        if (!insertado) {
            mostrarAlerta("Error", "No se pudo registrar el préstamo en el historial.", Alert.AlertType.ERROR);
            return;
        }

        mostrarAlerta("Éxito", "El préstamo ha sido devuelto correctamente.", Alert.AlertType.INFORMATION);
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual y ejecuta la callback asociada, si existe.
     */
    private void cerrarVentana() {
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
        Stage stage = (Stage) cbEstadoLibro.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una ventana emergente de alerta con un mensaje y título proporcionados.
     *
     * @param titulo  El título de la alerta.
     * @param mensaje El mensaje de la alerta.
     * @param tipo    El tipo de la alerta (por ejemplo, ERROR, INFORMATION).
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Establece la callback que se ejecutará cuando se cierre la ventana.
     *
     * @param callback La función que se ejecutará al cerrar la ventana.
     */
    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }
}
