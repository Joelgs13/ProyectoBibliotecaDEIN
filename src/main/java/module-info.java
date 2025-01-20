module joel.dein.proyectobibliotecadein {
    requires javafx.controls;
    requires javafx.fxml;


    opens joel.dein.proyectobibliotecadein to javafx.fxml;
    exports joel.dein.proyectobibliotecadein;
}