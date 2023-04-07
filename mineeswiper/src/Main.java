/**

 La clase Main es la clase principal que inicia la aplicación y carga la ventana principal (menu.fxml).
 Extiende de la clase Application de JavaFX y sobrescribe el método start para definir la escena principal.
 @author BraynerMoncada
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * En esta clase se ejecuta la aplicacion
 * @author BraynerMoncada
 */
public class Main extends Application {
    /**
     }
 * Sobrescribe el método start para definir la escena principal de la aplicación.
 *
 * @param primaryStage El Stage principal de la aplicación.
 * @throws Exception Si hay un error al cargar el archivo FXML.
 */
@Override
public void start(Stage primaryStage) throws Exception{
    Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
    primaryStage.setTitle("Mineeswiper");
    primaryStage.setScene(new Scene(root, 420, 340));
    primaryStage.show();





}

    /**
     * El método main es el punto de entrada de la aplicación.
     *
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}