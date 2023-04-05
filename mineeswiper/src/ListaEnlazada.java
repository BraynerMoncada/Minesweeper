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


    public Nodo getNodo(int indice) {
        Nodo actual = this.primero;
        int contador = 0;
        while (actual != null) {
            if (contador == indice) {
                return actual;
            }
            contador++;
            actual = actual.getSiguiente();
        }
        return null;
    }

    public int getTamaño() {
        return this.tamaño;
    }

    /**
     * Metodo que elimina nodos por indice
     * @param nodo
     */
    public void eliminarPorNodo(Nodo nodo) {
        if (nodo == null) {
            return;
        }

        // Si el nodo a eliminar es el primero de la lista
        if (nodo == primero) {
            primero = nodo.getSiguiente();
            tamaño--;
            return;
        }

        // Busca el nodo anterior al que se desea eliminar
        Nodo nodoActual = primero;
        while (nodoActual != null && nodoActual.getSiguiente() != nodo) {
            nodoActual = nodoActual.getSiguiente();
        }

        // Si se encontró el nodo anterior, elimina el nodo deseado
        if (nodoActual != null) {
            nodoActual.setSiguiente(nodo.getSiguiente());
            tamaño--;
        }
    }




}