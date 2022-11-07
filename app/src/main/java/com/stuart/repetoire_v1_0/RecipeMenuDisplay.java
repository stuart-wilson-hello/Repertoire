package com.stuart.repetoire_v1_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class RecipeMenuDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_menu_display);

        Intent in = getIntent();
        Recipe recipe =  in.getParcelableExtra("SingleRecipe");



    }
}