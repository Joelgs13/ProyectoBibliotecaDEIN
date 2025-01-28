package joel.dein.proyectobibliotecadein.DAO;

import joel.dein.proyectobibliotecadein.BBDD.ConexionBBDD;
import joel.dein.proyectobibliotecadein.MODEL.AlumnoModel;
import joel.dein.proyectobibliotecadein.MODEL.HistoricoPrestamoModel;
import joel.dein.proyectobibliotecadein.MODEL.LibroModel;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoPrestamoDao {

    /**
     * Obtiene un registro de historial de préstamo por ID.
     *
     * @param idPrestamo el ID del historial de préstamo.
     * @return un objeto HistoricoPrestamoModel si se encuentra, null en caso contrario.
     */
    public static HistoricoPrestamoModel getHistorialPrestamo(int idPrestamo) {
        String sql = "SELECT * FROM Historico_prestamo WHERE id_prestamo = ?";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPrestamo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new HistoricoPrestamoModel(
                        rs.getInt("id_prestamo"),
                        AlumnosDao.getAlumno(rs.getString("dni_alumno")),
                        LibroDao.getLibro(rs.getInt("codigo_libro")),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime(),
                        rs.getTimestamp("fecha_devolucion").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los registros de historial de préstamo.
     *
     * @return una lista de HistoricoPrestamoModel con todos los registros de historial.
     */
    public static List<HistoricoPrestamoModel> getTodosHistorialPrestamo() {
        List<HistoricoPrestamoModel> listaHistorial = new ArrayList<>();
        String sql = "SELECT * FROM Historico_prestamo";

        try (Connection conn = ConexionBBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaHistorial.add(new HistoricoPrestamoModel(
                        rs.getInt("id_prestamo"),
                        AlumnosDao.getAlumno(rs.getString("dni_alumno")),
                        LibroDao.getLibro(rs.getInt("codigo_libro")),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime(),
                        rs.getTimestamp("fecha_devolucion").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaHistorial;
    }

    /**
     * Inserta un nuevo registro en el historial de préstamo.
     *
     * @param prestamo el objeto HistoricoPrestamoModel que contiene los datos del historial.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public static boolean insertHistorialPrestamo(HistoricoPrestamoModel prestamo) {
        String sql = "INSERT INTO Historico_prestamo (dni_alumno, codigo_libro, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, prestamo.getAlumno().getDni());
            pstmt.setInt(2, prestamo.getLibro().getCodigo());
            pstmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFecha_prestamo()));
            pstmt.setTimestamp(4, Timestamp.valueOf(prestamo.getFecha_devolucion()));
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtiene el historial de préstamos por alumno.
     *
     * @param dniAlumno el DNI del alumno.
     * @return una lista de HistoricoPrestamoModel correspondientes al historial del alumno.
     */
    public static List<HistoricoPrestamoModel> getHistorialPrestamoByAlumno(String dniAlumno) {
        List<HistoricoPrestamoModel> listaHistorial = new ArrayList<>();
        String sql = "SELECT * FROM Historico_prestamo WHERE dni_alumno = ?";

        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dniAlumno);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listaHistorial.add(new HistoricoPrestamoModel(
                        rs.getInt("id_prestamo"),
                        AlumnosDao.getAlumno(dniAlumno),
                        LibroDao.getLibro(rs.getInt("codigo_libro")),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime(),
                        rs.getTimestamp("fecha_devolucion").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaHistorial;
    }

    /**
     * Obtiene el historial de préstamos por libro.
     *
     * @param codigoLibro el código del libro.
     * @return una lista de HistoricoPrestamoModel correspondientes al historial del libro.
     */
    public static List<HistoricoPrestamoModel> getHistorialPrestamoByLibro(int codigoLibro) {
        List<HistoricoPrestamoModel> listaHistorial = new ArrayList<>();
        String sql = "SELECT * FROM Historico_prestamo WHERE codigo_libro = ?";

        try (Connection conn = ConexionBBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigoLibro);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listaHistorial.add(new HistoricoPrestamoModel(
                        rs.getInt("id_prestamo"),
                        AlumnosDao.getAlumno(rs.getString("dni_alumno")),
                        LibroDao.getLibro(codigoLibro),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime(),
                        rs.getTimestamp("fecha_devolucion").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaHistorial;
    }
}
