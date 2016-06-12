package com.hane.cookit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Controller til at kontrollere forbindelsen mellem vaegten og appen
 */

public class Vaegt_Controller extends AppCompatActivity {

    // Attributter
    // ---------------------------------------------------------------------------------------------
    TextView vaegtSvar;
    Button buttonVidere, buttonTidligere, buttonOk;
    opskriftTrinDTO dto;
    private VaegtKlient vaegt;
    Socket socket;

    // ---------------------------------------------------------------------------------------------

    // OnCreate metodik
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaegt_controller);
        // Diverse tekstfelter og knapper defineres
        vaegtSvar = (TextView) findViewById(R.id.vaegtSvar);
        buttonVidere = (Button)findViewById(R.id.vaegtGodkendt);
        buttonTidligere = (Button)findViewById(R.id.forrigeTrin);
        buttonOk = (Button) findViewById(R.id.vaegtOk);
        Log.e("Aktive socket", "socket: "+ socket);
        Log.e("VaegtController", "Vaegt-objekt: " + vaegt);

        //Retrieve DTO
        dto = (opskriftTrinDTO) this.getIntent().getExtras().getSerializable("DTO");

          // Forbind til vægten
        new aktiverVaegt().execute();

        Log.e("VaegtController", "Vaegt-objekt_2: " + vaegt);

        // Funktionalitet for "Ok"-knappen
        buttonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonVidere.setVisibility(View.VISIBLE); // Gør "Næste trin"-knappen synlig
                buttonTidligere.setVisibility(View.VISIBLE); // Gør "Forrige trin"-knappen synlig
                // vaegtSvar.setText(""); // Nulstilles bare for at kunne opdateres af OnProgressUpdate
                buttonOk.setVisibility(View.INVISIBLE); // Gør, at "Ok"-knappen forsvinder
                buttonOk.setEnabled(false); // Gør, at der ikke kan trykkes på "Ok"-knappen
                vaegt.setOkButtonPressed(true); // Husker, at brugeren nu har trykket "Ok"
            }
        });

        // Funktionalitet for "Forrige Trin"-knappen
        buttonTidligere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // .....
            }
        });

        // Funktionalitet for "Næste Trin"-knappen
        // Der kan ikke trykkes på denne knap før at en betingelse er mødt, se "onProgressUpdate"
        buttonVidere.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                // vaegt.setOkButtonPressed(false); // Resetter "Ok"-knappen

                finish(); // Afslutter sekvensen (Native metode)



            }
        });

    } // Method end

    // Metodik til at invokere VaegtKlient
    public class aktiverVaegt extends AsyncTask<String, String, VaegtKlient> {


        // Metodik til at køre "VaegtKlient" i baggrunden
        @Override
        protected VaegtKlient doInBackground(String... message) {

            // Åbner socket vha. hjælpemetode
            openSocket();
            Log.e("VaegtController", "" + socket);

            // Opretter VaegtKlient objekt
            // Parametre skal aendres til at hente ingredienser for hvert trin
            vaegt = new VaegtKlient(dto.getMaengde(),dto.getEnhed(),dto.getIngrediens(), socket, new VaegtKlient.receiveInterface() {

                @Override
                public void receive(String message) {
                    publishProgress(message);
                }

                @Override
                public boolean condition(boolean con) {
                    return vaegt.getCondition();
                }

            });

            Log.e("VaegtController", "Objekt_3: " + vaegt);
            vaegt.run();

            return null;
        }

        // Metodik til at opdatere attributter og tekstfelter automatisk
        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
            vaegtSvar.setText(values[0]); // Ændrer tekst på skærmen

            // Kontrollere, om brugeren kan gå videre
            if (vaegt.getCondition()) { // Hvis brugeren har vejet korrekt af...
                buttonVidere.setEnabled(true); // Så kan de trykker på "Næste Trin"-knappen
            }
            // Log.e("VaegtController", "Condition: " + vaegt.getCondition());
        }

        @Override
        protected void onCancelled() {
            // Make sure we clean up if the task is killed
            Log.e("VaegtController", "AsyncTask cancel");
            super.onCancelled();

        }
    } // aktiverVaegt end

    // Hjaelpemetoder
    // ---------------------------------------------------------------------------------------------

    // Hjaelpemetode til at åbne vores socket via socketSingleton
    private void openSocket() {

        socket = socketSingleton.getSocket();
    }

    // Hjaelpemetode til at resette knappernes indstillinger
    private void buttonReset() {
        buttonVidere.setVisibility(View.INVISIBLE); // Gør "Næste trin"-knappen synlig
        buttonTidligere.setVisibility(View.INVISIBLE); // Gør "Forrige trin"-knappen synlig
        vaegtSvar.setText(""); // Nulstilles bare for at kunne opdateres af OnProgressUpdate
        buttonOk.setVisibility(View.VISIBLE); // Gør, at "Ok"-knappen forsvinder
        buttonOk.setEnabled(true); // Gør, at der ikke kan trykkes på "Ok"-knappen
    }

    // ---------------------------------------------------------------------------------------------
} // main end

