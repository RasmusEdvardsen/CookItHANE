package com.hane.cookit;

import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by haugaard on 6/11/16.
 */
public class socketSingleton{

    private static final String VAEGT_IP = "80.71.140.75"; // Vaegtens ip-addresse
    private static final int PORT = 8000;

    private static Socket socketInstance = null;

    // private Socket socket;

    /*
    * A private Constructor prevents any other
    * class from instantiating.
    */
    private static void socketSingleton() {

        try {
            InetAddress vaegtAddr = InetAddress.getByName(VAEGT_IP);
            socketInstance = new Socket(vaegtAddr, PORT);
            Log.e("socketSingleton", "Socket oprettet");
        } catch (UnknownHostException Ue) {
            Log.e("socketSingleton", "UnknownHostException kastet");
            Ue.printStackTrace();
        } catch (IOException e) {
            Log.e("socketSingleton", "IOException kastet");
            e.printStackTrace();
        }
    }

    public static Socket getSocket() {

        if (socketInstance == null) {
            socketSingleton();
        }
        Log.e("socketSingleton", "socket returneret:");
        return socketInstance;
    }

    public void closeSocket() {

        try {
            socketInstance.close();
        } catch (IOException e) {
            Log.e("socketSingleton","IOException - closeSocket");
            e.printStackTrace();
        }

    }
}

