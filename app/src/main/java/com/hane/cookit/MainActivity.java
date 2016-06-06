package com.hane.cookit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonRecipes;

    public void setButtonRecipes(){
        buttonRecipes = (Button)findViewById(R.id.buttonRecipes);
        buttonRecipes.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent goToRecipes = new Intent(MainActivity.this, Recipes.class);
                startActivity(goToRecipes);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonRecipes();
    }
}
