package org.aoclient.connection;

import org.aoclient.connection.packets.E_Modo;

import java.net.*;
import java.io.*;

import static org.aoclient.connection.Protocol.*;
import static org.aoclient.connection.packets.E_Modo.NORMAL;

public class SocketConnection {
    private static SocketConnection instance;


    final static String IP_SERVER = "127.0.0.1";
    final static int PORT_SERVER = 7666;

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

    public void connect(E_Modo estadoLogin) {
        try {
            if(sock == null) {
                sock = new Socket(IP_SERVER, PORT_SERVER);
                writeData = new DataOutputStream(sock.getOutputStream()); // envio
                handleData = new DataInputStream(sock.getInputStream()); // respuesta..
            }

            incomingData.readASCIIStringFixed(incomingData.length());
            outgoingData.readASCIIStringFixed(outgoingData.length());

            switch (estadoLogin) {
                case NORMAL:
                case CREATE_NEW_CHARACTER: login(estadoLogin); break;
                case DICES: break;
            }

            readData();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void login(E_Modo estadoLogin) {
        if (estadoLogin == NORMAL) {
            writeLoginExistingChar();
        } else {
            writeLoginNewChar();
        }

        flushBuffer();
    }

    public void flushBuffer() {
        if (writeData == null || !sock.isConnected()) return;

        if (outgoingData.length() != 0){

            //new Thread(() -> {
                String sndData;
                sndData = outgoingData.readASCIIStringFixed(outgoingData.length());
                sendData(sndData);
           // }).start();

        }
    }

    public void sendData(String sdData) {
        if (sock.isConnected()){
            try {
                writeData.write(sdData.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readData() {
        if (handleData == null || !sock.isConnected()) return;

        try {
            int availableBytes = handleData.available();
            if (availableBytes > 0) {
                //System.out.println("Available Bytes: " + availableBytes);
                byte[] dataBuffer = new byte[availableBytes];

                int bytesRead = 0;

                bytesRead = handleData.read(dataBuffer);
                //System.out.println("Bytes Read: " + bytesRead);

                if (bytesRead > 0) {
                    String RD = new String(dataBuffer, 0 , bytesRead);
                    //System.out.println(RD.length());
                    byte[] data = RD.getBytes();

                    // Process the received data here
                    //System.out.println("Received data: " + RD);

                    if (RD.isEmpty()) return;

                    // Put data in the buffer
                    incomingData.writeBlock(data, -1);

                    //Send buffer to Handle data
                    handleIncomingData();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void disconnect() {
        try {
            if (sock.isConnected()) {
                writeData.close();
                handleData.close();
                sock.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}