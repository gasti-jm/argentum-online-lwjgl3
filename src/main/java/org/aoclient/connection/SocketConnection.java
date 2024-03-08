package org.aoclient.connection;


import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.engine.gui.ImGUISystem;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.aoclient.connection.Protocol.*;
import static org.aoclient.engine.game.models.Character.eraseAllChars;

/**
 * Clase que maneja el socket de conexion.
 */
public class SocketConnection {
    private static SocketConnection instance;

    private Socket sock;
    private DataOutputStream writeData;
    private DataInputStream handleData;

    /**
     * @desc: Constructor privado por singleton.
     */
    private SocketConnection() {

    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton)
     */
    public static SocketConnection get() {
        if(instance == null) {
            instance = new SocketConnection();
        }

        return instance;
    }

    /**
     * Intenta conectarse con el servidor segun la Ip y el puerto asignado.
     */
    public void connect(final String ip, final String port) {
        try {
            if(sock == null || sock.isClosed()) {
                sock = new Socket(ip, Integer.parseInt(port));
                writeData = new DataOutputStream(sock.getOutputStream()); // envio
                handleData = new DataInputStream(sock.getInputStream()); // respuesta..
            }

            incomingData.readASCIIStringFixed(incomingData.length());
            outgoingData.readASCIIStringFixed(outgoingData.length());

        } catch(Exception e) {
            ImGUISystem.get().checkAddOrChange("frmMessage",
                    new FMessage(e.getMessage()));
        }
    }

    /**
     * Prepara el envio de informacion checkeando nuestra cola de bytes y que el socket este conectado
     */
    public void flushBuffer() {
        if (writeData == null || !sock.isConnected() || sock.isClosed()) return;

        if (outgoingData.length() != 0){
            sendData(outgoingData.readASCIIStringFixed(outgoingData.length()));
        }
    }

    /**
     *
     * @param sdData Bytes en cadena para ser enviada
     * @desc Envia los bytes al servidor.
     */
    public void sendData(String sdData) {
        if (sock.isConnected()){
            try {
                writeData.write(sdData.getBytes());
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
        User.get().setUserConected(false);
        User.get().setUserNavegando(false);
        Rain.get().setRainValue(false);
    }
}
