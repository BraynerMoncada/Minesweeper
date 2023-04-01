/**

 Esta clase es el controlador para el juego dummy de Buscaminas
 @author BraynerMoncada
 */

import java.util.Random;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class dummyController {
    @FXML
    private GridPane gridPane;

    private Button[][] botones;
    private boolean[][] minas;
    private int[][] valores;
    private int numMinasRestantes;

    private boolean turnoJugador;
    private boolean juegoTermina;

    @FXML
    /**
     * Este método se ejecuta al cargar la interfaz de usuario.
     * Inicializa los botones, los eventos, las minas y los valores de las casillas.
     */
    public void initialize() {
        botones = new Button[8][8];
        minas = new boolean[8][8];
        valores = new int[8][8];
        numMinasRestantes = 10;
        turnoJugador = true;
        juegoTermina = false;


        /**
         * Agregar botones y manejo de eventos
         * @author BraynerMoncada
         */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Button button = new Button();
                button.setPrefSize(50, 50);
                botones[i][j] = button;
                gridPane.add(button, j, i);
                // Cambiar el tipo de letra de los números en los botones
                Font font = Font.font("Arial", FontWeight.BOLD,20 ); // Cambiar el tipo de letra a Arial, negrita y tamaño 16
                button.setFont(font);

                /**
                 * Manejo de eventos para revelar la casilla
                 * @author BraynerMoncada
                 */
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            int row = GridPane.getRowIndex(button);
                            int col = GridPane.getColumnIndex(button);
                            if (!minas[row][col]) {
                                revelarCasilla(row, col);
                                if (turnoJugador) {
                                    turnoJugador = false;
                                    seleccionarCasillaComputador();
                                } else {
                                    turnoJugador = true;
                                }
                            } else {
                                /**
                                 * El usuario seleccionó una mina, fin del juego
                                 * @author BraynerMoncada
                                 */
                                // Aquí puede agregar lógica para mostrar todas las minas
                                button.setStyle("-fx-background-color: red");
                                System.out.println("Hay una bomba, perdiste");

                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(null);
                                alert.setContentText("Perdiste!");
                                alert.getDialogPane().setPrefSize(400, 200); // Establecer el tamaño de la ventana en píxeles
                                alert.getDialogPane().setStyle("-fx-font-size: 20; -fx-font-family: 'Arial';"); // Cambiar el tamaño y la fuente de la ventana
                                alert.setOnHidden(e -> {
                                    Stage stage = (Stage) gridPane.getScene().getWindow(); // Obtiene la ventana actual
                                    stage.close(); // Cierra la ventana actual
                                });

                                alert.showAndWait();




                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            int row = GridPane.getRowIndex(button);
                            int col = GridPane.getColumnIndex(button);
                            if (botones[row][col].getText().equals("")) {
                                botones[row][col].setText("F");
                                numMinasRestantes--;
                            } else {
                                botones[row][col].setText("");
                                numMinasRestantes++;
                            }
                        }
                        /**
                         * Verificar si el usuario ganó el juego
                         */
                        if (numMinasRestantes == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("Ganaste!");
                            alert.getDialogPane().setPrefSize(400, 200); // Establecer el tamaño de la ventana en píxeles
                            alert.getDialogPane().setStyle("-fx-font-size: 20; -fx-font-family: 'Arial';"); // Cambiar el tamaño y la fuente de la ventana
                            alert.setOnHidden(e -> {
                                Stage stage = (Stage) gridPane.getScene().getWindow(); // Obtiene la ventana actual
                                stage.close(); // Cierra la ventana actual
                            });

                            alert.showAndWait();
                        }
                    }
                });
            }
        }

        /**
         * Agregar minas aleatorias
         */

        Random rand = new Random(); // Crea un objeto de tipo Random para generar números aleatorios
        int minasColocadas = 0; // Inicializa el contador de minas colocadas en cero

        // Inicia un ciclo mientras no se hayan colocado las 10 minas requeridas
        while (minasColocadas < 10) {
            // Genera aleatoriamente un índice de fila y un índice de columna en la matriz
            int row = rand.nextInt(8);
            int col = rand.nextInt(8);

            // Verifica si la posición generada ya contiene una mina, si no es así, se coloca una mina en esa posición
            if (!minas[row][col]) {
                minas[row][col] = true; // Marca la posición como una mina
                minasColocadas++; // Incrementa el contador de minas colocadas
            }
        }


        /**
         * Calcular los valores de cada casilla
         */
        // Recorrer todas las celdas en la matriz valores
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                // Si hay una mina en la celda actual de la matriz minas, asignar -1 a la celda correspondiente en valores
                if (minas[i][j]) {
                    valores[i][j] = -1; // Asignar -1 a las casillas con minas
                } else {

                    // Contar las minas adyacentes a la celda actual
                    int count = 0;
                    for (int r = i - 1; r <= i + 1; r++) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (r >= 0 && r < 8 && c >= 0 && c < 8 && minas[r][c]) {
                                count++;
                            }
                        }
                    }

                    // Asignar el número de minas adyacentes a la celda correspondiente en valores
                    valores[i][j] = count;
                }
            }
        }
    }

    private void revelarCasilla(int row, int col) {

        if (valores[row][col] == -1) {
            // Mostrar la mina seleccionada y finalizar el juego
            System.out.println("Perdiste");
            return;
        } else if (valores[row][col] == 0) {
            // Mostrar la casilla vacía y revelar las casillas adyacentes que también son vacías
            botones[row][col].setText("0");
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = col - 1; c <= col + 1; c++) {
                    if (r >= 0 && r < 8 && c >= 0 && c < 8 && botones[r][c].getText().equals("")) {
                        revelarCasilla(r, c);
                    }
                }
            }
        } else {
            // Mostrar el número de minas adyacentes en la casilla seleccionada
            botones[row][col].setText(String.valueOf(valores[row][col]));
        }
    }

    /**
     *
     * @author Brayner Moncada
     * En este metodo es el turno del computador,selecciona una casilla aleatoria.
     */
    private void seleccionarCasillaComputador() {
        Random rand = new Random();
        boolean casillaRevelada = false;
        boolean casillaConBandera = false;

        // Buscar casillas adyacentes que no han sido reveladas y que tengan un valor igual al número de minas adyacentes
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (botones[i][j].getText().equals("")) {
                    int numMinasAdyacentes = getNumMinasAdyacentes(i, j);
                    int numCasillasAdyacentes = getNumCasillasAdyacentes(i, j);
                    int numBanderasAdyacentes = getNumBanderasAdyacentes(i, j);
                    if (numMinasAdyacentes >= 0 && numCasillasAdyacentes - numBanderasAdyacentes == numMinasAdyacentes) {
                        casillaConBandera = true;
                        botones[i][j].setText("F");
                        break;
                    } else if (numMinasAdyacentes >= 0 && numMinasAdyacentes == numCasillasAdyacentes) {
                        revelarCasilla(i, j);
                        casillaRevelada = true;
                        break;
                    }
                }
            }
            if (casillaRevelada || casillaConBandera) {
                break;
            }
        }

        // Si no se encontraron casillas con la pista, seleccionar una casilla al azar y revélarla
        if (!casillaRevelada && !casillaConBandera) {
            int row = rand.nextInt(8);
            int col = rand.nextInt(8);
            while (!botones[row][col].getText().equals("")) {
                row = rand.nextInt(8);
                col = rand.nextInt(8);
            }
            revelarCasilla(row, col);
        }
    }

    private int getNumBanderasAdyacentes(int row, int col) {
        int numBanderas = 0;
        for (int i = Math.max(row - 1, 0); i <= Math.min(row + 1, 7); i++) {
            for (int j = Math.max(col - 1, 0); j <= Math.min(col + 1, 7); j++) {
                if (botones[i][j].getText().equals("F")) {
                    numBanderas++;
                }
            }
        }
        return numBanderas;
    }


    private int getNumMinasAdyacentes(int row, int col) {
        int numMinas = 0;
        for (int i = Math.max(row - 1, 0); i <= Math.min(row + 1, 7); i++) {
            for (int j = Math.max(col - 1, 0); j <= Math.min(col + 1, 7); j++) {
                if (minas[i][j] && (i != row || j != col)) {
                    numMinas++;
                }
            }
        }
        return numMinas;
    }

    private int getNumCasillasAdyacentes(int row, int col) {
        int numCasillas = 0;
        for (int i = Math.max(row - 1, 0); i <= Math.min(row + 1, 7); i++) {
            for (int j = Math.max(col - 1, 0); j <= Math.min(col + 1, 7); j++) {
                if (i != row || j != col) {
                    numCasillas++;
                }
            }
        }
        return numCasillas;
    }


    /**
     * Método para revelar una casilla para el computador
     */
    private void revelarCasillaComputador(int row, int col) {
        botones[row][col].setDisable(true);
        if (valores[row][col] == -1) {
            botones[row][col].setText("X");
            botones[row][col].setStyle("-fx-background-color: blue;");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Computador Ganó");
            alert.showAndWait();
            System.exit(0);
        } else if (valores[row][col] == 0) {
            botones[row][col].setText("");
            if (row > 0 && col > 0 && !botones[row - 1][col - 1].isDisabled()) {
                revelarCasillaComputador(row - 1, col - 1);
            }
            if (row > 0 && !botones[row - 1][col].isDisabled()) {
                revelarCasillaComputador(row - 1, col);
            }
            if (row > 0 && col < 7 && !botones[row - 1][col + 1].isDisabled()) {
                revelarCasillaComputador(row - 1, col + 1);
            }
            if (col > 0 && !botones[row][col - 1].isDisabled()) {
                revelarCasillaComputador(row, col - 1);
            }
            if (col < 7 && !botones[row][col + 1].isDisabled()) {
                revelarCasillaComputador(row, col + 1);
            }
            if (row < 7 && col > 0 && !botones[row + 1][col - 1].isDisabled()) {
                revelarCasillaComputador(row + 1, col - 1);
            }
            if (row < 7 && !botones[row + 1][col].isDisabled()) {
                revelarCasillaComputador(row + 1, col);
            }
            if (row < 7 && col < 7 && !botones[row + 1][col + 1].isDisabled()) {
                revelarCasillaComputador(row + 1, col + 1);
            }
        } else {
            botones[row][col].setText(Integer.toString(valores[row][col]));
        }
    }



}

