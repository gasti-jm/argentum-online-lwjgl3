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
 * Gestiona la conexion a traves de sockets TCP, permitiendo establecer comunicacion con un servidor. Ofrece metodos para
 * conectar, desconectar, enviar y recibir datos utilizando flujos de entrada y salida.
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
     *
     * @return true si la conexion fue exitosa, false en caso de error o si no logra establecerse la conexion con el servidor.
     */
    public boolean connect() {
        // Comprueba si ya esta intentando conectarse
        if (tryConnect) {
            ImGUISystem.get().show(new FMessage("Trying to connect to the server, please wait..."));
            return false;
        }

        // Comprueba si el socket no existe o esta cerrado
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
     * Envia los datos del buffer de salida a traves del socket.
     * <p>
     * Este metodo verifica si el socket esta preparado para realizar operaciones de escritura mediante
     * {@code isReadyForWriting()}. Si el estado del socket no permite esta operacion, el metodo no realiza ninguna accion.
     * <p>
     * Si existen datos en la cola de salida representada por {@code outgoingData}, se leen dichos datos mediante
     * {@code outgoingData.readBytes()} y se envian al flujo de salida del socket utilizando el metodo {@code write()}.
     */
    public void flushBuffer() {
        if (!isReadyForWriting()) return;
        if (outgoingData != null && outgoingData.length() > 0) {
            byte[] bytes = outgoingData.readBytes(outgoingData.length());
            write(bytes);
        }
    }

    /**
     * Escribe los bytes en el flujo de salida del socket.
     * <p>
     * Si ocurre un error al enviar los bytes, imprime un mensaje de error y desconecta el socket.
     *
     * @param bytes arreglo de bytes que se enviara a traves del flujo de salida.
     */
    private void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            System.err.println("Error writing bytes: " + e.getMessage());
            disconnect();
        }
    }

    /**
     * Lee los bytes en el flujo de entrada del socket.
     * <p>
     * Este metodo verifica si el estado del socket permite la operacion de lectura mediante {@code isReadyForReading()}. Si no es
     * asi, no realiza ninguna accion. En caso de que existan bytes disponibles en el flujo de entrada, se procesan a traves del
     * metodo {@code processAvailableBytes(int availableBytes)}.
     * <p>
     * Si ocurre una excepcion {@code IOException} durante el proceso, el error es registrado en la salida de error del sistema.
     */
    public void read() {
        if (!isReadyForReading()) return;
        try {
            int availableBytes = inputStream.available();
            if (availableBytes > 0) processAvailableBytes(availableBytes);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Procesa los bytes disponibles del flujo de entrada.
     *
     * @param availableBytes cantidad de bytes disponibles para leer
     * @throws IOException si ocurre un error durante la lectura
     */
    private void processAvailableBytes(int availableBytes) throws IOException {
        byte[] buffer = new byte[availableBytes];
        int bytesRead = inputStream.read(buffer); // Lee los bytes disponibles y los guarda en el buffer, en donde el numero de bytes leidos se devuelve como un entero
        if (bytesRead > 0) {
            incomingData.writeBlock(buffer, -1);
            handleIncomingData();
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
