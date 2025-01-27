package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import joel.dein.proyectobibliotecadein.BBDD.ConexionBBDD;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PrestamoController {

    @FXML
    private ComboBox<?> cbEstudiante;

    @FXML
    private ComboBox<?> cbLibro;

    @FXML
    private DatePicker doFechaPrestamo;

    @FXML
    void cancelar(ActionEvent event) {

    }

    @FXML
    void guardarCambios(ActionEvent event) {
        //se genera el pdfd el prestamo: y como datos hay que pasar las claver primarias y las fechas
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
        generarReporte("/JasperReport/InformePConCalculos.jasper", parameters);
        //parameters.put("dni_alumno", selectedDni); // DNI del estudiante
        //parameters.put("codigo_libro", selectedCodigoLibro); // Código del libro
        //parameters.put("fecha_prestamo", selectedDate); // Fecha del préstamo

    }

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
     * @param titulo El título del cuadro de diálogo.
     * @param mensaje El mensaje a mostrar en el cuadro de diálogo.
     */
    private void mostrarError(String titulo, String mensaje) {
        // Crear una ventana emergente de tipo "error"
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // No queremos un encabezado
        alert.setContentText(mensaje); // El mensaje que queremos mostrar
        alert.showAndWait(); // Mostrar el mensaje y esperar a que el usuario lo cierre
    }
}
