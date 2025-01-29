package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import joel.dein.proyectobibliotecadein.DAO.AlumnosDao;
import joel.dein.proyectobibliotecadein.MODEL.AlumnoModel;

public class AlumnosController {

    @FXML
    private TextField tfDniAlumno;

    @FXML
    private TextField tfNombreAlumno;

    @FXML
    private TextField tfPrimerApellidoAlumno;

    @FXML
    private TextField tfSegundoApellidoAlumno;

    // Referencia al controlador principal

    private Runnable onCloseCallback;

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    @FXML
    void cancelar(ActionEvent event) {
        // Ejecutar el callback si está configurado
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }

        // Cerrar la ventana
        Stage stage = (Stage) tfDniAlumno.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardarAlumno(ActionEvent event) {
        StringBuilder errores = new StringBuilder();

        // Validar los campos
        String dni = tfDniAlumno.getText().toUpperCase();
        String nombre = tfNombreAlumno.getText();
        String primerApellido = tfPrimerApellidoAlumno.getText();
        String segundoApellido = tfSegundoApellidoAlumno.getText();

        if (dni == null || dni.isEmpty() || !dni.matches("\\d{8}[A-Za-z]")) {
            errores.append("- El DNI debe tener 8 números seguidos de una letra (ejemplo: 12345678A).\n");
        }

        if (nombre == null || nombre.isEmpty()) {
            errores.append("- El nombre no puede estar vacío.\n");
        }

        if (primerApellido == null || primerApellido.isEmpty()) {
            errores.append("- El primer apellido no puede estar vacío.\n");
        }

        if (segundoApellido == null || segundoApellido.isEmpty()) {
            errores.append("- El segundo apellido no puede estar vacío.\n");
        }

        // Mostrar errores si los hay
        if (errores.length() > 0) {
            mostrarAlerta("Errores de Validación", errores.toString());
            return;
        }

        // Crear un objeto AlumnoModel
        AlumnoModel alumno = new AlumnoModel();
        alumno.setDni(dni);
        alumno.setNombre(nombre);
        alumno.setApellido1(primerApellido);
        alumno.setApellido2(segundoApellido);

        // Insertar el alumno en la base de datos
        try {
            AlumnosDao.insertAlumno(alumno);
            mostrarAlerta("Éxito", "El alumno ha sido registrado correctamente.");

            // Cerrar la ventana después de guardar
            cancelar(event);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo registrar el alumno: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
