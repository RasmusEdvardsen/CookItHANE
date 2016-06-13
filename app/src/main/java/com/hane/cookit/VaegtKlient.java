package com.hane.cookit;

import android.app.Activity;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Klasse til at oprette en forbindelse imellem applikationen og vaegten som klient - server
 */
public class VaegtKlient {

    // Attributter
    // ---------------------------------------------------------------------------------------------
    private boolean condition = false, indledning = true, okButtonPressed = false;
    private double vaegt;
    private String enhed, ingrediens;
    private int vaegtOutputInt;

    private String vaegtOutput = "", tara = "";

    private double Sikkerhedsmargin;

    // Initialiserer interface
    private receiveInterface listener = null;
    // ---------------------------------------------------------------------------------------------

    // Konstruktoer
    public VaegtKlient(double vaegt, String enhed, String ingrediens, Socket socket, receiveInterface listener) {

        this.vaegt = vaegt; // Forventede vægt til trinnet
        this.enhed = enhed; // Enhed for ingrediens til trinnet
        this.ingrediens = ingrediens; // Ingrediens for trinnet
        this.listener = listener; // Meddeler UI
    }

    // Run metode
    public void run() {

        try {

            /*
            // Boot af vægten
            if (indledning) {
                // Indledning til vægten
                listener.receive("Du skal nu igang med at bruge vægten\n" +
                        "Dette består af 2 trin:\n" +
                        "\t\t 1. Indtastning af tara \n" +
                        "\t\t 2. Instastning af vægten \n" +
                        "Du vil blive guidet igennem trinene \n" +
                        "Tryk på \"Ok\" for at begynde");
                indledning = false; // Husker, at indledningen allerede er blevet vist
                Log.e("VaegtKlient:Run", "Indledning kørt og registreret");
            }
            else {
                listener.receive("Du skal nu bruge vægten igen. Dette komme til at foregå på samme måde\n" +
                        "som tidligere. Tryk \"Ok\" for at begynde");
            }

            Log.e("VaegtKlient:Run","Venter på tryk på \"Ok\"");
            while (true) {
                if (okButtonPressed) {
                    break; // While og break bare for at opnå at en tilstand er mødt inden, at der
                }          // fortsættes i sekvensen
            }
            */

            // Her modtages tara
            listener.receive("Sæt din skål på vægten og reset den ved at trykke på \"reset\"");
            MainActivity.getService().sendMessage("T");
            Log.e("VaegtKlient", "Efterspurgt tara");
            while ((tara = MainActivity.getService().readMessage()) != null) {
                break; // Break for bare at komme videre i sekvensen...
            }
            Log.e("VaegtKlient:Run", "Tara modtaget:" + tara);

            // Her modtages så vægten
            listener.receive("Begynd nu at veje " + vaegt + " " + enhed + " " +
                    ingrediens + " op"); // Standardbesked
            MainActivity.getService().sendMessage("RM");
            Log.e("VaegtKlient:Run", "Efterspørger vægt");

            Log.e("VaegtKlient:Run", "Venter på vægt");
            while ((vaegtOutput = MainActivity.getService().readMessage()) != null) {

                Log.e("VaegtKlient:Run", "Vægt modtaget: " + vaegtOutput);

                if (vaegtOutput != null && listener != null) {

                    try {
                        vaegtOutputInt = Integer.parseInt(vaegtOutput);
                    } catch (NumberFormatException Ne) {
                        Log.e("VaegtKlient:Run", "NumberFormatException: " + vaegtOutput);
                        listener.receive("Prøv igen!");
                        MainActivity.getService().sendMessage("RM");
                        continue;
                    }

                    Sikkerhedsmargin = (vaegt * 1.05) - vaegt; // 1.05 svarer til 5 %

                    // Kontrollere om brugeren har vejet op inden for en 5 % sikkerhedsmargin
                    if (vaegtOutputInt > vaegt + Sikkerhedsmargin
                            || vaegt - Sikkerhedsmargin > vaegtOutputInt) {

                        listener.receive("Forkert afvejning: " + vaegtOutput + " gram\nJuster din afvejning");
                        Log.e("VaegtKlient", "Forkert data - venter på nyt input");
                        MainActivity.getService().sendMessage("RM");
                    }
                    else {
                        condition = true; // Tillader brugeren at trykke på "Næste trin"-knappen
                        listener.receive("Korrekt afvejning: " + vaegtOutput + " gram\nDu kan nu fortsætte ved at" +
                                " trykke på knappen \"Næste trin\"");
                        Log.e("VaegtKlient:Run", "Korrekt data");
                        Log.e("VaegtKlient:Run","Condition: " + condition);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("VaegtKlient:Run", "Exception kastet");
            e.printStackTrace();
        }
    } // Run end

    // Definere interface
    public interface receiveInterface {

        public void receive(String besked);
        public boolean condition(boolean con);
    }

    // Hjaelpemetoder
    // ---------------------------------------------------------------------------------------------

    // Getter-metode (af condition) til at fortælle, om brugeren har vejet korrekt af
    public boolean getCondition() {

        return condition;
    }

    // Setter-metode for okButtonPressed
    public void setOkButtonPressed(boolean bool) {

        okButtonPressed = bool;
    }

    // ---------------------------------------------------------------------------------------------
}

