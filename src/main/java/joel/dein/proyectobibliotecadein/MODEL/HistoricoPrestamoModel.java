package joel.dein.proyectobibliotecadein.MODEL;

import java.time.LocalDateTime;
import java.util.Objects;

public class HistoricoPrestamoModel {
    private int id_prestamo;
    private AlumnoModel alumno;
    private LibroModel libro;
    private LocalDateTime fecha_prestamo;
    private LocalDateTime fecha_devolucion;

    public HistoricoPrestamoModel(int id_prestamo, AlumnoModel alumno, LibroModel libro, LocalDateTime fecha_prestamo, LocalDateTime fecha_devolucion) {
        this.id_prestamo = id_prestamo;
        this.alumno = alumno;
        this.libro = libro;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
    }

    public HistoricoPrestamoModel() {}

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public AlumnoModel getAlumno() {
        return alumno;
    }

    public void setAlumno(AlumnoModel alumno) {
        this.alumno = alumno;
    }

    public LibroModel getLibro() {
        return libro;
    }

    public void setLibro(LibroModel libro) {
        this.libro = libro;
    }

    public LocalDateTime getFecha_prestamo() {
        return fecha_prestamo;
    }

    public void setFecha_prestamo(LocalDateTime fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    public LocalDateTime getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(LocalDateTime fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoPrestamoModel that = (HistoricoPrestamoModel) o;
        return id_prestamo == that.id_prestamo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_prestamo);
    }
}
