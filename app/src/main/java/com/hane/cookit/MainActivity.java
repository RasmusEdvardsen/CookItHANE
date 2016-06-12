package com.hane.cookit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {


    // Socket attributer
    private static SocketService ss;
    private Boolean isBound;
    public MainActivity ma;

    //MainActivity attributer
    Button buttonRecipes;

    public void setButtonRecipes(){
        buttonRecipes = (Button)findViewById(R.id.buttonRecipes);
        buttonRecipes.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent goToRecipes = new Intent(MainActivity.this, Recipes.class);
                startActivity(goToRecipes);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ma = this;
        setContentView(R.layout.activity_main);
        setButtonRecipes();
        startService(new Intent(MainActivity.this,SocketService.class));
        doBindService();
    }

    //Socket logik:
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e("ServiceConnection","Får vi etableret en ServiceConnection");
            ss = ((SocketService.LocalBinder)service).getService();

        }
        public void onServiceDisconnected(ComponentName className) {
            ss = null;
        }
    };

    private void doBindService() {
        Log.e("doBindService","Kører doBindService() ?");
        bindService(new Intent(MainActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
        //ss.IsBoundable();
    }


    private void doUnbindService() {
        Log.e("doUnbindService","Vi unbinder Kører succesfuldt.");
        if (isBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        Log.e("onDestroy","onDestroy() Kører succesfuldt.");
        super.onDestroy();
        doUnbindService();
    }

    public static SocketService getService(){
        return ss;
    }



    //Socket logik end.


}
