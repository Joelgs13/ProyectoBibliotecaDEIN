package joel.dein.proyectobibliotecadein.BBDD;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión a la base de datos MariaDB.
 * Proporciona métodos para establecer, obtener y cerrar la conexión,
 * así como cargar configuraciones necesarias desde un archivo de propiedades.
 */
public class ConexionBBDD {

    /** Objeto Connection que representa la conexión activa a la base de datos. */
    private static Connection connection;

    /**
     * Constructor que establece la conexión con la base de datos.
     * Carga las propiedades necesarias desde el archivo de configuración
     * llamado {@code configuration.properties} y se conecta a la base de datos
     * utilizando los parámetros especificados.
     *
     * @throws SQLException Si ocurre un error al establecer la conexión,
     *                      como problemas de acceso, credenciales inválidas o URL incorrecta.
     */
    public ConexionBBDD() throws SQLException {
        Properties connConfig = loadProperties();
        String url = connConfig.getProperty("dburl");
        connection = DriverManager.getConnection(url, connConfig);
        connection.setAutoCommit(true);
    }

    /**
     * Devuelve la conexión activa a la base de datos.
     *
     * @return La conexión activa a la base de datos.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Cierra la conexión activa con la base de datos si está abierta.
     *
     * @return {@code null} después de cerrar la conexión.
     * @throws SQLException Si ocurre un error al cerrar la conexión,
     *                      como intentar cerrar una conexión que ya está cerrada.
     */
    public Connection closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null; // Se asigna null después de cerrar
        }
        return connection;
    }

    /**
     * Carga las propiedades de configuración desde el archivo {@code configuration.properties}.
     * El archivo debe contener claves como {@code dburl} y otros parámetros necesarios
     * para establecer la conexión a la base de datos.
     *
     * @return Un objeto {@link Properties} con las configuraciones cargadas.
     * @throws RuntimeException Si ocurre un error al leer el archivo de propiedades.
     */
    public static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("configuration.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo de propiedades", e);
        }
    }
}
