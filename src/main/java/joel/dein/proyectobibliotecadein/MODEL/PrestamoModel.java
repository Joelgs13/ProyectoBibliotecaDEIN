package joel.dein.proyectobibliotecadein.MODEL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un préstamo de un libro en el sistema.
 */
public class PrestamoModel {

    /**
     * Logger para registrar los eventos.
     */
    private static final Logger logger = LoggerFactory.getLogger(PrestamoModel.class);

    /**
     * Formato para mostrar la fecha del préstamo.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    /**
     * Identificador único del préstamo.
     */
    private int id_prestamo;

    /**
     * Alumno que realiza el préstamo.
     */
    private AlumnoModel alumno;

    /**
     * Libro que se presta.
     */
    private LibroModel libro;

    /**
     * Fecha en la que se realiza el préstamo.
     */
    private LocalDateTime fecha_prestamo;

    /**
     * Constructor de la clase PrestamoModel.
     *
     * @param id_prestamo  El identificador único del préstamo.
     * @param alumno       El alumno que realiza el préstamo.
     * @param libro        El libro que se presta.
     * @param fecha_prestamo La fecha y hora en la que se realiza el préstamo.
     */
    public PrestamoModel(int id_prestamo, AlumnoModel alumno, LibroModel libro, LocalDateTime fecha_prestamo) {
        this.id_prestamo = id_prestamo;
        this.alumno = alumno;
        this.libro = libro;
        this.fecha_prestamo = fecha_prestamo;
    }

    /**
     * Constructor vacío de la clase PrestamoModel.
     */
    public PrestamoModel() {}

    /**
     * Devuelve una representación en formato de texto del préstamo, incluyendo
     * el id del préstamo, el alumno, el libro y la fecha de préstamo formateada.
     *
     * @return Una cadena de texto que representa el préstamo.
     */
    @Override
    public String toString() {
        String formattedFecha = FORMATTER.format(fecha_prestamo);
        logger.info("Fecha formateada a string: {}", formattedFecha);
        return id_prestamo + " - " + alumno + " - " + libro + " - " + formattedFecha;
    }

    /**
     * Obtiene el identificador único del préstamo.
     *
     * @return El identificador único del préstamo.
     */
    public int getId_prestamo() {
        return id_prestamo;
    }

    /**
     * Establece el identificador único del préstamo.
     *
     * @param id_prestamo El identificador único del préstamo.
     */
    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    /**
     * Obtiene el alumno que realiza el préstamo.
     *
     * @return El alumno que realiza el préstamo.
     */
    public AlumnoModel getAlumno() {
        return alumno;
    }

    /**
     * Establece el alumno que realiza el préstamo.
     *
     * @param alumno El alumno que realiza el préstamo.
     */
    public void setAlumno(AlumnoModel alumno) {
        this.alumno = alumno;
    }

    /**
     * Obtiene el libro que se presta.
     *
     * @return El libro que se presta.
     */
    public LibroModel getLibro() {
        return libro;
    }

    /**
     * Establece el libro que se presta.
     *
     * @param libro El libro que se presta.
     */
    public void setLibro(LibroModel libro) {
        this.libro = libro;
    }

    /**
     * Obtiene la fecha en la que se realiza el préstamo.
     *
     * @return La fecha en la que se realiza el préstamo.
     */
    public LocalDateTime getFecha_prestamo() {
        return fecha_prestamo;
    }

    /**
     * Establece la fecha en la que se realiza el préstamo.
     *
     * @param fecha_prestamo La fecha en la que se realiza el préstamo.
     */
    public void setFecha_prestamo(LocalDateTime fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }
}
