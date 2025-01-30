package joel.dein.proyectobibliotecadein.MODEL;

import java.util.Objects;

/**
 * Esta clase representa a un alumno en el sistema de biblioteca.
 */
public class AlumnoModel {
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;

    /**
     * Constructor de la clase AlumnoModel.
     *
     * @param dni       El DNI del alumno.
     * @param nombre    El nombre del alumno.
     * @param apellido1 El primer apellido del alumno.
     * @param apellido2 El segundo apellido del alumno.
     */
    public AlumnoModel(String dni, String nombre, String apellido1, String apellido2) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    /**
     * Constructor vacío de la clase AlumnoModel.
     */
    public AlumnoModel() {
    }

    /**
     * Obtiene el DNI del alumno.
     *
     * @return El DNI del alumno.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del alumno.
     *
     * @param dni El DNI del alumno.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del alumno.
     *
     * @return El nombre del alumno.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del alumno.
     *
     * @param nombre El nombre del alumno.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el primer apellido del alumno.
     *
     * @return El primer apellido del alumno.
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Establece el primer apellido del alumno.
     *
     * @param apellido1 El primer apellido del alumno.
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * Obtiene el segundo apellido del alumno.
     *
     * @return El segundo apellido del alumno.
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Establece el segundo apellido del alumno.
     *
     * @param apellido2 El segundo apellido del alumno.
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    /**
     * Metodo para representar el alumno como una cadena (solo su nombre).
     *
     * @return El nombre del alumno.
     */
    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Compara este objeto con otro.
     *
     * @param o El objeto con el que se comparará.
     * @return True si los objetos son iguales, false de lo contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlumnoModel alumno = (AlumnoModel) o;

        return Objects.equals(dni, alumno.dni) &&
                Objects.equals(nombre, alumno.nombre) &&
                Objects.equals(apellido1, alumno.apellido1) &&
                Objects.equals(apellido2, alumno.apellido2);
    }

    /**
     * Genera un código hash único para este objeto.
     *
     * @return El código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(dni, nombre, apellido1, apellido2);
    }
}