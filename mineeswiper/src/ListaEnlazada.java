/**
 Esta clase representa una lista enlazada que almacena nodos con coordenadas de fila y columna.
 Los nodos se agregan al final de la lista y se pueden quitar de la cabeza de la lista.
 Además, se puede seleccionar un nodo aleatorio de la lista y obtener el primer nodo.
 @author BraynerMoncada
 */
public class ListaEnlazada {
    private Nodo primero;
    private Nodo ultimo;
    private int tamaño;

    /**

     Crea una nueva lista enlazada vacía.
     */
    public ListaEnlazada() {
        this.primero = null;
        this.ultimo = null;
    }
    /**
     Agrega un nuevo nodo con las coordenadas de fila y columna especificadas al final de la lista.
     @param fila la coordenada de fila del nodo.
     @param columna la coordenada de columna del nodo.
     */
    public void agregar(int fila, int columna) {
        Nodo nuevoNodo = new Nodo(fila, columna);
        if (this.primero == null) {
            this.primero = nuevoNodo;
            this.ultimo = nuevoNodo;
        } else {
            this.ultimo.setSiguiente(nuevoNodo);
            this.ultimo = nuevoNodo;
        }
    }
    /**
     Quita el primer nodo de la lista y lo devuelve.
     Si la lista está vacía, devuelve null.
     @return el primer nodo de la lista o null si la lista está vacía.
     */
    public Nodo quitar() {
        if (this.primero == null) {
            return null;
        } else {
            Nodo nodo = this.primero;
            this.primero = this.primero.getSiguiente();
            if (this.primero == null) {
                this.ultimo = null;
            }
            return nodo;
        }
    }
    /**
     Agrega un nodo dado al final de la lista.
     @param nodo el nodo a agregar.
     */
    public void agregarAlFinal(Nodo nodo) {
        if (primero == null) {
            primero = nodo;
            ultimo = nodo;
        } else {
            Nodo actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nodo;
            nodo.anterior = actual;
            ultimo = nodo;
        }
        tamaño++;
    }
    /**
     Selecciona un nodo aleatorio de la lista y lo devuelve.
     Si la lista está vacía, devuelve null.
     @return un nodo aleatorio de la lista o null si la lista está vacía.
     */
    public Nodo seleccionarNodoAleatorio() {
        if (this.tamaño == 0) {
            return null;
        }

        int index = (int) (Math.random() * this.tamaño);
        Nodo current = this.primero;
        while (index > 0) {
            current = current.getSiguiente();
            index--;
        }

        return current;
    }

    /**
     Devuelve el primer nodo de la lista.
     @return el primer nodo de la lista.
     */
    public Nodo getPrimero() {
        return this.primero;
    }
}