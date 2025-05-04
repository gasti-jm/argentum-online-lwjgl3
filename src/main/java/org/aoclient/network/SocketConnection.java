package org.aoclient.network;

import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.network.protocol.Protocol.*;

/**
 * <p>
 * Gestiona la conexion a traves de sockets TCP, permitiendo establecer comunicacion con un servidor. Ofrece metodos para
 * conectar, desconectar, enviar y recibir datos utilizando flujos de entrada y salida.
 * <p>
 * TODO Se podria llamar Connection?
 */

public class SocketConnection {

    /** Instancia de Socket para la conexion TCP. */
    private Socket socket;
    /** Flujo de salida para enviar datos. */
    private DataOutputStream outputStream;
    /** Flujo de entrada para recibir datos. */
    private DataInputStream inputStream;
    /** Bandera que indica si hay un intento de conexion en curso. */
    private boolean tryConnect;

    private SocketConnection() {
    }

    public static SocketConnection getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * <p>
     * Intenta establecer una conexion con el servidor a traves de un socket. Si la conexion ya esta en proceso, muestra un
     * mensaje al usuario y no ejecuta ningun procedimiento adicional. En caso de que no exista un socket activo o este cerrado,
     * intenta abrir uno utilizando las opciones configuradas.
     * <p>
     * Este metodo se llama primero en tres posibles casos: cuando se conecta el usuario por primera vez, cuando se crea un
     * personaje o cuando se lanza los dados. Esto es asi ya que son los tres posibles casos en donde se va a iniciar la conexion
     * con el servidor antes de iniciar el juego. Por lo tanto, se crea un subproceso para manejar la comunicacion
     * cliente-servidor.
     * <p>
     * La primera vez que se necesita la clase {@code Protocol} ocurre cuando:
     * <ol>
     *   <li>El usuario intenta conectarse al servidor desde la interfaz de usuario
     *   <li>Como resultado, se llama al metodo {@code connect()} de {@code SocketConnection}
     *   <li>Al ejecutar este metodo, la JVM necesita resolver las referencias a {@code incomingData} y {@code outgoingData}
     *   <li>La JVM carga la clase {@code Protocol} en memoria para resolver estas referencias
     *   <li>Durante la carga de la clase, se inicializan las variables estaticas:
     *   <pre>{@code
     *      public static ByteQueue outgoingData = new ByteQueue();
     *      public static ByteQueue incomingData = new ByteQueue();
     *   }</pre>
     * </ol>
     * <p>
     * Por lo tanto, la primera utilizacion de la clase {@code Protocol} ocurre en el primer intento de conexion al servidor,
     * cuando el codigo llama a {@code SocketConnection.connect()} y necesita acceder a los buffers de datos para limpiarlos como
     * parte del proceso de inicializacion de la conexion.
     *
     * @return true si la conexion fue exitosa, false en caso de error o si no logra establecerse la conexion con el servidor.
     */
    public boolean connect() {
        // Comprueba si ya esta intentando conectarse
        if (tryConnect) {
            ImGUISystem.get().show(new FMessage("Trying to connect to the server, please wait..."));
            return false;
        }

        // Comprueba si el socket no existe o esta cerrado, importante que sea en ese orden
        if (socket == null || socket.isClosed()) {
            this.tryConnect = true;

            try {
                // Crea un nuevo socket usando la IP y puerto del servidor
                socket = new Socket(options.getIpServer(), Integer.parseInt(options.getPortServer()));
                // Inicializa los flujos de salida y entrada de datos
                outputStream = new DataOutputStream(socket.getOutputStream());
                inputStream = new DataInputStream(socket.getInputStream());

                // Si el socket se conecto, lo marca como proceso de conexion, y limpia los buffers de entrada y salida leyendo todos los bytes existentes
                if (socket.isConnected()) {
                    this.tryConnect = false;
                    /* Antes de establecer la conexion, es importante asegurarse de que los buffers esten vacios. Es decir que se
                     * hace un "reset" completo del estado de comunicacion del cliente, eliminando cualquier dato pendiente tanto
                     * para envio como para recepcion. */
                    incomingData.readBytes(incomingData.length());
                    outgoingData.readBytes(outgoingData.length());
                }

            } catch (Exception e) {
                ImGUISystem.get().show(new FMessage(e.getMessage()));
                this.tryConnect = false;
                return false;
            }
        }

        return true;
    }

