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

    public int getOpskriftID() {
        return opskriftID;
    }
    public void setOpskriftID(int opskriftID) {
        this.opskriftID = opskriftID;
    }
    public double getMaengde() {
        return maengde;
    }
    public void setMaengde(int maengde) {
        this.maengde = maengde;
    }
    public int getTid() {
        return tid;
    }
    public void setTid(int tid) {
        this.tid = tid;
    }
    public int getTrin() {
        return trin;
    }
    public void setTrin(int trin) {
        this.trin = trin;
    }
    public String getHandling() {
        return handling;
    }
    public void setHandling(String handling) {
        this.handling = handling;
    }
    public void setMaengde(double maengde) {
        this.maengde = maengde;
    }
    public String getIngrediens() {
        return ingrediens;
    }
    public void setIngrediens(String ingrediens) {
        this.ingrediens = ingrediens;
    }
    public String getEnhed() {
        return enhed;
    }
    public void setEnhed(String enhed) {
        this.enhed = enhed;
    }
    public boolean isVaegt() {
        return vaegt;
    }
    public void setVaegt(boolean vaegt) {
        this.vaegt = vaegt;
    }
}
