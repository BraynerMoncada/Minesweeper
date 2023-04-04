/**

 Esta clase es el controlador para el juego dummy de Buscaminas
 @author BraynerMoncada
 */

import java.sql.SQLOutput;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdvancedController {
    @FXML
    private GridPane gridPane;

    private Button[][] botones;
    private boolean[][] minas;
    private int[][] valores;
    private int numMinasRestantes;

    private boolean turnoJugador;
    private boolean juegoTermina;
    public Label tiempoLabel;
    private int tiempo = 0;
    private Timer timer;
    public Label minasEncontradasLabel;
    public  int minasEncontradas = 0;


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
        ListaEnlazada listaSegura = new ListaEnlazada();
        ListaEnlazada listaIncertidumbre = new ListaEnlazada();
        ListaEnlazada listaGeneral = new ListaEnlazada();

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
                                    seleccionarCasillaComputador(listaGeneral,listaSegura,listaIncertidumbre);
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
                                    //stage.close(); // Cierra la ventana actual
                                });
                                alert.showAndWait();




                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            int row = GridPane.getRowIndex(button);
                            int col = GridPane.getColumnIndex(button);
                            if (botones[row][col].getText().equals("")) {
                                botones[row][col].setText("F");
                                descubrirMina();
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

    public void seleccionarCasillaComputador(ListaEnlazada listaGeneral, ListaEnlazada listaSegura, ListaEnlazada listaIncertidumbre) {
        Random rand = new Random();

        int index = rand.nextInt(listaGeneral.getTamaño() + 1);
        Nodo nodoSeleccionado = listaGeneral.getNodo(index);

        if (listaSegura.contiene(nodoSeleccionado) || listaIncertidumbre.contiene(nodoSeleccionado)) {
            return; // seleccionar otro nodo
        }

        if (listaGeneral.contiene(nodoSeleccionado)) {
            listaGeneral.eliminarPorNodo(nodoSeleccionado);

            // Agrega el nodo seleccionado a la lista correspondiente (listaSegura o listaIncertidumbre)
            if (valores[nodoSeleccionado.getFila()][nodoSeleccionado.getColumna()] == 0) {
                listaSegura.agregarAlFinal(nodoSeleccionado);
            } else {
                listaIncertidumbre.agregarAlFinal(nodoSeleccionado);
            }
        }


        // Si hay nodos seguros en la listaSegura, selecciona uno y revela la casilla correspondiente
        if (listaSegura.getTamaño() != 0) {
            Nodo nodoSeguroSeleccionado = listaSegura.seleccionarNodoAleatorio();
            int row2 = nodoSeguroSeleccionado.getFila();
            int col2 = nodoSeguroSeleccionado.getColumna();
            botones[row2][col2].fire();
            revelarCasillaComputador(row2, col2);
        } else {
            // Si no hay nodos seguros, selecciona uno de la listaIncertidumbre y revela la casilla correspondiente
            if (listaIncertidumbre.getTamaño() != 0) {
                Nodo nodoIncertidumbreSeleccionado = listaIncertidumbre.seleccionarNodoAleatorio();
                int row1 = nodoIncertidumbreSeleccionado.getFila();
                int col1 = nodoIncertidumbreSeleccionado.getColumna();
                botones[row1][col1].fire();
                revelarCasillaComputador(row1, col1);
            }
            // Si tanto la listaSegura como la listaIncertidumbre están vacías, no hay nada más que hacer.
            return;
        }

        /**public void seleccionarCasillaComputador() {
         Random rand = new Random();


         // Recorre el tablero y agrega cualquier casilla vacía a la listaGeneral
         for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
         if (botones[i][j].getText().equals("")) {
         listaGeneral.agregarAlFinal(new Nodo(i, j));
         }
         }
         }

         // Crea una copia de la listaGeneral
         ListaEnlazada listaGeneralCopia = new ListaEnlazada();
         Nodo nodoActual = listaGeneral.getPrimero();
         while (nodoActual != null) {
         int x = nodoActual.getFila();
         int y = nodoActual.getColumna();
         listaGeneralCopia.agregarAlFinal(new Nodo(x, y));
         nodoActual = nodoActual.getSiguiente();
         }

         // Selecciona un nodo aleatorio de la listaGeneral y elimínalo si no se encuentra en listaSegura o listaIncertidumbre
         int index = rand.nextInt(listaGeneralCopia.getTamaño());
         Nodo nodoSeleccionado = listaGeneralCopia.getNodo(index);

         if(listaGeneral.contiene(nodoSeleccionado)){
         listaGeneral.eliminarPorNodo(nodoSeleccionado);
         }

         if (listaSegura.contiene(nodoSeleccionado) || listaIncertidumbre.contiene(nodoSeleccionado)) {
         return; // seleccionar otro nodo
         }


         // Agrega el nodo seleccionado a la lista correspondiente (listaSegura o listaIncertidumbre)
         if (valores[nodoSeleccionado.getFila()][nodoSeleccionado.getColumna()] == 0) {
         listaSegura.agregarAlFinal(nodoSeleccionado);
         } else {
         listaIncertidumbre.agregarAlFinal(nodoSeleccionado);
         }

         // Imprime las listas para verificar los nodos seleccionados y eliminados
         System.out.println("Lista General:\n");
         imprimirListaEnlazada(listaGeneral);
         System.out.println("Lista Segura:\n");
         imprimirListaEnlazada(listaSegura);
         System.out.println("Lista Incertidumbre:\n");
         imprimirListaEnlazada(listaIncertidumbre);

         // Si hay nodos seguros en la listaSegura, selecciona uno y revela la casilla correspondiente
         if (listaSegura.getTamaño() != 0) {
         Nodo nodoSeguroSeleccionado = listaSegura.seleccionarNodoAleatorio();
         int row2 = nodoSeguroSeleccionado.getFila();
         int col2 = nodoSeguroSeleccionado.getColumna();
         botones[row2][col2].fire();
         revelarCasillaComputador(row2, col2);
         } else {
         // Si no hay nodos seguros, selecciona uno de la listaIncertidumbre y revela la casilla correspondiente
         Nodo nodoIncertidumbreSeleccionado = listaIncertidumbre.seleccionarNodoAleatorio();
         int row1 = nodoIncertidumbreSeleccionado.getFila();
         int col1 = nodoIncertidumbreSeleccionado.getColumna();
         botones[row1][col1].fire();
         revelarCasillaComputador(row1, col1);
         return;
         }
         }*/
        // Imprime las listas para verificar los nodos seleccionados y eliminados
        System.out.println("Lista General:\n");
        imprimirListaEnlazada(listaGeneral);
        System.out.println("Lista Segura:\n");
        imprimirListaEnlazada(listaSegura);
        System.out.println("Lista Incertidumbre:\n");
        imprimirListaEnlazada(listaIncertidumbre);

    }

    private void revelarCasillaComputador(int row, int col) {
        botones[row][col].setText(String.valueOf(valores[row][col]));
        botones[row][col].setDisable(true);
        if (valores[row][col] == -1) {
            botones[row][col].setStyle("-fx-background-color: blue;");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Computador Perdio");
            alert.setOnHidden(e -> {
                Stage stage = (Stage) gridPane.getScene().getWindow(); // Obtiene la ventana actual
                //stage.close(); // Cierra la ventana actual
            });
            alert.showAndWait();
        }

    }
    public void imprimirListaEnlazada(ListaEnlazada lista) {
        Nodo nodoActual = lista.getPrimero();
        while (nodoActual != null) {
            System.out.print("(" + nodoActual.getFila() +"," + nodoActual.getColumna() + ")" + " ");
            nodoActual = nodoActual.getSiguiente();
        }
        System.out.println("\n");
    }}