    /**
     * Cierra la conexion del socket y los flujos de entrada y salida asociados.
     * <p>
     * Este metodo verifica si el socket esta preparado para la comunicacion mediante {@code isReadyForCommunication()}. Si no
     * esta listo, el metodo se interrumpe sin realizar ninguna accion.
     * <p>
     * En caso de que la conexion este activa, cierra los flujos de entrada y salida, asi como el socket asociado, manejando
     * posibles excepciones {@code IOException} que pudieran ocurrir durante el cierre.
     * <p>
     * Finalmente, resetea el estado del juego.
     */
    public void disconnect() {
        if (!isReadyForCommunication()) return;
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        } finally {
            User.get().resetGameState();
        }
    }

    /**
     * Escribe los datos del buffer interno de salida en el flujo de salida del socket.
     * <p>
     * Este metodo gestiona el proceso completo de envio de datos pendientes al servidor:
     * <ol>
     * <li>Verifica si el socket esta preparado para realizar operaciones de escritura mediante {@code isReadyForWriting()}. Si el
     * estado del socket no permite esta operacion, el metodo finaliza sin realizar ninguna accion.
     * <li>Comprueba si existen datos en el buffer interno de salida ({@code outgoingData}). Si hay datos disponibles, los extrae
     * utilizando {@code outgoingData.readBytes()}
     * <li>Envia los datos extraidos al servidor a traves del flujo de salida
     * <li>Maneja posibles errores durante la transmision, registrando el mensaje de error y desconectando el socket en caso de
     * fallo
     * </ol>
     * <p>
     * Se debe llamar a este metodo cada vez que sea necesario enviar los datos acumulados en el buffer interno de salida hacia el
     * servidor.
     */
    public void write() {
        if (!isReadyForWriting()) return;
        if (outgoingData != null && outgoingData.length() > 0) {
            // Extrae los bytes del buffer interno de salida y los almacena en el array de bytes
            byte[] bytes = outgoingData.readBytes(outgoingData.length());
            try {
                // Envia los bytes al servidor a traves del flujo de salida
                outputStream.write(bytes);
            } catch (IOException e) {
                System.err.println("Error writing bytes: " + e.getMessage());
                disconnect();
            }
        }
    }

    /**
     * Lee y procesa los datos disponibles en el flujo de entrada del socket.
     * <p>
     * Este metodo realiza las siguientes operaciones en secuencia:
     * <ol>
     * <li>Verifica si el socket esta preparado para realizar operaciones de lectura mediante {@code isReadyForReading()}. Si no
     * esta listo, el metodo finaliza sin realizar ninguna accion.
     * <li>Intenta obtener la cantidad de bytes disponibles en el flujo de entrada mediante {@code inputStream.available()}.
     * <li>Si hay bytes disponibles (mayor que cero), crea un buffer temporal con el tamaño adecuado.
     * <li>Lee los bytes disponibles del flujo de entrada y los almacena en el buffer temporal.
     * <li>Si se leyeron bytes correctamente (bytesRead > 0), los escribe en el buffer de datos entrantes mediante
     * {@code incomingData.writeBlock()}.
     * <li>Procesa los datos recibidos llamando al metodo {@code handleIncomingData()}.
     * </ol>
     * <p>
     * En caso de que ocurra una excepcion {@code IOException} durante cualquiera de estas operaciones, el error es capturado y
     * registrado en la salida de error estandar.
     */
    public void read() {
        if (!isReadyForReading()) return;
        try {
            int availableBytes = inputStream.available();
            if (availableBytes > 0) {
                byte[] buffer = new byte[availableBytes];
                int bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    /* Escribe los bytes entrantes del servidor en el buffer temporal de entrada y luego delega el procesamiento
                     * de los datos (bytes) a PacketReceiver. */
                    incomingData.writeBlock(buffer, -1);
                    handleIncomingData();
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Verifica si el socket esta listo para escribir datos.
     *
     * @return true si el socket esta conectado y el flujo de salida esta disponible
     */
    private boolean isReadyForWriting() {
        return isReadyForCommunication() && outputStream != null;
    }

    /**
     * Verifica si el socket esta listo para leer datos.
     *
     * @return true si el socket esta conectado y el flujo de entrada esta disponible
     */
    private boolean isReadyForReading() {
        return isReadyForCommunication() && inputStream != null;
    }

    /**
     * Verifica si el socket esta preparado para comunicacion.
     *
     * @return true si el socket existe y esta conectado
     */
    private boolean isReadyForCommunication() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    private static class SingletonHolder {
        private static final SocketConnection INSTANCE = new SocketConnection();
    }

}
