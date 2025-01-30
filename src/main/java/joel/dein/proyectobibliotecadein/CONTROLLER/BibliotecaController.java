package joel.dein.proyectobibliotecadein.CONTROLLER;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador principal de la biblioteca. Se encarga de gestionar la interfaz de usuario
 * y la interacción con la base de datos en el sistema de préstamos de libros.
 */
public class BibliotecaController implements Initializable {
    @FXML
    public TextField tfFiltrarHistoricoPrestamo;

    @FXML
    private TableColumn<LibroModel, ImageView> tcImagenTabLibros;

    @FXML
    private ComboBox<String> cbFiltroHistorico;

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
    /**
     * Metodo de inicialización del controlador.
     *
     * @param location  La URL de localización.
     * @param resources Recursos adicionales.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("iniciando...");
        try {
            ConexionBBDD con = new ConexionBBDD();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cargarDatosTablas();
        cargarFiltrosHistorico(); // Nuevo metodo para cargar el ComboBox
    }


    /**
     * Carga los datos de las tablas desde la base de datos.
     */
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
        tcImagenTabLibros.setCellValueFactory(cellData -> {
            LibroModel libro = cellData.getValue();
            ImageView imageView = new ImageView(blobToImage(blobToBytes(libro.getImagen())));

            // Ajustar el tamaño de la imagen en la celda
            imageView.setFitWidth(50);  // Ancho deseado
            imageView.setFitHeight(50); // Alto deseado
            imageView.setPreserveRatio(true);

            return new SimpleObjectProperty<>(imageView);
        });

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

