public class Nodo {
    public int fila;
    public int columna;
    public Nodo siguiente;
    public Nodo anterior;

    public int valor;

    public Nodo(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.siguiente = null;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getValor() {
        return valor;
    }

    public Nodo getSiguiente() {

        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {

        this.siguiente = siguiente;
    }
}
