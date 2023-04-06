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
public class menuController {

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
        SerialTest tester = new SerialTest();
        try {
            tester.connect("COM7");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dummy.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            tester.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana Advanced.
     */
    @FXML
    private void openAdvancedWindow() {
        SerialTest tester = new SerialTest();
        try {
            tester.connect("COM7");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("advanced.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            tester.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la ventana actual y muestra la ventana principal.
     * @param stage el Stage de la ventana actual.
     */


}
