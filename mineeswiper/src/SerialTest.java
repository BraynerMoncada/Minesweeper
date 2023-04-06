import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import java.io.IOException;
import java.io.InputStream;

public class SerialTest implements SerialPortMessageListener {
    private SerialPort serialPort;

    /**
     * Establece la conexión con el puerto serial especificado y lo configura con los valores predeterminados.
     * Agrega un DataListener a la conexión para poder escuchar eventos del puerto serial.
     *
     * @param portName El nombre del puerto serial al que se va a conectar.
     */
    public void connect(String portName) {
        try {
            serialPort = SerialPort.getCommPort(portName);
            serialPort.setBaudRate(9600);
            serialPort.setNumDataBits(8);
            serialPort.setNumStopBits(1);
            serialPort.setParity(SerialPort.NO_PARITY);
            if (!serialPort.openPort()) {
                throw new IOException("Error al abrir el puerto serial.");
            }
            serialPort.addDataListener(this);
            System.out.println("¡Conexión establecida!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la conexión serial actual.
     */
    public void disconnect() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.removeDataListener();
            serialPort.closePort();
            System.out.println("¡Conexión cerrada!");
        }
    }

    /**
     * Indica que se desea escuchar eventos de datos disponibles en el puerto serial.
     *
     * @return El valor SerialPort.LISTENING_EVENT_DATA_AVAILABLE.
     */
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    /**
     * Maneja los eventos de recepción de datos disponibles en el puerto serial.
     * Lee los datos del InputStream y los muestra por pantalla.
     *
     * @param event El evento de recepción de datos disponibles en el puerto serial.
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return;
        }
        InputStream inputStream = serialPort.getInputStream();
        byte[] buffer = new byte[1024];
        try {
            while (inputStream.available() > 0) {
                int numBytes = inputStream.read(buffer);
                System.out.print(new String(buffer, 0, numBytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve un array de bytes vacío, lo que indica que no se espera un delimitador de mensajes.
     *
     * @return Un array de bytes vacío.
     */
    @Override
    public byte[] getMessageDelimiter() {
        return new byte[0];
    }

    /**
     * Indica que no se espera un delimitador de mensajes.
     *
     * @return El valor false.
     */
    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return false;
    }
}
