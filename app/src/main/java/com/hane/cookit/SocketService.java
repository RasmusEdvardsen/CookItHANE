package com.hane.cookit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketService extends Service{
    private Socket socket;
    PrintWriter out;
    BufferedReader in;
    private static final String VAEGT_IP = "80.71.140.75"; // Vaegtens ip-addresse
    private static final int PORT = 8000;
    InetSocketAddress vaegtAddr;

    @Override
    //-------------------------------------------------------------------------
    // Binder er obligatorisk, og bruges til at binde servicen til en Activity.
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

    private final IBinder myBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }
    // Binder slut
    // -------------------------------------------------------------------------


    @Override
    // Denne metode køres når en instans af SocketService bliver oprettet
    public void onCreate() {
        super.onCreate();
        vaegtAddr = new InetSocketAddress(VAEGT_IP,PORT); //Create Socket address til senere brug
        socket = new Socket();  //initiate Socket;
        Log.e("SocketService:onCreate:", "Socket oprettet");
    }

    @SuppressWarnings("deprecation")
    @Override
    // Denne metode køres når startService() bliver kaldt. StartService er en native metode til Service
    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
        Runnable connect = new connectSocket(); //Ny tråd initiate
        new Thread(connect).start();    //Thread startes. .start() kører run() metoden nedenfor
        Log.e("SocketService:onStart:","Ny tråd startet");
    }

    // Socket og read/write i Tråd
    class connectSocket implements Runnable {
        @Override
        public void run() {
            try {
                socket.connect(vaegtAddr);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.e("SocketService:run:","Connection established");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    //Obligatorisk metode, når Servicen skal nedlægges
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
            out.close();
            in.close();
            Log.e("SocketService:onDestroy","I/O lukket");
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;
    }

    //Send besked til vægten
    public void sendMessage(String message){
        if(out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            Log.e("SocketService:sendMsg","Message send: "+message);
        }
    }

    //Læs besked fra vægten
    public String readMessage(){
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

