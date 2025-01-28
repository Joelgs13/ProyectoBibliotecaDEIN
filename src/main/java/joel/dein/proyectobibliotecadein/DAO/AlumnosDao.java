package joel.dein.proyectobibliotecadein.DAO;

import joel.dein.proyectobibliotecadein.BBDD.ConexionBBDD;
import joel.dein.proyectobibliotecadein.MODEL.AlumnoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Clase DAO para la gestión de la tabla Alumno.
 */
public class AlumnosDao {

    private static Connection conn;

    static {
        conn = ConexionBBDD.getConnection();
    }

    public AlumnosDao() throws SQLException {
    }

    /**
     * Obtiene un alumno por su DNI.
     *
     * @param dni el DNI del alumno.
     * @return un objeto AlumnoModel si se encuentra, null en caso contrario.
     */
    public static AlumnoModel getAlumno(String dni) {
        String sql = "SELECT * FROM Alumno WHERE dni = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new AlumnoModel(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los alumnos de la base de datos.
     *
     * @return una lista observable de AlumnoModel.
     */
    public static ObservableList<AlumnoModel> getTodosAlumnos() {
        ObservableList<AlumnoModel> listaAlumnos = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Alumno";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                listaAlumnos.add(new AlumnoModel(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAlumnos;
    }

    /**
     * Inserta un nuevo alumno en la base de datos.
     *
     * @param alumno el objeto AlumnoModel que contiene los datos del alumno.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public static boolean insertAlumno(AlumnoModel alumno) {
        String sql = "INSERT INTO Alumno (dni, nombre, apellido1, apellido2) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, alumno.getDni());
            pstmt.setString(2, alumno.getNombre());
            pstmt.setString(3, alumno.getApellido1());
            pstmt.setString(4, alumno.getApellido2());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Actualiza los datos de un alumno existente.
     *
     * @param alumno el objeto AlumnoModel que contiene los nuevos datos del alumno.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public static boolean updateAlumno(AlumnoModel alumno) {
        String sql = "UPDATE Alumno SET nombre = ?, apellido1 = ?, apellido2 = ? WHERE dni = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, alumno.getNombre());
            pstmt.setString(2, alumno.getApellido1());
            pstmt.setString(3, alumno.getApellido2());
            pstmt.setString(4, alumno.getDni());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un alumno de la base de datos por su DNI.
     *
     * @param dni el DNI del alumno a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public static boolean deleteAlumno(String dni) {
        String sql = "DELETE FROM Alumno WHERE dni = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Comprueba si un alumno existe en la base de datos.
     *
     * @param dni el DNI del alumno a comprobar.
     * @return true si el alumno existe, false en caso contrario.
     */
    public static boolean existeAlumno(String dni) {
        String sql = "SELECT dni FROM Alumno WHERE dni = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