        // 🔹 Ahora muestra el título en lugar del código 🔹
        tcLibroTabHistoricoPrestamos.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLibro().getTitulo())
        );
    }

    /**
     * Convierte un objeto Blob en un array de bytes.
     *
     * @param blob El objeto Blob a convertir.
     * @return Un array de bytes representando la imagen.
     */
    private byte[] blobToBytes(Blob blob) {
        if (blob == null) {
            return null; // Si el blob es nulo, devolvemos null
        }
        try (InputStream inputStream = blob.getBinaryStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convierte un array de bytes en una imagen.
     *
     * @param imageBytes El array de bytes que representa la imagen.
     * @return Un objeto de tipo Image.
     */
    private Image blobToImage(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            // Si no hay imagen, usar una imagen por defecto
            return new Image(getClass().getResource("/IMG/libroIcono.png").toString());
        }

        return new Image(new ByteArrayInputStream(imageBytes));
    }


    /**
     * Carga una nueva pantalla.
     *
     * @param rutaFXML Ruta del archivo FXML.
     * @param titulo   Título de la nueva ventana.
     * @param <T>      Tipo del controlador de la pantalla cargada.
     * @return El controlador de la nueva pantalla.
     */
    private <T> T cargarPantalla(String rutaFXML, String titulo) {
        try {
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");

// Cargar el recurso de idioma adecuado utilizando el archivo de propiedadess
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("LANGUAGES/lang", locale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML), bundle);
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

    /**
     * Metodo encargado de abrir la pantalla para añadir un nuevo alumno.
     * Al cerrar la pantalla, se recargan los datos de la tabla de alumnos.
     *
     * @param event El evento generado por el botón de añadir alumno.
     */
    @FXML
    void aniadirAlumno(ActionEvent event) {
        AlumnosController alumnosController = cargarPantalla("/JXML/alumnos.fxml", "Añadir Alumno");

        if (alumnosController != null) {
            // Configurar el callback para actualizar la tabla
            alumnosController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }

    /**
     * Metodo encargado de abrir la pantalla para añadir un nuevo libro.
     * Al cerrar la pantalla, se recargan los datos de la tabla de libros.
     *
     * @param event El evento generado por el botón de añadir libro.
     */
    @FXML
    public void aniadirLibro(ActionEvent event) {
        LibrosController librosController = cargarPantalla("/JXML/libros.fxml", "Añadir Libro");

        if (librosController != null){
            librosController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }

    /**
     * Metodo encargado de abrir la pantalla para gestionar un préstamo de libro.
     * Al cerrar la pantalla, se recargan los datos de la tabla de préstamos.
     *
     * @param event El evento generado por el botón de prestar libro.
     */
    @FXML
    void prestarLibro(ActionEvent event) {
        PrestamoController prestamoController = cargarPantalla("/JXML/prestamo.fxml", "Prestar Libro");

        if (prestamoController != null){
            prestamoController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }

    /**
     * Filtra la tabla de alumnos por nombre según el texto ingresado en el campo de búsqueda.
     *
     * @param event El evento generado por el campo de búsqueda de alumnos.
     */
    @FXML
    void filtrarAlumno(ActionEvent event) {
        String filtro = tfFiltrarAlumno.getText().trim().toLowerCase();

        List<AlumnoModel> alumnosFiltrados = AlumnosDao.getTodosAlumnos().stream()
                .filter(alumno -> alumno.getNombre().toLowerCase().contains(filtro))
                .collect(Collectors.toList());

        tablaAlumnos.getItems().setAll(alumnosFiltrados);
    }

    /**
     * Filtra la tabla de libros por título según el texto ingresado en el campo de búsqueda.
     *
     * @param event El evento generado por el campo de búsqueda de libros.
     */
    @FXML
    void filtrarLibros(ActionEvent event) {
        String filtro = tfFiltrarLibros.getText().trim().toLowerCase();

        List<LibroModel> librosFiltrados = LibroDao.getTodosLibrosConBajaA0().stream()
                .filter(libro -> libro.getTitulo().toLowerCase().contains(filtro))
                .collect(Collectors.toList());

        tablaLibros.getItems().setAll(librosFiltrados);
    }

    /**
     * Filtra la tabla de préstamos por fecha según el texto ingresado en el campo de búsqueda.
     *
     * @param event El evento generado por el campo de búsqueda de préstamos.
     */
    @FXML
    void filtrarPrestamo(ActionEvent event) {
        String filtro = tfFiltrarPrestamo.getText().trim();

        List<PrestamoModel> prestamosFiltrados = PrestamoDao.getTodosPrestamo().stream()
                .filter(prestamo -> prestamo.getFecha_prestamo().toString().contains(filtro))
                .collect(Collectors.toList());

        tablaPrestamos.getItems().setAll(prestamosFiltrados);
    }

    /**
     * Configura el filtro para la tabla de historial de préstamos, permitiendo filtrar por DNI de alumno o título de libro.
     */
    private void cargarFiltrosHistorico() {
        ObservableList<String> filtros = FXCollections.observableArrayList("Por DNI Del alumno", "Por Título de libro");
        cbFiltroHistorico.setItems(filtros);
        cbFiltroHistorico.getSelectionModel().selectFirst(); // Seleccionar el primer valor por defecto
    }

    /**
     * Filtra la tabla de historial de préstamos según el texto ingresado y el criterio seleccionado.
     *
     * @param event El evento generado por el campo de búsqueda de historial de préstamos.
     */
    @FXML
    void filtrarHistoricoPrestamo(ActionEvent event) {
        String filtro = tfFiltrarHistoricoPrestamo.getText().trim().toLowerCase();
        String criterio = cbFiltroHistorico.getSelectionModel().getSelectedItem();

        List<HistoricoPrestamoModel> historicoFiltrado = HistoricoPrestamoDao.getTodosHistorialPrestamo().stream()
                .filter(historico -> {
                    if ("Por DNI Del alumno".equals(criterio)) {
                        return historico.getAlumno().getDni().toLowerCase().contains(filtro);
                    } else if ("Por Título de libro".equals(criterio)) {
                        return historico.getLibro().getTitulo().toLowerCase().contains(filtro);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        tablaHistorico.getItems().setAll(historicoFiltrado);
    }

    /**
     * Genera el informe con base en el reporte Jasper proporcionado y los parámetros definidos.
     *
     * @param event El evento generado por el botón para cargar el informe 2.
     */
    public void cargarInforme2(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
        parameters.put("SUBREPORT_PATH", getClass().getResource("/JASPERREPORTS/").toString());
        generarReporte("/JASPERREPORTS/proyectoInforme2.jasper", parameters);
    }

    /**
     * Genera el informe con base en el reporte Jasper proporcionado y los parámetros definidos.
     *
     * @param event El evento generado por el botón para cargar el informe 3.
     */
    public void cargarInforme3(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
        parameters.put("SUBREPORT_PATH", getClass().getResource("/JASPERREPORTS/").toString());
        generarReporte("/JASPERREPORTS/proyectoInforme3.jasper", parameters);
    }

    /**
     * Genera el informe con base en el reporte Jasper proporcionado y los parámetros definidos.
     *
     * @param event El evento generado por el botón para cargar el informe 4.
     */
    public void cargarInforme4(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
        generarReporte("/JASPERREPORTS/proyectoInforme4.jasper", parameters);
    }

    /**
     * Metodo encargado de cargar una guía o manual.
     *
     * @param event El evento generado por el botón para cargar una guía.
     */

    public void cargarGuia(ActionEvent event) {
        // Implementa la funcionalidad para cargar una guía
    }

    /**
     * Genera y muestra un reporte en formato Jasper.
     *
     * @param reportePath El path del reporte Jasper.
     * @param parameters Los parámetros a incluir en el reporte.
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
     * Metodo encargado de abrir la ventana para modificar un alumno seleccionado.
     * Al cerrar la pantalla, se recargan los datos de la tabla de alumnos.
     *
     * @param event El evento generado por el botón de modificar alumno.
     */
    @FXML
    void modificarAlumno(ActionEvent event) {
        // Obtener el alumno seleccionado
        AlumnoModel alumnoSeleccionado = tablaAlumnos.getSelectionModel().getSelectedItem();

        // Si no hay alumno seleccionado, mostramos un error
        if (alumnoSeleccionado == null) {
            mostrarAlerta("Error", "No has seleccionado ningún alumno", "Por favor, selecciona un alumno para modificar.");
            return;
        }

        // Si el alumno está seleccionado, cargar la ventana de modificación
        AlumnosController alumnosController = cargarPantalla("/JXML/alumnos.fxml", "Modificar Alumno");

        if (alumnosController != null) {
            // Pasar los datos del alumno seleccionado al controlador de la ventana de alumnos
            alumnosController.cargarDatosAlumno(alumnoSeleccionado);

            // Configurar el callback para actualizar la tabla cuando la ventana se cierre
            alumnosController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }

    /**
     * Metodo encargado de dar de baja un libro seleccionado.
     * Muestra un cuadro de confirmación antes de realizar la acción.
     *
     * @param event El evento generado por el botón de baja de libro.
     */
    @FXML
    void bajaLibro(ActionEvent event) {
        // Primero, obtenemos el libro seleccionado en la tabla
        LibroModel libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        // Si no hay libro seleccionado, mostramos un mensaje de error
        if (libroSeleccionado == null) {
            mostrarAlerta("Error", "No has seleccionado ningún libro", "Por favor, selecciona un libro para dar de baja.");
            return;
        }

        // Si hay libro seleccionado, mostramos una ventana emergente de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Baja");
        alert.setHeaderText("¿Estás seguro de que deseas dar de baja el libro?");
        alert.setContentText("Libro: " + libroSeleccionado.getTitulo());

        // Si el usuario confirma, llamamos al metodo bajaDelLibro
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Llamamos al metodo bajaDelLibro para dar de baja el libro en la base de datos
                boolean exito = LibroDao.bajaDelLibro(libroSeleccionado.getCodigo());

                // Mostramos un mensaje dependiendo si la operación fue exitosa o no
                if (exito) {
                    mostrarAlerta("Éxito", "Libro dado de baja", "El libro ha sido dado de baja correctamente.");
                    // Aquí podríamos actualizar la tabla para reflejar los cambios
                    cargarDatosTablas();
                } else {
                    mostrarAlerta("Error", "No se pudo dar de baja el libro", "Hubo un error al intentar dar de baja el libro.");
                }
            }
        });
    }


    /**
     * Metodo para mostrar alertas informativas.
     *
     * @param titulo El título de la alerta.
     * @param cabecera El texto de la cabecera.
     * @param contenido El contenido principal de la alerta.
     */
    private void mostrarAlerta(String titulo, String cabecera, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(cabecera);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }


    /**
     * Metodo encargado de abrir la pantalla para modificar un libro seleccionado.
     * Si no hay libro seleccionado, muestra un mensaje de error.
     *
     * @param event El evento generado por el botón de modificar libro.
     */
    @FXML
    void modificarLibro(ActionEvent event) {
        // Obtener el libro seleccionado de la tabla
        LibroModel libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        // Verificar si se ha seleccionado un libro
        if (libroSeleccionado == null) {
            mostrarAlerta("Error", "No has seleccionado ningún libro", "Por favor, selecciona un libro para modificar.");
            return;
        }

        // Cargar la ventana de modificación con los datos del libro seleccionado
        LibrosController librosController = cargarPantalla("/JXML/libros.fxml", "Modificar Libro");

        if (librosController != null) {
            // Pasar el libro seleccionado al controlador para modificarlo
            librosController.setLibroSeleccionado(libroSeleccionado);
        }

        if (librosController != null){
            librosController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }

    /**
     * Metodo encargado de gestionar la devolución de un libro prestado.
     * Al seleccionar un préstamo, se abre la pantalla de devolución y se pasa el préstamo seleccionado.
     *
     * @param event El evento generado por el botón de devolución de libro.
     */
    @FXML
    void devolverLibro(ActionEvent event) {
        // Obtener el préstamo seleccionado en la tabla
        PrestamoModel prestamoSeleccionado = tablaPrestamos.getSelectionModel().getSelectedItem();

        // Verificar que se haya seleccionado un préstamo
        if (prestamoSeleccionado == null) {
            mostrarAlerta("Error", "No se pudo devolver el libro","Debes seleccionar un préstamo antes de devolver un libro.");
            return;
        }

        // Cargar la pantalla de devolución
        DevolucionController devolucionController = cargarPantalla("/JXML/devolucion.fxml", "Devolución de un libro");

        if (devolucionController != null) {
            // Pasar el préstamo seleccionado al controlador de devolución
            devolucionController.setPrestamo(prestamoSeleccionado);

            // Configurar el callback para actualizar la tabla al cerrar
            devolucionController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }


    /**
     * Metodo que cambia el idioma de la aplicación a español.
     * Este metodo se llama cuando el usuario selecciona el idioma español.
     * Al activarse, se guarda el idioma "es" en la base de datos y luego se
     * actualiza la ventana para reflejar el cambio de idioma.
     *
     * @param event El evento que activa este metodo (generalmente un clic en un botón).
     */
    public void idiomaEspaniol(ActionEvent event) {
        ConexionBBDD.guardarIdioma("es");
        Stage stage = (Stage) tfFiltrarAlumno.getScene().getWindow();
        this.actualizarVentana(stage);
    }
    /**
     * Metodo que cambia el idioma de la aplicación a inglés.
     * Este metodo se llama cuando el usuario selecciona el idioma inglés.
     * Al activarse, se guarda el idioma "en" en la base de datos y luego se
     * actualiza la ventana para reflejar el cambio de idioma.
     *
     * @param event El evento que activa este metodo (generalmente un clic en un botón).
     */
    public void idiomaIngles(ActionEvent event) {
        ConexionBBDD.guardarIdioma("en");
        Stage stage = (Stage) tfFiltrarAlumno.getScene().getWindow();
        this.actualizarVentana(stage);
    }

    /**
     * Actualiza la ventana actual con el nuevo idioma seleccionado.
     * Este metodo recarga el archivo FXML correspondiente y aplica el nuevo idioma
     * a la interfaz de usuario, obteniendo las traducciones desde un
     * `ResourceBundle`. El idioma se carga desde la base de datos y se aplica
     * a la ventana principal.
     *
     * @param stage El escenario (ventana) que se actualizará con el nuevo idioma.
     */
    public void actualizarVentana(Stage stage) {
        try {
            // Cargar las propiedades de idioma y establecer el nuevo locale
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("LANGUAGES/lang", locale);

            // Cargar el archivo FXML de la ventana principal con el nuevo ResourceBundle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/JXML/biblioteca.fxml"), bundle);
            Parent root = fxmlLoader.load();

            // Verificar que el Stage no sea nulo antes de cambiar la raíz de la escena
            if (stage != null) {
                stage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra información sobre la aplicación en una ventana emergente.
     * Este metodo se llama cuando el usuario selecciona la opción "Acerca de" en
     * el menú. Muestra una ventana con detalles acerca de la aplicación, como el
     * nombre del autor, la fecha de inicio del desarrollo y una breve descripción
     * de la funcionalidad.
     *
     * @param event El evento que activa este metodo (generalmente un clic en un botón).
     */
    @FXML
    public void cargarAcercaDe(ActionEvent event) {
        // Crear la ventana de alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Información sobre la aplicación");
        alert.setContentText(
                "Autor: Joel González Salgado\n" +
                        "Fecha de comienzo del desarrollo: 16 de enero del 2025\n" +
                        "Alumno de Desarrollo de Aplicaciones Multiplataforma\n\n" +
                        "Descripción:\n" +
                        "Esta aplicación permite gestionar los datos de una biblioteca,\n" +
                        "incluyendo préstamos, devoluciones, libros y alumnos."
        );

        // Mostrar la ventana
        alert.showAndWait();
    }

}
