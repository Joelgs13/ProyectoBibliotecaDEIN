package joel.dein.proyectobibliotecadein.MODEL;

import java.sql.Blob;
import java.util.Objects;

/**
 * Esta clase representa un libro en el sistema.
 */
public class LibroModel {

    /**
     * Código único del libro.
     */
    private int codigo;

    /**
     * Título del libro.
     */
    private String titulo;

    /**
     * Autor del libro.
     */
    private String autor;

    /**
     * Editorial del libro.
     */
    private String editorial;

    /**
     * Estado del libro (ej. disponible, prestado, etc.).
     */
    private String estado;

    /**
     * Indica si el libro está dado de baja (1 para dado de baja, 0 para activo).
     */
    private int baja;

    /**
     * Imagen del libro (probablemente una carátula o portada en formato Blob).
     */
    private Blob imagen;

    /**
     * Constructor de la clase LibroModel.
     *
     * @param codigo    El código único del libro.
     * @param titulo    El título del libro.
     * @param autor     El autor del libro.
     * @param editorial La editorial del libro.
     * @param estado    El estado del libro.
     * @param baja      El estado de baja del libro (1 si está dado de baja, 0 si no lo está).
     * @param imagen    La imagen del libro en formato Blob.
     */
    public LibroModel(int codigo, String titulo, String autor, String editorial, String estado, int baja, Blob imagen) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado;
        this.baja = baja;
        this.imagen = imagen;
    }

    /**
     * Constructor vacío de la clase LibroModel.
     */
    public LibroModel() {
    }

    /**
     * Devuelve el título del libro.
     *
     * @return El título del libro.
     */
    @Override
    public String toString() {
        return titulo;
    }

    /**
     * Obtiene el código único del libro.
     *
     * @return El código único del libro.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el código único del libro.
     *
     * @param codigo El código único del libro.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el título del libro.
     *
     * @return El título del libro.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del libro.
     *
     * @param titulo El título del libro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el autor del libro.
     *
     * @return El autor del libro.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Establece el autor del libro.
     *
     * @param autor El autor del libro.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtiene la editorial del libro.
     *
     * @return La editorial del libro.
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Establece la editorial del libro.
     *
     * @param editorial La editorial del libro.
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * Obtiene el estado del libro.
     *
     * @return El estado del libro.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del libro.
     *
     * @param estado El estado del libro.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el estado de baja del libro.
     *
     * @return El estado de baja del libro (1 para dado de baja, 0 si no está dado de baja).
     */
    public int getBaja() {
        return baja;
    }

    /**
     * Establece el estado de baja del libro.
     *
     * @param baja El estado de baja del libro (1 para dado de baja, 0 si no está dado de baja).
     */
    public void setBaja(int baja) {
        this.baja = baja;
    }

    /**
     * Obtiene la imagen del libro.
     *
     * @return La imagen del libro en formato Blob.
     */
    public Blob getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen del libro.
     *
     * @param imagen La imagen del libro en formato Blob.
     */
    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    /**
     * Compara este objeto con otro objeto.
     *
     * @param o El objeto con el que se va a comparar.
     * @return true si los objetos son iguales (mismo código), false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LibroModel libroModel = (LibroModel) o;

        // Compara todos los campos excepto la imagen
        return codigo == libroModel.codigo &&
                Objects.equals(titulo, libroModel.titulo) &&
                Objects.equals(autor, libroModel.autor) &&
                Objects.equals(editorial, libroModel.editorial) &&
                Objects.equals(estado, libroModel.estado) &&
                baja == libroModel.baja;
    }

    /**
     * Genera un código hash único para este objeto basado en el código.
     *
     * @return El código hash único del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}
