package joel.dein.proyectobibliotecadein.MODEL;

import java.sql.Blob;
import java.util.Objects;

public class LibroModel {
    private int codigo;
    private String titulo;
    private String autor;
    private String editorial;
    private String estado;
    private int baja;
    private Blob imagen;

    public LibroModel(int codigo, String titulo, String autor, String editorial, String estado, int baja, Blob imagen) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado;
        this.baja = baja;
        this.imagen = imagen;
    }

    public LibroModel() {
    }

    @Override
    public String toString() {
        return titulo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getBaja() {
        return baja;
    }

    public void setBaja(int baja) {
        this.baja = baja;
    }

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

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


    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}
