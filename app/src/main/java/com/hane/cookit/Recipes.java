package com.hane.cookit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Recipes extends AppCompatActivity {

    db_controller db = new db_controller();
    SocketService ss;
    Boolean isBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        //Fylder StringArray med navnene på opskrifter
        final List<opskriftDTO> array =  db.getRecipes();
        String[] output = new String[array.size()];
        for(int i = 0; i < array.size(); i++){
            output[i] = array.get(i).getNavn();
        }

        //Laver et ListView med navnene fra StringArray'et
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listview,output);
        ListView ListView = (ListView) findViewById(R.id.recipes);
        ListView.setAdapter(adapter);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){

                Intent goToSteps = new Intent(Recipes.this, step_controller.class);
                Bundle b = new Bundle();
                b.putSerializable("opskrift",array.get(position));
                b.putSerializable("db",db);
                goToSteps.putExtras(b);
                startActivity(goToSteps);
            }
        });
    }
}
