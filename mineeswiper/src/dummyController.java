/**

 Esta clase es el controlador para el juego dummy de Buscaminas
 @author BraynerMoncada
 */

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.sound.sampled.*;

/**
 * En esta clase se controla todo el nivel dummy
 * @author BraynerMoncada
 */

public class dummyController {
    @FXML
    private GridPane gridPane;

    private Button[][] botones;
    private boolean[][] minas;
    private int[][] valores;
    private int numMinasRestantes;

    public Label tiempoLabel;
    private int tiempo = 0;
    private Timer timer;
    public Label minasEncontradasLabel;
    public  int minasEncontradas = 0;

    public int selectedRow =0;
    public int selectedCol = 0;

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


        SerialTest tester = new SerialTest();
        try {
            tester.connect("COM7");
        } catch (Exception e) {
            e.printStackTrace();
        }

        gridPane.setFocusTraversable(true);
        gridPane.requestFocus();


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    tiempo++;
                    actualizarTiempo();
                });
            }
        }, 0, 1000);
        /**
         * Manejo de eventos para el teclado
         */
        TimerTask tarea = new TimerTask() {
            public void run() {
                String movimiento = tester.movimiento;
                Platform.runLater(() -> {
                    moverCasilla(selectedRow, selectedCol, movimiento);
                    tester.movimiento = ""; // Establecer movimiento en cadena vacía
                });
            }
        };
        timer.schedule(tarea, 0, 500);


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
                                String filePath = "/C:\\Users\\brayn\\OneDrive\\Desktop\\Minesweeper\\mineeswiper\\src\\song/SinMina.wav";
                                //playSound(filePath);
                                tester.reproducirTono1();
                                seleccionarCasillaComputador();

                            } else {
                                /**
                                 * El usuario seleccionó una mina, fin del juego
                                 * @author BraynerMoncada
                                 */
                                // Aquí puede agregar lógica para mostrar todas las minas
                                String filePath = "/C:\\Users\\brayn\\OneDrive\\Desktop\\Minesweeper\\mineeswiper\\src\\song/ConMina.wav";
                                //playSound(filePath);
                                tester.reproducirTono2();
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
                                    tester.disconnect();
                                });
                                alert.showAndWait();




                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            int row = GridPane.getRowIndex(button);
                            int col = GridPane.getColumnIndex(button);
                            if (botones[row][col].getText().equals("")) {
                                botones[row][col].setText("F");
                                tester.encenderLed();
                                descubrirMina();
                                seleccionarCasillaComputador();
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

    private void actualizarTiempo() {
        tiempoLabel.setText(String.format("%02d:%02d", tiempo / 60, tiempo % 60));
    }
    // Método que se llama cuando se descubre una mina
    public void descubrirMina() {
        minasEncontradas++;
        minasEncontradasLabel.setText(Integer.toString(minasEncontradas));
    }

    private void revelarCasilla(int row, int col) {

        if (valores[row][col] == -1) {
            // Mostrar la mina seleccionada y finalizar el juego
            System.out.println("Perdiste");
            return;
        }else {
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
                        botones[i][j].setText("FC");
                        break;
                    } else if (numMinasAdyacentes >= 0 && numMinasAdyacentes == numCasillasAdyacentes) {
                        revelarCasillaComputador(i, j);
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
            revelarCasillaComputador(row, col);
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
        botones[row][col].setText(String.valueOf(valores[row][col]));
        botones[row][col].setDisable(true);
        if (valores[row][col] == -1) {
            botones[row][col].setStyle("-fx-background-color: blue;");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Computador Perdio");
            alert.setOnHidden(e -> {
                Stage stage = (Stage) gridPane.getScene().getWindow(); // Obtiene la ventana actual
                stage.close(); // Cierra la ventana actual

            });
            alert.showAndWait();
        }

    }
    /**
     * Este metodo se encarga de revelar las casillas que el computador selecciona.
     * @param row
     * @param col
     */
    private void moverCasilla(int row, int col, String movimiento) {
        // Desseleccionar la casilla actual
        Button currentButton = botones[selectedRow][selectedCol];
        currentButton.setStyle("");

        // Actualizar las coordenadas de la casilla seleccionada
        switch (movimiento) {
            case "a":
                selectedRow = Math.max(selectedRow - 1, 0);
                break;
            case "b":
                selectedRow = Math.min(selectedRow + 1, botones.length - 1);
                break;
            case "i":
                selectedCol = Math.max(selectedCol - 1, 0);
                break;
            case "d":
                selectedCol = Math.min(selectedCol + 1, botones[0].length - 1);
                break;
            case "c":
                if(!minas[selectedRow][selectedCol]) {
                    revelarCasilla(selectedRow,selectedCol);
                    String filePath = "/C:\\Users\\brayn\\OneDrive\\Desktop\\Minesweeper\\mineeswiper\\src\\song/SinMina.wav";
                    //playSound(filePath);
                    seleccionarCasillaComputador();

                }else {
                    /**
                     * El usuario seleccionó una mina, fin del juego
                     * @author BraynerMoncada
                     */
                    // Aquí puede agregar lógica para mostrar todas las minas
                    String filePath = "/C:\\Users\\brayn\\OneDrive\\Desktop\\Minesweeper\\mineeswiper\\src\\song/ConMina.wav";
                    //playSound(filePath);
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
                    break;
                }
            default:
                // Si movimiento no es "a", "b", "i" o "d", no se hace nada
                break;
        }

        // Seleccionar la nueva casilla
        Button newButton = botones[selectedRow][selectedCol];
        // Establecer el foco en el nuevo botón seleccionado
        //newButton.requestFocus();
        newButton.setStyle("-fx-background-color: yellow");
    }

    /**
     *Metodo para reproducir audio
     * @param filePath
     */
    public static void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println("Error al reproducir el sonido: " + ex.getMessage());


        }
    }
}