package org.aoclient.network;


import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.engine.gui.ImGUISystem;

import java.net.*;
import java.io.*;

import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.network.Protocol.*;

/**
 * Clase que maneja el socket de conexion.
 */
public class SocketConnection {
    private static SocketConnection instance;

    private Socket sock;
    private DataOutputStream writeData;
    private DataInputStream handleData;
    private boolean tryConnect;


    /**
     * @desc: Constructor privado por singleton.
     */
    private SocketConnection() {
        this.tryConnect = false;
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
     * Intenta conectarse con el servidor segun la Ip y el puerto asignado y retorna true en caso de completarse la conexion
     */
    public boolean connect() {
        if (tryConnect) {
            ImGUISystem.get().show(new FMessage(
                    "Intentando conectarse con el servidor, porfavor espere..."));

            return false;
        }

        if(sock == null || sock.isClosed()) {
            this.tryConnect = true;

            try {
                sock = new Socket(options.getIpServer(), Integer.parseInt(options.getPortServer()));
                writeData   = new DataOutputStream(sock.getOutputStream());     // envio
                handleData  = new DataInputStream(sock.getInputStream());       // respuesta..

                if (sock.isConnected()) {
                    this.tryConnect = false;
                    incomingData.readBytes(incomingData.length());
                    outgoingData.readBytes(outgoingData.length());
                }

            } catch(Exception e) {
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
        if (writeData == null ||
                !sock.isConnected() ||
                sock.isClosed())
            return;

        if (outgoingData.length() != 0){
            sendData(outgoingData.readBytes(outgoingData.length()));
        }
    }

    /**
     *
     * @param sdData Bytes en cadena para ser enviada
     * @desc Envia los bytes al servidor.
     */
    public void sendData(byte[] sdData) {
        if (sock.isConnected()){
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
        if (handleData == null ||
                !sock.isConnected() ||
                sock.isClosed())
            return;

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
        Rain.get().stopRainingSoundLoop();
    }

}
