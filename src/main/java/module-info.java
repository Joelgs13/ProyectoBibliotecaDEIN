module joel.dein.proyectobibliotecadein {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires net.sf.jasperreports.core;


    opens joel.dein.proyectobibliotecadein to javafx.fxml;
    exports joel.dein.proyectobibliotecadein;
}