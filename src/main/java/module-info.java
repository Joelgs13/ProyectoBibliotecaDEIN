module joel.dein.proyectobibliotecadein {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires org.slf4j;


    opens joel.dein.proyectobibliotecadein to javafx.fxml;
    exports joel.dein.proyectobibliotecadein;
}