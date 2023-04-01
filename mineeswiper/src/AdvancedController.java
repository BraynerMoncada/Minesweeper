/**

 Esta clase es el controlador para el juego advanced de Buscaminas
 @author BraynerMoncada
 */

import java.util.Random;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseButton;

public class AdvancedController {
    @FXML
    private GridPane gridPane;

    private Button[][] botones;
    private boolean[][] minas;
    private int[][] valores;
    private int numMinasRestantes;

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
                            } else {
                                /**
                                 * El usuario seleccionó una mina, fin del juego
                                 * @author BraynerMoncada
                                 */
                                // Aquí puede agregar lógica para mostrar todas las minas
                                System.out.println("Hay una bomba, perdiste");
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
                            System.out.println("Ganaste");
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
}

