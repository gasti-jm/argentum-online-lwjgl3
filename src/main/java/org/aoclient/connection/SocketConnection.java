package org.aoclient.connection;

import org.aoclient.connection.packets.E_Modo;

import java.net.*;
import java.io.*;

import static org.aoclient.connection.Protocol.*;

public class SocketConnection {
    private static SocketConnection instance;

    private final static String IP_SERVER = "127.0.0.1";
    private final static int PORT_SERVER = 7666;

    private Socket sock;
    private DataOutputStream writeData;
    private DataInputStream handleData;

    private SocketConnection() {

    }

    public static SocketConnection getInstance() {
        if(instance == null) {
            instance = new SocketConnection();
        }

        return instance;
    }

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
            e.printStackTrace();
        }
    }

    public void flushBuffer() {
        if (writeData == null || !sock.isConnected() || sock.isClosed()) return;

        if (outgoingData.length() != 0){
            sendData(outgoingData.readASCIIStringFixed(outgoingData.length()));
        }
    }

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

    public void readData() {
        if (handleData == null || !sock.isConnected() || sock.isClosed()) return;

        try {
            final int availableBytes = handleData.available();
            if (availableBytes > 0) {
                //System.out.println("Available Bytes: " + availableBytes);
                final byte[] dataBuffer = new byte[availableBytes];
                final int bytesRead = handleData.read(dataBuffer);
                //System.out.println("Bytes Read: " + bytesRead);

                if (bytesRead > 0) {
                    final String RD = new String(dataBuffer, 0 , bytesRead);
                    byte[] data = RD.getBytes();
                    //System.out.println("Received data: " + RD);

                    if (RD.isEmpty()) return;

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
