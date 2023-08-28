package org.aoclient.connection;


import org.aoclient.engine.gui.forms.Message;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.aoclient.connection.Protocol.*;
import static org.aoclient.engine.Engine.forms;

/**
 * Clase que maneja el socket de conexion.
 */
public class SocketConnection {
    private static SocketConnection instance;

    private final static String IP_SERVER = "127.0.0.1";
    private final static int PORT_SERVER = 7666;

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
    public static SocketConnection getInstance() {
        if(instance == null) {
            instance = new SocketConnection();
        }

        return instance;
    }

    /**
     * Intenta conectarse con el servidor segun la Ip y el puerto asignado.
     */
    public void connect() {
        try {
            if(sock == null || sock.isClosed()) {
                sock = new Socket(IP_SERVER, PORT_SERVER);
                writeData = new DataOutputStream(sock.getOutputStream()); // envio
                handleData = new DataInputStream(sock.getInputStream()); // respuesta..
            }

            incomingData.readASCIIStringFixed(incomingData.length());
            outgoingData.readASCIIStringFixed(outgoingData.length());

        } catch(Exception e) {
            forms.add(new Message(e.getMessage()));
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
                //throw new RuntimeException(e);
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
                //System.out.println("Available Bytes: " + availableBytes);
                final byte[] dataBuffer = new byte[availableBytes];
                final int bytesRead = handleData.read(dataBuffer); // leemos los bytes que devolvio
                //System.out.println("Bytes Read: " + bytesRead);

                if (bytesRead > 0) {
                    //final String RD = new String(dataBuffer, 0 , bytesRead, "Cp1252");
                    final String RD = convertVBUnicodeToUTF8(dataBuffer, bytesRead); // leemos la informacion
                    if (RD.isEmpty()) return;

                    byte[] data = RD.getBytes(); // la convertimos en bytes
                    //System.out.println("Received data: " + RD);

                    // Put data in the buffer
                    incomingData.writeBlock(data, -1);

                    //Send buffer to handle data
                    handleIncomingData();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Covierte una string al formato de "vbUnicode" (Cp1252). Esto es para que pueda leer los caracteres
     *         especiales que devuleve el servidor.
     */
    public String convertVBUnicodeToUTF8(byte[] dataBuffer, int bytesRead) {
        try {
            String rawData = new String(dataBuffer, 0, bytesRead, "Cp1252"); // Assuming Windows-1252 encoding
            byte[] utf8Bytes = rawData.getBytes(StandardCharsets.UTF_8);
            return new String(utf8Bytes, StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
