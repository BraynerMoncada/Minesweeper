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
    public void initialize() {
        botones = new Button[8][8];
        minas = new boolean[8][8];
        valores = new int[8][8];
        numMinasRestantes = 10;

        // Agregar botones y manejo de eventos
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Button button = new Button();
                button.setPrefSize(50, 50);
                botones[i][j] = button;
                gridPane.add(button, j, i);

                // Manejo de eventos para revelar la casilla
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            int row = GridPane.getRowIndex(button);
                            int col = GridPane.getColumnIndex(button);
                            if (!minas[row][col]) {
                                revelarCasilla(row, col);
                            } else {
                                // El usuario seleccionó una mina, fin del juego
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
                        // Verificar si el usuario ganó el juego
                        if (numMinasRestantes == 0) {
                            System.out.println("Ganaste");
                        }
                    }
                });
            }
        }

        // Agregar minas aleatorias
        Random rand = new Random();
        int minasColocadas = 0;
        while (minasColocadas < 10) {
            int row = rand.nextInt(8);
            int col = rand.nextInt(8);
            if (!minas[row][col]) {
                minas[row][col] = true;
                minasColocadas++;
            }
        }

        // Calcular los valores de cada casilla
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (minas[i][j]) {
                    valores[i][j] = -1; // Asignar -1 a las casillas con minas
                } else {
                    // Contar las minas adyacentes
                    int count = 0;
                    for (int r = i - 1; r <= i + 1; r++) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (r >= 0 && r < 8 && c >= 0 && c < 8 && minas[r][c]) {
                                count++;
                            }
                        }
                    }
                    valores[i][j] = count; // Asignar el número de minas adyacentes a la casilla
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

