package com.hane.cookit;

import java.io.Serializable;

/**
 * Created by Mathias on 6/7/2016.
 */
public class opskriftDTO implements Serializable {
    private int opskriftID;
    private int medarbejderID;
    private String navn;
    private String ingredienser;
    private String beskrivelse;


    //Constructor
    public opskriftDTO(int opskriftID, int medarbejderID, String navn, String ingredienser, String beskrivelse) {
        this.opskriftID = opskriftID;
        this.medarbejderID = medarbejderID;
        this.navn = navn;
        this.ingredienser = ingredienser;
        this.beskrivelse = beskrivelse;
    }


    //Get metoder
    public int getOpskriftID() {
        return opskriftID;
    }
    public int getMedarbejderID() {
        return medarbejderID;
    }
    public String getNavn() {
        return navn;
    }
    public String getIngredienser() {
        return ingredienser;
    }
    public String getBeskrivelse() {
        return beskrivelse;
    }
}
