package com.hane.cookit;

import java.io.Serializable;

/**
 * Created by Mathias on 6/7/2016.
 */
public class opskriftTrinDTO implements Serializable {

    private int opskriftID;
    private double maengde;
    private int tid;
    private int trin;
    private String handling;
    private String ingrediens;
    private String enhed;
    private boolean vaegt;

    //Constructor
    public opskriftTrinDTO(int opskriftID, double maengde, int tid, int trin, String ingrediens, String enhed, String handling, boolean vaegt) {
        this.opskriftID = opskriftID;
        this.maengde = maengde;
        this.tid = tid;
        this.trin = trin;
        this.ingrediens = ingrediens;
        this.enhed = enhed;
        this.handling = handling;
        this.vaegt = vaegt;
    }


    // Get metoder
    public int getOpskriftID() {
        return opskriftID;
    }
    public double getMaengde() {
        return maengde;
    }
    public int getTid() {
        return tid;
    }
    public int getTrin() {
        return trin;
    }
    public String getHandling() {
        return handling;
    }
    public String getIngrediens() {
        return ingrediens;
    }
    public String getEnhed() {
        return enhed;
    }
    public boolean isVaegt() {
        return vaegt;
    }
}
