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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    private Image blobToImage(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            // Si no hay imagen, usar una imagen por defecto
            return new Image(getClass().getResource("/IMG/libroIcono.png").toString());
        }

        return new Image(new ByteArrayInputStream(imageBytes));
    }


    /*
    * Metodo que sirve para cargar una nueva pantalla
    * */
    private <T> T cargarPantalla(String rutaFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
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


    @FXML
    void aniadirAlumno(ActionEvent event) {
        AlumnosController alumnosController = cargarPantalla("/JXML/alumnos.fxml", "Añadir Alumno");

        if (alumnosController != null) {
            // Configurar el callback para actualizar la tabla
            alumnosController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }

    public void aniadirLibro(ActionEvent event) {
        LibrosController librosController = cargarPantalla("/JXML/libros.fxml", "Añadir Libro");

        if (librosController != null){
            librosController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }
    @FXML
    void prestarLibro(ActionEvent event) {
        PrestamoController prestamoController = cargarPantalla("/JXML/prestamo.fxml", "Prestar Libro");

        if (prestamoController != null){
            prestamoController.setOnCloseCallback(this::cargarDatosTablas);
        }
    }

    @FXML
    void filtrarAlumno(ActionEvent event) {
        String filtro = tfFiltrarAlumno.getText().trim().toLowerCase();

        List<AlumnoModel> alumnosFiltrados = AlumnosDao.getTodosAlumnos().stream()
                .filter(alumno -> alumno.getNombre().toLowerCase().contains(filtro))
                .collect(Collectors.toList());

        tablaAlumnos.getItems().setAll(alumnosFiltrados);
    }

    @FXML
    void filtrarLibros(ActionEvent event) {
        String filtro = tfFiltrarLibros.getText().trim().toLowerCase();

        List<LibroModel> librosFiltrados = LibroDao.getTodosLibrosConBajaA0().stream()
                .filter(libro -> libro.getTitulo().toLowerCase().contains(filtro))
                .collect(Collectors.toList());

        tablaLibros.getItems().setAll(librosFiltrados);
    }

    @FXML
    void filtrarPrestamo(ActionEvent event) {
        String filtro = tfFiltrarPrestamo.getText().trim();

        List<PrestamoModel> prestamosFiltrados = PrestamoDao.getTodosPrestamo().stream()
                .filter(prestamo -> prestamo.getFecha_prestamo().toString().contains(filtro))
                .collect(Collectors.toList());

        tablaPrestamos.getItems().setAll(prestamosFiltrados);
    }

    private void cargarFiltrosHistorico() {
        ObservableList<String> filtros = FXCollections.observableArrayList("Por DNI Del alumno", "Por Título de libro");
        cbFiltroHistorico.setItems(filtros);
        cbFiltroHistorico.getSelectionModel().selectFirst(); // Seleccionar el primer valor por defecto
    }

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

    public void cargarInforme2(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
        parameters.put("SUBREPORT_PATH", getClass().getResource("/JASPERREPORTS/").toString());
        generarReporte("/JASPERREPORTS/proyectoInforme2.jasper", parameters);
    }

    public void cargarInforme3(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
        parameters.put("SUBREPORT_PATH", getClass().getResource("/JASPERREPORTS/").toString());
        generarReporte("/JASPERREPORTS/proyectoInforme3.jasper", parameters);
    }

    public void cargarInforme4(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("IMAGE_PATH", getClass().getResource("/IMG/").toString());
        generarReporte("/JASPERREPORTS/proyectoInforme4.jasper", parameters);
    }

    public void cargarGuia(ActionEvent event) {
        // Implementa la funcionalidad para cargar una guía
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

    // Metodo para mostrar alertas
    private void mostrarAlerta(String titulo, String cabecera, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(cabecera);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    @FXML
    void devolverLibro(ActionEvent event) {
        // Este mtodo está vacío, indícalo si necesita alguna funcionalidad
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

}
