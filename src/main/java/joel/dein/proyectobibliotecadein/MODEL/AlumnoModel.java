package joel.dein.proyectobibliotecadein.MODEL;

import java.util.Objects;

public class AlumnoModel {
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;

    public AlumnoModel(String dni, String nombre, String apellido1, String apellido2) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    public AlumnoModel() {
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    @Override
    public boolean equals(Object o) {
        // Verificamos si el objeto es null o si las clases no son iguales
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        // Hacemos el casting al tipo AlumnoModel
        AlumnoModel alumno = (AlumnoModel) o;

        // Comparamos todos los campos
        return Objects.equals(dni, alumno.dni) &&
                Objects.equals(nombre, alumno.nombre) &&
                Objects.equals(apellido1, alumno.apellido1) &&
                Objects.equals(apellido2, alumno.apellido2);
    }

    @Override
    public int hashCode() {
        // Calculamos el hashCode considerando todos los campos
        return Objects.hash(dni, nombre, apellido1, apellido2);
    }
}