package com.stuart.repetoire_v1_0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipesList extends AppCompatActivity {

    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        Intent in = getIntent();
        List<Recipe> recipe_list = (List<Recipe>) in.getSerializableExtra("RecipeList");
        l = findViewById(R.id.list);
        List<String> recipe_name_list=new ArrayList<String>();
        for(int i=0;i<recipe_list.size();i++){
            recipe_name_list.add(recipe_list.get(i).name);
        }

        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, recipe_name_list);
        l.setAdapter(arr);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(RecipesList.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + recipe_name_list.get(position));
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        recipe_name_list.remove(position);
                        recipe_list.remove(position);
                        arr.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });

        Button doneb=findViewById(R.id.donebutton);
        doneb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecipesList.this, AddRecipe.class);
                intent.putExtra("NewRecipeList", (Serializable) recipe_list);
                if(recipe_list.isEmpty()) {
                    setResult(2, intent);
                }
                else {
                    setResult(1, intent);
                }
                finish();
            }
        });

    }
}