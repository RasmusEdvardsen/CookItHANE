package com.hane.cookit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class step_controller extends AppCompatActivity {

    //OnCreate
    TextView tv,ig,itv,intro;
    Button start;
    opskriftDTO temp;
    int currentStep = 1;
    db_controller db;

    //Start
    TextView overskrift, handling, timerTV;
    Button nextStep, extraButton;
    String setText;
    int min, tid;

    protected void onCreate(Bundle savedInstanceState){
        //Standard initiate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        //Retrieve DTO
        temp = (opskriftDTO) this.getIntent().getExtras().getSerializable("opskrift");

        //Retrieve DB
        db = (db_controller) this.getIntent().getExtras().getSerializable("db");

        //Overskrift
        tv  = (TextView) findViewById(R.id.opskriftTV);
        tv.setText(temp.getNavn());

        //Intro
        intro = (TextView) findViewById(R.id.introTV);
        intro.setText(temp.getBeskrivelse());

        //Instruks
        itv = (TextView) findViewById(R.id.instruksTV);
        itv.setText("Ingredienser:");

        //String manipulation af ingredienser.
        String ingredienser = temp.getIngredienser();
        String[] split = ingredienser.split(",");
        String ready = "";
        for(int i = 0; i < split.length; i++){
            ready = ready + " \u2022 "+split[i]+ "\n";
        }

        //Ingrediensview
        ig = (TextView) findViewById(R.id.ingrediensTV);
        ig.setText(ready);

        //Start knappen
        start = (Button) findViewById(R.id.startOpskrift);
        start.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                setContentView(R.layout.steps);
                nextStep = (Button) findViewById(R.id.nextStepButton);
                nextStep.setText("Næste trin");
                overskrift = (TextView) findViewById(R.id.stepOverskriftTV);
                handling = (TextView) findViewById(R.id.handlingTV);
                extraButton = (Button) findViewById(R.id.startVejningButton);
                run(db.getStep(temp.getOpskriftID(),1));
            }
        });



    }

    public void run(final opskriftTrinDTO step){

        final Bundle b = new Bundle();
        b.putSerializable("DTO",step);

        extraButton.setVisibility(View.INVISIBLE);
        //Flow control fra step 1, til sidste step.
        if(step.getTrin()== 99){
            //Trin 99, er en betegnelse vi bruger til at definere sidste step
            setText = "Sidste trin";
            nextStep.setText("Afslut");

            //onClickListener der går til Recipe listen
            nextStep.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v){
                    Intent goToRecipes = new Intent(step_controller.this, Recipes.class);
                    startActivity(goToRecipes);
                }
            });
        } else {
            setText = "Trin " + step.getTrin();
            //onClickListener til næste trin.
            nextStep.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v){
                    int i,j;
                    i = step.getOpskriftID();
                    j = step.getTrin();
                    run(db.getStep(i,++j));
                }
            });
        }
        //Set trin overskrift med nummer.
        overskrift.setText(setText);
        handling.setText(step.getHandling());
        
        //Hvis vægten skal bruges
        if(step.isVaegt()){

            extraButton.setText("Vejning");
            extraButton.setVisibility(View.VISIBLE);
            nextStep.setEnabled(false);
            extraButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v){
                    //startVejnin
                    Intent goToVaegt = new Intent(step_controller.this, Vaegt_Controller.class);
                    goToVaegt.putExtras(b);
                    startActivity(goToVaegt);
                    nextStep.setEnabled(true);
                }
            });
        }

        //Hvis der er en Timer.
        if((min=step.getTid())!=0){
            timerTV = (TextView) findViewById(R.id.timerTV);
            extraButton.setText("Start timer");
            extraButton.setVisibility(View.VISIBLE);
            tid = step.getTid()*60000;
            extraButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v){
                    extraButton.setVisibility(View.INVISIBLE);
                    timerTV.setVisibility(View.VISIBLE);
                 new CountDownTimer(tid, 1000) { // adjust the milli seconds here

                        public void onTick(long millisUntilFinished) {
                            timerTV.setText(""+String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }

                        public void onFinish() {
                            timerTV.setText("done!");
                            timerTV.setVisibility(View.INVISIBLE);
                        }
                    }.start();
                }
            });
        }

    }
}
