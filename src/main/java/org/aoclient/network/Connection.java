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
 * Gestiona la conexion cliente-servidor a traves de sockets TCP. Ofrece metodos para conectar y desconectar a un servidor, y
 * metodos para enviar y recibir bytes dentro de un buffer por medio de los flujos de entrada y salida del socket.
 * <p>
 * TODO Se podria usar NIO?
 */

public enum Connection {

    INSTANCE;

    /** Socket para la conexion TCP. */
    private Socket socket;
    /** Flujo de salida de bytes. */
    private DataOutputStream outputStream;
    /** Flujo de entrada de bytes. */
    private DataInputStream inputStream;
    /** Bandera que indica si hay un intento de conexion en curso. */
    private boolean tryConnect;

    /**
     * <p>
     * Se conecta a una conexion con el servidor a traves de un socket.
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
     *   <li>Al ejecutar este metodo, la JVM necesita resolver las referencias a {@code inputBuffer} y {@code outputBuffer}
     *   <li>La JVM carga la clase {@code Protocol} en memoria para resolver estas referencias
     *   <li>Durante la carga de la clase, se inicializan los objetos estaticos:
     *   <pre>{@code
     *      public static NetworkBuffer outputBuffer = new NetworkBuffer();
     *      public static NetworkBuffer inputBuffer = new NetworkBuffer();
     *   }</pre>
     * </ol>
     *
     * @return true si la conexion fue exitosa, false en caso de error o si no logra establecerse la conexion con el servidor.
     */
    public boolean connect() {
        // Comprueba si ya esta intentando conectarse
        if (tryConnect) {
            ImGUISystem.INSTANCE.show(new FMessage("Trying to connect to the server, please wait..."));
            return false;
        }

        // Comprueba si el socket no existe o esta cerrado, importante que sea en ese orden
        if (socket == null || socket.isClosed()) {
            this.tryConnect = true;

            try {
                // Crea un socket a la IP y puerto del servidor especificado
                socket = new Socket(options.getIpServer(), Integer.parseInt(options.getPortServer()));
                // Inicializa los flujos de salida y entrada de bytes
                outputStream = new DataOutputStream(socket.getOutputStream());
                inputStream = new DataInputStream(socket.getInputStream());

                // Si el socket se conecto, resuelve las referencias de los buffers de entrada y salida
                if (socket.isConnected()) {
                    this.tryConnect = false;
                    // TODO No hay otra forma de crear estas dos instancias? Me parece raro llamar al metodo readBytes() para crear estas intancias
                    inputBuffer.readBytes();
                    outputBuffer.readBytes();
                }

            } catch (Exception e) {
                ImGUISystem.INSTANCE.show(new FMessage(e.getMessage()));
                this.tryConnect = false;
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * Cierra la conexion del socket y los flujos de entrada y salida asociados. En caso de que la conexion este activa, cierra
     * los flujos de entrada y salida, asi como el socket asociado, manejando posibles excepciones {@code IOException} que
     * pudieran ocurrir durante el cierre. Finalmente, resetea el estado del juego.
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
            User.INSTANCE.resetGameState();
        }
    }

    /**
     * Escribe los bytes del buffer de salida en el flujo de salida del socket.
     */
    public void write() {
        if (!isReadyForWriting()) return;
        // Si hay bytes en el buffer de salida
        if (outputBuffer != null && outputBuffer.getLength() > 0) {
            // Extrae los bytes del buffer de salida (que ya fue llenado con los metodos *write antes de enviarlos a travez del flujo de salida) y los almacena en el array de bytes
            byte[] bytes = outputBuffer.readBytes();
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
     * Lee los bytes del flujo de entrada del socket y los almacena en el buffer de entrada.
     * <p>
     * La estructura actual del codigo evita intencionalmente que el metodo {@code read()} bloquee el hilo de ejecucion, lo que es
     * importante para mantener la capacidad de respuesta de la aplicacion, especialmente si este codigo se ejecuta en el hilo
     * principal de la interfaz de usuario.
     * <p>
     * Solo si {@code available()} indica que hay bytes disponibles, entonces se procede a llamar a {@code read()}, lo que
     * garantiza que la llamada no bloqueara el hilo porque ya se sabe que hay bytes disponibles.
     * <p>
     * Ademas, esta doble verificacion es una tecnica de programacion defensiva en entornos de red, donde las condiciones pueden
     * cambiar rapidamente. Aunque en la mayoria de los casos ambas verificaciones daran el mismo resultado, mantener ambas
     * protege contra casos extremos y condiciones de carrera, haciendo el codigo mas robusto.
     */
    public void read() {
        if (!isReadyForReading()) return;
        try {
            // Si hay bytes disponibles para leer (sin bloquear el hilo) en el flujo de entrada
            int availableBytes = inputStream.available();
            if (availableBytes > 0) {
                byte[] buffer = new byte[availableBytes];
                // Si efectivamente se leyeron bytes
                int bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    // Escribe los bytes del servidor en el buffer de entrada
                    inputBuffer.writeBlock(buffer);
                    // Delega el manejo de los bytes a PacketReceiver
                    handleIncomingBytes();
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
     * Verifica si el socket esta preparado para comunicarse.
     *
     * @return true si el socket existe, esta conectado y no esta cerrado
     */
    private boolean isReadyForCommunication() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

}
