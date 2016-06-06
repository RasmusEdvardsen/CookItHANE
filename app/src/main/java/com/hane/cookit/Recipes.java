package com.hane.cookit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Recipes extends AppCompatActivity {

    public List<String> getRecipes(){
        Connection con = null;
        int retur = 0;
        List<String> RecipeList = new ArrayList<>();
        try {
            con = DriverManager.getConnection("jdbc:mysql://80.71.140.75:3306/02324", "DTU", "HejVen123");
            retur = 1;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT navn from opskrift");
            while(rs.next()){
                String temp = rs.getString("navn");
                RecipeList.add(temp);
                System.out.println(temp);
            }
        } catch (SQLException e) {
            retur = -1;
            System.out.println(e);
            System.out.println(retur);
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        getRecipes();
    }
}
