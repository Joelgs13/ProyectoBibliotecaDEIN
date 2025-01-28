package joel.dein.proyectobibliotecadein;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class pantallaBiblioteca extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(pantallaBiblioteca.class.getResource("/JXML/biblioteca.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mi Biblioteca!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}