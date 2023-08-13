package org.aoclient.connection;

import java.net.*;
import java.io.*;

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

    public void initialize() {
        try {
            sock = new Socket(IP_SERVER, PORT_SERVER);
            writeData = new DataOutputStream(sock.getOutputStream());
            writeData.writeByte(1);

            //System.out.println(handleData.readInt());

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
