package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import joel.dein.proyectobibliotecadein.DAO.LibroDao;
import joel.dein.proyectobibliotecadein.MODEL.LibroModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LibrosController {



    @FXML
    private CheckBox cbBajaLibro;

    @FXML
    private ComboBox<String> cbEstadoLibro; // Asumimos que contiene opciones como "Disponible", "Prestado", etc.

    @FXML
    private ImageView ivImagenDePortada;

    @FXML
    private TextField tfAutorLibro;

    @FXML
    private TextField tfEditorialLibro;

    @FXML
    private TextField tfTituloLibro;

    private Runnable onCloseCallback; // Callback para actualizar datos al cerrar

    private final String DEFAULT_IMAGE_PATH = "/IMG/libroIcono.png"; // Ruta de la imagen por defecto

    private byte[] imagenBytes; // Variable para almacenar la imagen como array de bytes

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Carga los valores en el ComboBox de estado.
     */
    @FXML
    public void initialize() {
        ObservableList<String> estados = FXCollections.observableArrayList(
                "Nuevo",
                "Usado nuevo",
                "Usado seminuevo",
                "Usado estropeado",
                "Restaurado"
        );
        cbEstadoLibro.setItems(estados);

    }

    @FXML
    void cancelar(ActionEvent event) {
        // Ejecutar el callback si está configurado
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }

        // Cerrar la ventana
        Stage stage = (Stage) tfTituloLibro.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardarCambios(ActionEvent event) {
        StringBuilder errores = new StringBuilder();

        // Validar los campos
        String titulo = tfTituloLibro.getText();
        String autor = tfAutorLibro.getText();
        String editorial = tfEditorialLibro.getText();
        String estado = cbEstadoLibro.getValue(); // Estado seleccionado
        int baja = cbBajaLibro.isSelected() ? 1 : 0;

        if (titulo == null || titulo.isEmpty()) {
            errores.append("- El título no puede estar vacío.\n");
        }

        if (autor == null || autor.isEmpty()) {
            errores.append("- El autor no puede estar vacío.\n");
        }

        if (editorial == null || editorial.isEmpty()) {
            errores.append("- La editorial no puede estar vacía.\n");
        }

        if (estado == null || estado.isEmpty()) {
            errores.append("- Debes seleccionar un estado para el libro.\n");
        }

        if (imagenBytes == null || imagenBytes.length == 0) {
            errores.append("- Debes seleccionar una imagen válida.\n");
        }

        if (errores.length() > 0) {
            mostrarAlerta("Errores de Validación", errores.toString());
            return;
        }

        // Crear el objeto LibroModel
        LibroModel libro = new LibroModel();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setEstado(estado);
        libro.setBaja(baja);

        // Convertir la imagen a Blob antes de insertar
        try {
            libro.setImagen(LibroDao.convertirABlob(imagenBytes));
            LibroDao.insertLibro(libro);
            mostrarAlerta("Éxito", "El libro ha sido registrado correctamente.");

            // Cerrar la ventana después de guardar
            cancelar(event);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo registrar el libro: " + e.getMessage());
        }
    }

    @FXML
    void borrarImagen(ActionEvent event) {
        // Establecer la imagen por defecto y su representación en bytes
        ivImagenDePortada.setImage(new Image(String.valueOf(getClass().getResource(DEFAULT_IMAGE_PATH))));
        imagenBytes = convertirImagenABytes(new Image(String.valueOf(getClass().getResource(DEFAULT_IMAGE_PATH))));
    }

    @FXML
    void aniadirImagen(ActionEvent event) {
        // Abrir un explorador de archivos para seleccionar una imagen
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        // Obtener el archivo seleccionado
        File file = fileChooser.showOpenDialog(tfTituloLibro.getScene().getWindow());
        if (file != null) {
            try {
                // Verificar el tamaño del archivo
                long fileSize = Files.size(file.toPath());
                if (fileSize > 64 * 1024) { // 64 KB en bytes
                    mostrarAlerta("Error", "La imagen seleccionada es demasiado grande. Debe ser menor a 64 KB.");
                    return; // No continuar con la carga de la imagen
                }

                // Establecer la imagen seleccionada en el ImageView
                Image image = new Image(file.toURI().toString());
                ivImagenDePortada.setImage(image);

                // Convertir la imagen seleccionada a un array de bytes
                imagenBytes = Files.readAllBytes(file.toPath());
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo cargar la imagen: " + e.getMessage());
            }
        }
    }


    /**
     * Método para convertir una imagen a un array de bytes.
     */
    private byte[] convertirImagenABytes(Image image) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(DEFAULT_IMAGE_PATH);

            if (inputStream == null) {
                mostrarAlerta("Error", "No se pudo encontrar el recurso.");
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo convertir la imagen: " + e.getMessage());
            return null;
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
