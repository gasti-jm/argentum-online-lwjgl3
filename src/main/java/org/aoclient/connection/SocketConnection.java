package org.aoclient.connection;

import org.aoclient.connection.packets.E_Modo;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Arrays;

import static org.aoclient.connection.Protocol.*;
import static org.aoclient.connection.packets.E_Modo.NORMAL;

public class SocketConnection {
    private static SocketConnection instance;


    final static String IP_SERVER = "127.0.0.1";
    final static int PORT_SERVER = 7666;

    Socket sock;
    DataOutputStream writeData;
    DataInputStream handleData;

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
            if(!isConnected()) {
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
        if (outgoingData.length() != 0){

            new Thread(() -> {
                String sndData;
                sndData = outgoingData.readASCIIStringFixed(outgoingData.length());
                sendData(sndData);
            }).start();

        }
    }

    public void sendData(String sdData) {
        if (isConnected()){
            try {
                writeData.write(sdData.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readData() {

    }

    public boolean isConnected() {
        return sock != null;
    }

    public void disconnect() {
        try {
            if (isConnected()) {
                writeData.close();
                handleData.close();
                sock.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
