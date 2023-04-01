import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador de la ventana principal.
 * Autor: Brayner Moncada
 */
public class MainController {

    private Stage primaryStage;

    /**
     * Establece el Stage de la ventana principal.
     * @param primaryStage el Stage de la ventana principal.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Abre la ventana Dummy.
     */
    @FXML
    private void openDummyWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dummy.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana Advanced.
     */
    @FXML
    private void openAdvancedWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("advanced.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la ventana actual y muestra la ventana principal.
     * @param stage el Stage de la ventana actual.
     */
    public void closeWindow(Stage stage) {
        stage.close();
        primaryStage.show();
    }
}
