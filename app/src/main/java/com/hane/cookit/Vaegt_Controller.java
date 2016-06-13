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
    TextView vaegtSvar, vaegtIntro;
    Button buttonVidere, buttonTidligere, buttonOk;
    opskriftTrinDTO dto;
    private VaegtKlient vaegt;
    Socket socket;

    // ---------------------------------------------------------------------------------------------

    // OnCreate metodik
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaegt_intro);
        Log.e("VaegtCtrl:onCreate", "Igangsat");

        // Tekstfelter og knap defineres for introduktionen
        buttonOk = (Button) findViewById(R.id.vaegtOk);
        vaegtIntro = (TextView) findViewById(R.id.vaegtIntroduktion);
        Log.e("VaegtCtrl:onCreate","Knap/tekstfelt oprettet");

        Log.e("VaegtCtrl:onCreate","Ok knap defineret");
        // Funktionalitet for "Ok"-knappen
        buttonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* buttonVidere.setVisibility(View.VISIBLE); // Gør "Næste trin"-knappen synlig
                buttonTidligere.setVisibility(View.VISIBLE); // Gør "Forrige trin"-knappen synlig
                // vaegtSvar.setText(""); // Nulstilles bare for at kunne opdateres af OnProgressUpdate
                buttonOk.setVisibility(View.INVISIBLE); // Gør, at "Ok"-knappen forsvinder
                buttonOk.setEnabled(false); // Gør, at der ikke kan trykkes på "Ok"-knappen
                vaegt.setOkButtonPressed(true); // Husker, at brugeren nu har trykket "Ok" */
                setContentView(R.layout.activity_vaegt_controller);

                // Diverse tekstfelter og knapper defineres for vægt-sekvensen
                vaegtSvar = (TextView) findViewById(R.id.vaegtSvar);
                buttonVidere = (Button)findViewById(R.id.vaegtGodkendt);
                buttonTidligere = (Button)findViewById(R.id.forrigeTrin);

                // Funktionalitet for "Næste Trin"-knappen
                // Der kan ikke trykkes på denne knap før at en betingelse er mødt, se "onProgressUpdate"
                buttonVidere.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {

                        // vaegt.setOkButtonPressed(false); // Resetter "Ok"-knappen

                        finish(); // Afslutter sekvensen (Native metode)
                    }
                });

                // Forbind til vægten
                Log.e("vaegtCtrl:onClick","aktiverVaegt kaldes");
                new aktiverVaegt().execute();
            }
        });
        Log.e("VaegtCtrl:onCreate","Ok-knap oprettet");

        //Retrieve DTO
        dto = (opskriftTrinDTO) this.getIntent().getExtras().getSerializable("DTO");
        Log.e("VaegtCtrl:onCreate","DTO hentet");

        /*
        // Funktionalitet for "Forrige Trin"-knappen
        buttonTidligere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // .....
            }
        }); */

    } // Method end

    // Metodik til at invokere VaegtKlient
    public class aktiverVaegt extends AsyncTask<String, String, VaegtKlient> {


        // Metodik til at køre "VaegtKlient" i baggrunden
        @Override
        protected VaegtKlient doInBackground(String... message) {

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

