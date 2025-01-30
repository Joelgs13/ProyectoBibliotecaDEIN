package joel.dein.proyectobibliotecadein;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import joel.dein.proyectobibliotecadein.BBDD.ConexionBBDD;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * La clase pantallaBiblioteca gestiona la inicialización y el lanzamiento de la ventana principal
 * de la biblioteca, cargando el archivo FXML correspondiente y mostrando la interfaz.
 */
public class PantallaBiblioteca extends Application {

    static Stage stage;  // La ventana principal de la aplicación

    /**
     * Metodo que se llama al iniciar la aplicación.
     * Carga el archivo FXML para la interfaz de la biblioteca y muestra la ventana principal.
     *
     * @param s El escenario principal que se utiliza para mostrar la interfaz.
     */
    @Override
    public void start(Stage s) {
        try {
            Properties connConfig = ConexionBBDD.loadProperties();  // Carga la configuración de conexión a la base de datos
            stage = s;  // Establece el escenario principal

            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");

            // Cargar el recurso de idioma adecuado utilizando el archivo de propiedades
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("LANGUAGES/lang", locale);

            // Cargar el archivo FXML para la interfaz de la biblioteca
            FXMLLoader fxmlLoader = new FXMLLoader(PantallaBiblioteca.class.getResource("/JXML/biblioteca.fxml"), bundle);

            // Crear la escena con el archivo FXML cargado
            Scene scene = new Scene(fxmlLoader.load());

            // Configurar la ventana
            stage.setTitle("Mi Biblioteca!");  // Establece el título de la ventana
            stage.setResizable(false);
            stage.setScene(scene);  // Establece la escena en el escenario
            stage.show();  // Muestra la ventana

        } catch (IOException e) {
            mostrarErrorYSalir("Error en la base de datos", "No se pudo conectar a la base de datos. La aplicación se cerrará.");
        } catch (Exception e) {
            mostrarErrorYSalir("Error inesperado", "Ocurrió un error inesperado. La aplicación se cerrará.");
        }
    }

    /**
     * Metodo para mostrar una ventana emergente de error y cerrar la aplicación.
     *
     * @param titulo  Título del cuadro de diálogo.
     * @param mensaje Mensaje a mostrar en la alerta.
     */
    private void mostrarErrorYSalir(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Muestra la alerta y espera hasta que el usuario la cierre

        System.exit(1); // Termina la aplicación con código de error
    }

    /**
     * Devuelve el escenario principal de la aplicación.
     *
     * @return El escenario principal de la aplicación.
     */
    public static Stage getStage() {
        return stage;  // Retorna el escenario principal
    }

    /**
     * Metodo principal que se utiliza para iniciar la aplicación.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        launch();  // Lanza la aplicación JavaFX
    }
}
