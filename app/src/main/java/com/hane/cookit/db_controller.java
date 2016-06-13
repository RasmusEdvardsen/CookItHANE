package com.hane.cookit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class db_controller implements Serializable {

    //Internal Data structure
    public List<opskriftDTO> opskrift;
    public List<opskriftTrinDTO> pandekager;
    public List<opskriftTrinDTO> chokokage;
    boolean define = true;


    public db_controller() {
        opskrift = new ArrayList<opskriftDTO>();
        pandekager = new ArrayList<opskriftTrinDTO>();
        chokokage = new ArrayList<opskriftTrinDTO>();
        initiate();
    }

    public List<opskriftDTO> getRecipes(){
        opskriftDTO obj1 = new opskriftDTO(1, 2, "Pandekager", " 50g mel, 1.25 dl mælk, 0.75 æg, 1 tsk sukker, 1 tsk olie, 12.5 g smør", "Lækre Pandekager, godtkendt af den elskede Rasmus Klump" );
        opskrift.add(obj1);
        opskriftDTO obj2 = new opskriftDTO(2, 2, "Chokoladekage"," 2 æg, 1 dl sukker, 3 spsk kakao, 1.5 dl mel, 1 tsk bagepulver, 1 tsk vaniljesukker, 0.5 dl mælk, 0.5 spsk olie, flormelis","Lækker chokoladekage anbefalet af hjerteforeningen, til ca 12 personer.");
        opskrift.add(obj2);
        return opskrift;
    }

    public opskriftTrinDTO getStep(int opskriftID, int currentStep){


        // -- for at tage højde for 0-indeksering
        if(opskriftID == 1){
            return pandekager.get(--currentStep);
        }
        else {
            return chokokage.get(--currentStep);
        }
    }

    public void initiate(){
                // PANDEKAGE TRIN
                //  public opskriftTrinDTO(int opskriftID, double maengde, int tid, int trin, String ingrediens, String enhed, String handling, boolean vaegt)
                opskriftTrinDTO obj1 = new opskriftTrinDTO(1,50,0,1,"smør", "g","Smelt smøret",true);
                pandekager.add(obj1);
                opskriftTrinDTO obj2 = new opskriftTrinDTO(1,5,0,2,"mælk","dl","Pisk mælken sammen med smøret i en skål",false);
                pandekager.add(obj2);
                opskriftTrinDTO obj3 = new opskriftTrinDTO(1,200,0,3,"mel","g","Pisk melet i skålen",true);
                pandekager.add(obj3);
                opskriftTrinDTO obj4 = new opskriftTrinDTO(1,3,0,4,"æg","stk","Pisk æggene i skålen",false);
                pandekager.add(obj4);
                opskriftTrinDTO obj5 = new opskriftTrinDTO(1,1,0,5,"Sukker","tsk","Tilsæt sukkeret og en 1/4 tsk salt",false);
                pandekager.add(obj5);
                opskriftTrinDTO obj6 = new opskriftTrinDTO(1,1,0,6,"olie","spsk","Tilsæt olien i skålen",false);
                pandekager.add(obj6);
                opskriftTrinDTO obj7 = new opskriftTrinDTO(1,0,0,7,"null","null","Pisk det hele grundigt",false);
                pandekager.add(obj7);
                opskriftTrinDTO obj8 = new opskriftTrinDTO(1,0,0,99,"null","null","Brug en lille klat smør til hver pandekage og steg dem til de gyldne på hver side",false);
                pandekager.add(obj8);


                //CHOKOLADEKAGE TRIN
                //public opskriftTrinDTO(int opskriftID, double maengde, int tid, int trin, String ingrediens, String enhed, String handling, boolean vaegt);
                opskriftTrinDTO objA = new opskriftTrinDTO(2,175,0,1,"null","null", "forvarm ovnen til 175 grader varmluft",false);
                chokokage.add(objA);
                opskriftTrinDTO objB = new opskriftTrinDTO(2,2,0,2,"æg","stk","Pisk æggene",false);
                chokokage.add(objB);
                opskriftTrinDTO objC = new opskriftTrinDTO(2,1,0,3,"sukker","dl","Pisk sukkeret sammen med æggene, til en luftig æggesnaps",false);
                chokokage.add(objC);
                opskriftTrinDTO objD = new opskriftTrinDTO(2,3,0,4,"kakao","spsk","Tilsæt og pisk kakaoen. Find en ny skål frem, til næste trin.",false);
                chokokage.add(objD);
                opskriftTrinDTO objE = new opskriftTrinDTO(2,1.5,0,5,"mel","dl", "Hæld melen i den nye skål",false);
                chokokage.add(objE);
                opskriftTrinDTO objF = new opskriftTrinDTO(2,1,0,6,"bagepulver","tsk","Tilsæt bagepulveret i melen.",false);
                chokokage.add(objF);
                opskriftTrinDTO objG = new opskriftTrinDTO(2,1,0,7,"vaniljesukker","tsk","Tilsæt vaniljesukkeret og mix det hele.",false);
                chokokage.add(objG);
                opskriftTrinDTO objH = new opskriftTrinDTO(2,0.5,0,8,"mælk","dl","Pisk mixen i dejen, sammen med mælken",false);
                chokokage.add(objH);
                opskriftTrinDTO objI = new opskriftTrinDTO(2,0.5,0,9,"olie","spsk","Tilsæt olien i dejen",false);
                chokokage.add(objI);
                opskriftTrinDTO objJ = new opskriftTrinDTO(2,0,0,10,"null","null","Fordel dejen i en lille form på 20 x 25 cm foret med bagepapir",false);
                chokokage.add(objJ);
                opskriftTrinDTO objK = new opskriftTrinDTO(2,0,25,11,"null","null","Bag kagen i 25min",false);
                chokokage.add(objK);
                opskriftTrinDTO objL = new opskriftTrinDTO(2,0,0,99,"null","null","Lad kagen køle af, og pynt med flormelis",false);
                chokokage.add(objL);
            }

}