package joel.dein.proyectobibliotecadein.MODEL;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Esta clase representa un histórico de préstamo de un libro.
 */
public class HistoricoPrestamoModel {

    /**
     * Identificador único del préstamo.
     */
    private int id_prestamo;

    /**
     * Alumno que realiza el préstamo.
     */
    private AlumnoModel alumno;

    /**
     * Libro que se está prestando.
     */
    private LibroModel libro;

    /**
     * Fecha en la que se realiza el préstamo.
     */
    private LocalDateTime fecha_prestamo;

    /**
     * Fecha en la que se devuelve el libro.
     */
    private LocalDateTime fecha_devolucion;

    /**
     * Constructor de la clase HistoricoPrestamoModel.
     *
     * @param id_prestamo    El identificador único del préstamo.
     * @param alumno         El alumno que realiza el préstamo.
     * @param libro          El libro que se está prestando.
     * @param fecha_prestamo La fecha en que se realiza el préstamo.
     * @param fecha_devolucion La fecha de devolución del libro.
     */
    public HistoricoPrestamoModel(int id_prestamo, AlumnoModel alumno, LibroModel libro, LocalDateTime fecha_prestamo, LocalDateTime fecha_devolucion) {
        this.id_prestamo = id_prestamo;
        this.alumno = alumno;
        this.libro = libro;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
    }

    /**
     * Constructor vacío de la clase HistoricoPrestamoModel.
     */
    public HistoricoPrestamoModel() {}

    /**
     * Obtiene el identificador del préstamo.
     *
     * @return El identificador del préstamo.
     */
    public int getId_prestamo() {
        return id_prestamo;
    }

    /**
     * Establece el identificador del préstamo.
     *
     * @param id_prestamo El identificador del préstamo.
     */
    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    /**
     * Obtiene el alumno que realizó el préstamo.
     *
     * @return El alumno que realizó el préstamo.
     */
    public AlumnoModel getAlumno() {
        return alumno;
    }

    /**
     * Establece el alumno que realizó el préstamo.
     *
     * @param alumno El alumno que realizó el préstamo.
     */
    public void setAlumno(AlumnoModel alumno) {
        this.alumno = alumno;
    }

    /**
     * Obtiene el libro que fue prestado.
     *
     * @return El libro que fue prestado.
     */
    public LibroModel getLibro() {
        return libro;
    }

    /**
     * Establece el libro que fue prestado.
     *
     * @param libro El libro que fue prestado.
     */
    public void setLibro(LibroModel libro) {
        this.libro = libro;
    }

    /**
     * Obtiene la fecha del préstamo.
     *
     * @return La fecha del préstamo.
     */
    public LocalDateTime getFecha_prestamo() {
        return fecha_prestamo;
    }

    /**
     * Establece la fecha del préstamo.
     *
     * @param fecha_prestamo La fecha del préstamo.
     */
    public void setFecha_prestamo(LocalDateTime fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    /**
     * Obtiene la fecha de devolución del libro.
     *
     * @return La fecha de devolución del libro.
     */
    public LocalDateTime getFecha_devolucion() {
        return fecha_devolucion;
    }

    /**
     * Establece la fecha de devolución del libro.
     *
     * @param fecha_devolucion La fecha de devolución del libro.
     */
    public void setFecha_devolucion(LocalDateTime fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    /**
     * Compara este objeto con otro objeto.
     *
     * @param o El objeto con el que se va a comparar.
     * @return true si los objetos son iguales (mismo id_prestamo), false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoPrestamoModel that = (HistoricoPrestamoModel) o;
        return id_prestamo == that.id_prestamo;
    }

    /**
     * Genera un código hash único para este objeto basado en el id_prestamo.
     *
     * @return El código hash único del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id_prestamo);
    }
}
