package joel.dein.proyectobibliotecadein.DAO;

import joel.dein.proyectobibliotecadein.BBDD.ConexionBBDD;
import joel.dein.proyectobibliotecadein.MODEL.AlumnoModel;
import joel.dein.proyectobibliotecadein.MODEL.LibroModel;
import joel.dein.proyectobibliotecadein.MODEL.PrestamoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDao {

    /**
     * Obtiene un préstamo por su ID.
     *
     * @param idPrestamo el ID del préstamo.
     * @return un objeto PrestamoModel si se encuentra, null en caso contrario.
     */
    public static PrestamoModel getPrestamo(int idPrestamo) {
        String sql = "SELECT * FROM Prestamo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPrestamo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PrestamoModel(
                        rs.getInt("id_prestamo"),
                        AlumnosDao.getAlumno(rs.getString("dni_alumno")),
                        LibroDao.getLibro(rs.getInt("codigo_libro")),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los préstamos.
     *
     * @return una lista observable de PrestamoModel.
     */
    public static ObservableList<PrestamoModel> getTodosPrestamo() {
        ObservableList<PrestamoModel> listaPrestamos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Prestamo";

        try (Connection conn = ConexionBBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaPrestamos.add(new PrestamoModel(
                        rs.getInt("id_prestamo"),
                        AlumnosDao.getAlumno(rs.getString("dni_alumno")),
                        LibroDao.getLibro(rs.getInt("codigo_libro")),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPrestamos;
    }

    /**
     * Inserta un nuevo préstamo en la base de datos.
     *
     * @param prestamo el objeto PrestamoModel que contiene los datos del préstamo.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public static boolean insertPrestamo(PrestamoModel prestamo) {
        if (!comprobarSiLibroSePuedePrestar(prestamo.getLibro().getCodigo())) {
            return false; // No se puede prestar el libro porque ya está prestado.
        }

        String sql = "INSERT INTO Prestamo (dni_alumno, codigo_libro, fecha_prestamo) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, prestamo.getAlumno().getDni());
            pstmt.setInt(2, prestamo.getLibro().getCodigo());
            pstmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFecha_prestamo()));
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtiene todos los préstamos de un alumno.
     *
     * @param alumno el objeto AlumnoModel.
     * @return una lista de PrestamoModel correspondientes al alumno.
     */
    public static List<PrestamoModel> getPrestamosDeAlumno(AlumnoModel alumno) {
        List<PrestamoModel> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamo WHERE dni_alumno = ?";

        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, alumno.getDni());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                prestamos.add(new PrestamoModel(
                        rs.getInt("id_prestamo"),
                        alumno,
                        LibroDao.getLibro(rs.getInt("codigo_libro")),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    /**
     * Obtiene todos los préstamos de un libro.
     *
     * @param libro el objeto LibroModel.
     * @return una lista de PrestamoModel correspondientes al libro.
     */
    public static List<PrestamoModel> getPrestamosDeLibro(LibroModel libro) {
        List<PrestamoModel> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamo WHERE codigo_libro = ?";

        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, libro.getCodigo());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                prestamos.add(new PrestamoModel(
                        rs.getInt("id_prestamo"),
                        AlumnosDao.getAlumno(rs.getString("dni_alumno")),
                        libro,
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    /**
     * Comprueba si un libro puede ser prestado (si no está actualmente prestado).
     *
     * @param codigoLibro el código del libro.
     * @return true si el libro puede ser prestado, false en caso contrario.
     */
    public static boolean comprobarSiLibroSePuedePrestar(int codigoLibro) {
        String sql = "SELECT COUNT(*) FROM Prestamo WHERE codigo_libro = ?";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigoLibro);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Hay un préstamo activo para este libro.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Elimina un préstamo de la base de datos.
     *
     * @param idPrestamo el ID del préstamo.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public static boolean deletePrestamo(int idPrestamo) {
        String sql = "DELETE FROM Prestamo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPrestamo);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
