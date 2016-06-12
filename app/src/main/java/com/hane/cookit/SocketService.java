package com.hane.cookit;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SocketService extends Service{
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    private static final String VAEGT_IP = "80.71.140.75"; // Vaegtens ip-addresse
    private static final int PORT = 8000;
    InetSocketAddress vaegtAddr;

    @Override
    public IBinder onBind(Intent arg0) {
        // Obligatorisk metode.
        return myBinder;
    }

    private final IBinder myBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        vaegtAddr = new InetSocketAddress(VAEGT_IP,PORT);
        socket = new Socket();
        Log.e("socketSingleton", "Socket oprettet");
    }


    public void IsBoundable(){
       // Toast.makeText(this,"I bind like butter", Toast.LENGTH_LONG).show();
    }



    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
        Log.e("onStart","Ny tråd startet");
        Runnable connect = new connectSocket();
        new Thread(connect).start();
    }

    class connectSocket implements Runnable {

        @Override
        public void run() {
            try {
                socket.connect(vaegtAddr);
                Log.e("SocketService","Connection established");
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        socket = null;
    }

    public void sendMessage(String message){
        if(out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public String readMessage(){
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

