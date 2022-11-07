package com.stuart.repetoire_v1_0;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_menu);
        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 101) {

                        }
                    }
                });

        Intent intent = new Intent(RecipeMenu.this, MainActivity.class);
        Intent in = getIntent();
        List<Recipe> recipe_list = (List<Recipe>) in.getSerializableExtra("RecipeMenu");
        ListView l=findViewById(R.id.menulist);
        List<String> recipe_name_list=new ArrayList<String>();
        for(int i=0;i<recipe_list.size();i++){
            recipe_name_list.add(recipe_list.get(i).name);
        }

        ListViewAdapterWithDelete adapterWithDelete=new ListViewAdapterWithDelete(this,recipe_list,recipe_name_list);
        l.setAdapter(adapterWithDelete);

        Button doneBtn=findViewById(R.id.donebuttonrecipemenu);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("UpdatedMenu",(Serializable) adapterWithDelete.recipes);
                setResult(5,intent);
                finish();
            }
        });

        Button shoppingListBtn=findViewById(R.id.showshoppinglist);
        shoppingListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeMenu.this, ShoppingList.class);
                intent.putExtra("MenuRecipes", (Serializable) recipe_list);
                activityResultLaunch.launch(intent);
            }
        });
    }
}