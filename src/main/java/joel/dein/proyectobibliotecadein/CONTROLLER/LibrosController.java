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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controlador para gestionar la creación y modificación de libros en la interfaz de usuario.
 * Permite editar los detalles de un libro, incluyendo título, autor, editorial, estado y la imagen de portada.
 */
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


    private LibroModel libroSeleccionado;

    /**
     * Establece el libro seleccionado y carga sus datos en la interfaz de usuario.
     *
     * @param libroSeleccionado El libro que se va a editar.
     */
    public void setLibroSeleccionado(LibroModel libroSeleccionado) {
        this.libroSeleccionado = libroSeleccionado;
        cargarDatosLibro(libroSeleccionado); // Cargar los datos del libro en los campos de la interfaz
    }

    /**
     * Establece un callback que se ejecutará cuando se cierre la ventana.
     *
     * @param onCloseCallback El callback que se ejecutará al cerrar la ventana.
     */
    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    /**
     * Metodo que se ejecuta al inicializar el controlador.
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

    /**
     * Cancela la operación actual y cierra la ventana del libro.
     *
     * @param event El evento que dispara la acción de cancelar.
     */
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

    /**
     * Carga los datos de un libro seleccionado en los campos correspondientes de la interfaz.
     *
     * @param libroSeleccionado El libro cuyos datos se van a cargar.
     */
    public void cargarDatosLibro(LibroModel libroSeleccionado) {
        if (libroSeleccionado != null) {
            // Rellenar los campos con los datos del libro seleccionado
            tfTituloLibro.setText(libroSeleccionado.getTitulo());
            tfAutorLibro.setText(libroSeleccionado.getAutor());
            tfEditorialLibro.setText(libroSeleccionado.getEditorial());
            cbEstadoLibro.setValue(libroSeleccionado.getEstado());
            cbBajaLibro.setSelected(libroSeleccionado.getBaja() == 1);

            // Cargar la imagen
            byte[] imagenBytes = BlobABytes(libroSeleccionado.getImagen());
            if (imagenBytes != null && imagenBytes.length > 0) {
                Image image = new Image(new ByteArrayInputStream(imagenBytes));
                ivImagenDePortada.setImage(image);
                this.imagenBytes = imagenBytes;
            } else {
                ivImagenDePortada.setImage(new Image(String.valueOf(getClass().getResource(DEFAULT_IMAGE_PATH))));
            }
            //System.out.print(libroSeleccionado.getCodigo());
        }
    }

    /**
     * Convierte un Blob de imagen a un array de bytes.
     *
     * @param blob El Blob de imagen a convertir.
     * @return El array de bytes resultante de la conversión.
     */
    public byte[] BlobABytes(java.sql.Blob blob) {
        if (blob == null) {
            return null;
        }
        try {
            InputStream inputStream = blob.getBinaryStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace(); // Si ocurre un error, imprímelo para depurar
            return null;
        }
    }

    /**
     * Guarda los cambios realizados en los datos del libro.
     * Si el libro ya existe, se actualiza; si no, se inserta un nuevo libro.
     *
     * @param event El evento que dispara la acción de guardar cambios.
     */
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

            // Si el libro tiene un ID, significa que es una edición, si no, es una inserción
            if (libroSeleccionado != null && libroSeleccionado.getCodigo() != 0) {
                libro.setCodigo(libroSeleccionado.getCodigo());  // Asignar el código del libro para editarlo
                LibroDao.updateLibro(libro);
                mostrarAlerta("Éxito", "El libro ha sido modificado correctamente.");
            } else {
                LibroDao.insertLibro(libro);
                mostrarAlerta("Éxito", "El libro ha sido registrado correctamente.");
            }

            // Cerrar la ventana después de guardar
            cancelar(event);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo registrar o modificar el libro: " + e.getMessage());
        }
    }

    /**
     * Borra la imagen de portada del libro y la reemplaza por la imagen por defecto.
     *
     * @param event El evento que dispara la acción de borrar la imagen.
     */
    @FXML
    void borrarImagen(ActionEvent event) {
        // Establecer la imagen por defecto y su representación en bytes
        ivImagenDePortada.setImage(new Image(String.valueOf(getClass().getResource(DEFAULT_IMAGE_PATH))));
        imagenBytes = convertirImagenABytes(new Image(String.valueOf(getClass().getResource(DEFAULT_IMAGE_PATH))));
    }

    /**
     * Permite al usuario seleccionar una imagen desde su sistema de archivos para el libro.
     *
     * @param event El evento que dispara la acción de añadir una imagen.
     */
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
     * Metodo para convertir una imagen a un array de bytes.
     *
     * @param image La imagen que se va a convertir.
     * @return El array de bytes resultante de la conversión.
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

    /**
     * Muestra una alerta con un título y un mensaje en la interfaz de usuario.
     *
     * @param titulo   El título de la alerta.
     * @param mensaje  El mensaje de la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
