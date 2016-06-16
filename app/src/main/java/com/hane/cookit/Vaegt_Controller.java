package com.hane.cookit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    // til onCreate
    TextView vaegtSvar, vaegtIntro;
    Button buttonVidere, buttonTidligere, buttonOk;
    opskriftTrinDTO dto;
    String units;

    // til AsyncTask;
    double maengde, margin, safety = 1.05;
    String enhed, ingrediens, vaegtOutput, tara, setText;
    Handler refresh;
    aktiverVaegt tempAV;
    int vaegtOutputInt;

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaegt_intro);

        //GUI Handler
        refresh = new Handler(Looper.getMainLooper());

        //Retreive DTO
        dto = (opskriftTrinDTO) this.getIntent().getExtras().getSerializable("DTO");

        //Attributter udfyldt med data fra DTO
        enhed = dto.getEnhed();
        ingrediens = dto.getIngrediens();
        maengde = dto.getMaengde();
        units = " " + maengde + " " + enhed + " " + ingrediens;

        //Vores sikkerhedsmargin udregnes.
        margin = (maengde * safety) - maengde;

        vaegtIntro = (TextView) findViewById(R.id.vaegtIntroduktion);
        buttonOk = (Button) findViewById(R.id.vaegtOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_vaegt_controller);
                vaegtSvar = (TextView) findViewById(R.id.vaegtSvar);

                buttonVidere = (Button)findViewById(R.id.vaegtGodkendt);
                buttonVidere.setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View v) {
                        Log.e("tempAV:","Første cancel");
                        tempAV.cancel(true);
                        Log.e("Finish()","Anden cancel");
                        finish();
                    }
                });
                tempAV = new aktiverVaegt();
                tempAV.execute();
                Log.e("VaegtCtlr:OnClick","buttonOK gennemført");
            }
        });
    } //onCreate slut

    //GUI Metoder
    //-------------------------------------------------------------------------------------------
    private void minText(String message, TextView tv){
        tv.setText(message);
    }

    private void enableButton(Button button) {
        button.setEnabled(true);
    }

    //-------------------------------------------------------------------------------------------

    public class aktiverVaegt extends AsyncTask<Void, Void, Void> {

        // Metodik til at køre "VaegtKlient" i baggrunden
        @Override
        protected Void doInBackground(Void... arg0) {
            Log.e("VaegtCtrl:doInBckgrd","Started");

            refresh.post(new Runnable() {
                public void run()
                {minText(getString(R.string.tara), vaegtSvar);}
            });

            Log.e("VaegtCtrl:doInBckgrd","Venter på tara");
            MainActivity.getService().sendMessage("T");
            while ((tara = MainActivity.getService().readMessage()) != null) {
                break; // Break for bare at komme videre i sekvensen...
            }
            Log.e("VaegtCtrl:doInBckgrd","Tara modtaget");
            setText = "Begynd nu at veje " + units + " op";
            refresh.post(new Runnable() {
                public void run()
                {
                    minText(setText, vaegtSvar);
                }
            });

            Log.e("VaegtCtrl:doInBckgrd","Venter på vægt");
            MainActivity.getService().sendMessage("RM");

            while ((vaegtOutput = MainActivity.getService().readMessage()) != null) {
                Log.e("VaegtCtlr: doInBckgrd","vaegtOutput modtaget: " + vaegtOutput);
                try {
                    vaegtOutputInt = Integer.parseInt(vaegtOutput);
                } catch (NumberFormatException Ne) {
                    Log.e("VaegtCtrl:doInBckgrd","NumberFormatException");
                    refresh.post(new Runnable() {
                        public void run()
                        {minText("Prøv igen!", vaegtSvar);}
                    });
                    MainActivity.getService().sendMessage("RM");
                    continue;
                }
                // Kontrollerer om brugeren har vejet op inden for en 5 % sikkerhedsmargin
                if (vaegtOutputInt > maengde + margin || maengde - margin > vaegtOutputInt) {
                    refresh.post(new Runnable() {
                        public void run()
                        {minText("Forkert afvejning: " + vaegtOutput + " gram\nDu skal bruge " + units, vaegtSvar);}
                    });
                    MainActivity.getService().sendMessage("RM");
                } else {
                    refresh.post(new Runnable() {
                        public void run()
                        {
                            enableButton(buttonVidere);
                        }
                    });
                    refresh.post(new Runnable() {
                        public void run()
                        {minText("Korrekt afvejning: " + vaegtOutput + " gram\nDu kan nu fortsætte ved at trykke på knappen \"Næste trin\"", vaegtSvar);}
                    });
                    break;
                }
            }
            Log.e("AsyncTask: END","Afslutning af AsyncTask ???");
            return null;
        }

        @Override
        protected void onCancelled() {
            // Make sure we clean up if the task is killed
            Log.e("VaegtController", "AsyncTask cancel");
            super.onCancelled();
        }
    } // aktiverVaegt end
} // main end

