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

public class MainActivity extends AppCompatActivity {

    // Socket attributer
    private static SocketService ss;
    private Boolean isBound;
    public MainActivity ma;

    //MainActivity attributer
    Button buttonRecipes;

    //Metode kaldt i onCreate
    public void setButtonRecipes(){
        //Logik til Recipe knappen.
        buttonRecipes = (Button)findViewById(R.id.buttonRecipes); //Find html objekt.
        buttonRecipes.setOnClickListener(new View.OnClickListener() //Lytter efter Click.
        {
            public void onClick(View v){
                //Nyt intent startes, for at skifte activity.
                Intent goToRecipes = new Intent(MainActivity.this, Recipes.class);
                //Skift activty.
                startActivity(goToRecipes);
            }
        });
    }

    @Override
    //Denne metode kører ved opstart af app.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ma = this; //Bruges af SocketService logik.
        setContentView(R.layout.activity_main); //Sætter layout på Activitien.
        setButtonRecipes(); //Kalder førnævnte metode.
        startService(new Intent(MainActivity.this,SocketService.class)); //Starter SocketService.
        doBindService(); //Binder SocketService til denne Activity.
    }




    //SocketService Initialisering og logik nødvendig, for kommunikation imellem SocketService og Activities.
    //-----------------------------------------------------------------------------------------------
    //Vores SocketService som et Objekt i denne activity.
    private ServiceConnection mConnection = new ServiceConnection() {
        //Kaldes når vi connecter til vores Service.
        public void onServiceConnected(ComponentName className, IBinder service) {
            ss = ((SocketService.LocalBinder)service).getService(); //Connecter og Binder servicen til vores lokale SocketService objekt.
            Log.e("MainScript","Forbindelse oprettet");
        }
        //Kaldes hvis vi mister forbindelsen til vores Service.
        public void onServiceDisconnected(ComponentName className) {
            ss = null;
        }
    };

    // MainActivity.getService().sendMessage("String");



    //Denne metode binder servicen til vores MainActivity, så vi kan kalde metoder igennem, denne activity.
    private void doBindService() {
        bindService(new Intent(MainActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
        Log.e("doBindService","Kører doBindService() ?");
    }

    //unbinder service fra denne activity.
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
