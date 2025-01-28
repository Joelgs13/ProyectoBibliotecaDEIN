package joel.dein.proyectobibliotecadein.DAO;

import joel.dein.proyectobibliotecadein.BBDD.ConexionBBDD;
import joel.dein.proyectobibliotecadein.MODEL.LibroModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

/**
 * Clase DAO para la gestión de la tabla Libro.
 */
public class LibroDao {

    /**
     * Obtiene un libro por su código.
     *
     * @param codigo el código del libro.
     * @return un objeto LibroModel si se encuentra, null en caso contrario.
     */
    public static LibroModel getLibro(int codigo) {
        String sql = "SELECT * FROM Libro WHERE codigo = ?";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new LibroModel(
                        rs.getInt("codigo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getString("estado"),
                        rs.getInt("baja"),
                        rs.getBlob("imagen")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los libros de la base de datos.
     *
     * @return una lista observable de LibroModel.
     */
    public static ObservableList<LibroModel> getTodosLibros() {
        ObservableList<LibroModel> listaLibros = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Libro";
        try (Connection conn = ConexionBBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaLibros.add(new LibroModel(
                        rs.getInt("codigo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getString("estado"),
                        rs.getInt("baja"),
                        rs.getBlob("imagen")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaLibros;
    }

    /**
     * Obtiene todos los libros con el campo 'baja' igual a 0.
     *
     * @return una lista observable de libros activos.
     */
    public static ObservableList<LibroModel> getTodosLibrosConBajaA0() {
        ObservableList<LibroModel> listaLibros = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Libro WHERE baja = 0";
        try (Connection conn = ConexionBBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaLibros.add(new LibroModel(
                        rs.getInt("codigo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getString("estado"),
                        rs.getInt("baja"),
                        rs.getBlob("imagen")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaLibros;
    }

    /**
     * Convierte un array de bytes en un Blob.
     *
     * @param data el array de bytes.
     * @return un objeto Blob que representa la imagen.
     */
    public static Blob convertirABlob(byte[] data) {
        try (Connection conn = ConexionBBDD.getConnection()) {
            Blob blob = conn.createBlob();
            blob.setBytes(1, data);
            return blob;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param libro el objeto LibroModel que contiene los datos del libro.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public static boolean insertLibro(LibroModel libro) {
        String sql = "INSERT INTO Libro (titulo, autor, editorial, estado, baja, imagen) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getEditorial());
            pstmt.setString(4, libro.getEstado());
            pstmt.setInt(5, libro.getBaja());
            pstmt.setBlob(6, libro.getImagen());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Actualiza los datos de un libro existente.
     *
     * @param libro el objeto LibroModel que contiene los nuevos datos del libro.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public static boolean updateLibro(LibroModel libro) {
        String sql = "UPDATE Libro SET titulo = ?, autor = ?, editorial = ?, estado = ?, baja = ?, imagen = ? WHERE codigo = ?";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getEditorial());
            pstmt.setString(4, libro.getEstado());
            pstmt.setInt(5, libro.getBaja());
            pstmt.setBlob(6, libro.getImagen());
            pstmt.setInt(7, libro.getCodigo());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cambia el valor del campo 'baja' a 1 para marcar un libro como dado de baja.
     *
     * @param codigo el código del libro a dar de baja.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public static boolean bajaDelLibro(int codigo) {
        String sql = "UPDATE Libro SET baja = 1 WHERE codigo = ?";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
