package org.aoclient.network;

import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.network.protocol.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.network.protocol.Protocol.*;

/**
 * Clase que maneja la conexion de red entre el cliente y el servidor.
 * <p>
 * {@code SocketConnection} implementa el patron de diseño Singleton para garantizar una unica instancia de conexion con el
 * servidor. Se encarga de establecer, mantener y cerrar la comunicacion via sockets TCP/IP.
 * <p>
 * Esta clase provee metodos para conectar con el servidor, enviar datos, recibir y procesar la informacion entrante, y finalmente
 * desconectar la sesion de manera segura.
 * <p>
 * Trabaja en conjunto con {@link Protocol} para manejar el protocolo de comunicacion del juego, utilizando buffers de entrada y
 * salida para gestionar el flujo de datos.
 * <p>
 * La clase maneja automaticamente situaciones como intentos de reconexion, desconexiones inesperadas, y coordina el estado de la
 * conexion con otros componentes del juego para mantener la coherencia del estado del cliente.
 * <p>
 * Al desconectarse, esta clase se encarga de limpiar el estado del cliente, restableciendo variables relacionadas con el
 * personaje del usuario y deteniendo efectos como la lluvia para garantizar una transicion limpia al estado desconectado.
 */

public class SocketConnection {

    private Socket sock;
    private DataOutputStream writeData;
    private DataInputStream handleData;
    private boolean tryConnect;

    private SocketConnection() {
        this.tryConnect = false;
    }

    public static SocketConnection getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Intenta conectarse con el servidor segun la Ip y el puerto asignado y retorna true en caso de completarse la conexion
     */
    public boolean connect() {
        if (tryConnect) {
            ImGUISystem.get().show(new FMessage("Intentando conectarse con el servidor, porfavor espere..."));
            return false;
        }

        if (sock == null || sock.isClosed()) {
            this.tryConnect = true;

            try {
                sock = new Socket(options.getIpServer(), Integer.parseInt(options.getPortServer()));
                writeData = new DataOutputStream(sock.getOutputStream());     // envio
                handleData = new DataInputStream(sock.getInputStream());       // respuesta..

                if (sock.isConnected()) {
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
     * Prepara el envio de informacion checkeando nuestra cola de bytes y que el socket este conectado
     */
    public void flushBuffer() {
        if (writeData == null || !sock.isConnected() || sock.isClosed()) return;
        if (outgoingData.length() != 0) sendData(outgoingData.readBytes(outgoingData.length()));
    }

    /**
     * @param sdData Bytes en cadena para ser enviada
     * @desc Envia los bytes al servidor.
     */
    public void sendData(byte[] sdData) {
        if (sock.isConnected()) {
            try {
                writeData.write(sdData);
            } catch (IOException e) {
                disconnect();
            }
        }
    }

    /**
     * Lee los datos recibidos del servidor
     */
    public void readData() {
        if (handleData == null || !sock.isConnected() || sock.isClosed()) return;
        try {
            final int availableBytes = handleData.available(); // cantidad de bytes que devolvio el servidor.

            if (availableBytes > 0) {
                final byte[] dataBuffer = new byte[availableBytes];
                final int bytesRead = handleData.read(dataBuffer); // leemos los bytes que devolvio

                if (bytesRead > 0) {
                    // Put data in the buffer
                    incomingData.writeBlock(dataBuffer, -1);

                    //Send buffer to handle data
                    handleIncomingData();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Desconecta el socket.
     */
    public void disconnect() {
        try {
            writeData.close();
            handleData.close();
            sock.close();

            this.setDisconnected();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDisconnected() {
        User.get().resetState();
        Rain.get().setRainValue(false);
        Rain.get().stopRainingSoundLoop();
    }

    private static class SingletonHolder {
        private static final SocketConnection INSTANCE = new SocketConnection();
    }

}
