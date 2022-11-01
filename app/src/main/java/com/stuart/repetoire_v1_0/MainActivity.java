package com.stuart.repetoire_v1_0;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Object;
import java.util.stream.Collectors;
import java.util.*;
import java.util.stream.*;

public class MainActivity extends AppCompatActivity {

    List<Recipe> recipe_list = new ArrayList<Recipe>();
    String recipe_file="recipe_file1.txt";
    List<String> all_ingredients=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.recipetext);
        String debug=getRecipeList();
        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 123) {
                            tv.setClickable(false);
                            tv.setAllCaps(false);
                            tv.setText("Added Recipe to your Repetoire!!");
                            Recipe new_recipe=result.getData().getParcelableExtra("Recipe");
                            writeToFile(new_recipe);
                            recipe_list.add(new_recipe);
                            addToAllIngredients(new_recipe);
                        } else if(result.getResultCode() == 321) {
                            tv.setClickable(false);
                            tv.setAllCaps(false);
                            tv.setText("Cant add blank link or name");
                        } else if(result.getResultCode() == 1 || result.getResultCode() == 2 || result.getResultCode() == 101 ) {
                            if(result.getResultCode() == 1){
                                recipe_list=(List<Recipe>) result.getData().getSerializableExtra("NewRecipeList");
                            }
                            else if(result.getResultCode() == 2){
                                recipe_list.removeAll(recipe_list);
                            }
                            writeFullListToFile(recipe_list);
                            tv.setClickable(false);
                            tv.setAllCaps(false);
                            tv.setText("Welcome to Repetoire");
                        }
                    }
                });


        Random rd =new Random();
        Button btn =findViewById(R.id.randombutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipe_list.isEmpty()) {
                    tv.setAllCaps(false);
                    tv.setText("You have no Recipes in your Repetoire");
                    tv.setClickable(false);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, ShowRecipe.class);
                    intent.putExtra("RecipeList", (Serializable) recipe_list);
                    intent.putExtra("AllIngredients", (Serializable) all_ingredients);
                    activityResultLaunch.launch(intent);
                }
            }
        });

        Button recipebtn=findViewById(R.id.addbutton);
        recipebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mStartForResult.launch(new Intent(MainActivity.this, AddRecipe.class));
                Intent intent = new Intent(MainActivity.this, AddRecipe.class);
                intent.putExtra("RecipeList", (Serializable) recipe_list);
                intent.putExtra("AllIngredients", (Serializable) all_ingredients);
                activityResultLaunch.launch(intent);
            }
        });
    }

    protected String getRecipeList() {
        FileInputStream fis = null;
        File file = new File(getFilesDir().getAbsolutePath()+"//"+recipe_file);
        if (file.exists()) {
            if(!file.canRead()){
                return "Its there, but i cant read?";
            }
            if(file.isDirectory()){
                return "Its a directory?";
            }
            try {
                fis = new FileInputStream(file.getAbsoluteFile());
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line = reader.readLine();
                Recipe stored_recipe;
                while(line != null){
                    String[] line_parts=line.split("\t");
                    List<String> ingredients_array=new ArrayList<String>();
                    for(int i=2; i<line_parts.length;i++){
                        ingredients_array.add(line_parts[i]);
                    }
                    stored_recipe=new Recipe(line_parts[0], line_parts[1], ingredients_array);
                    recipe_list.add(stored_recipe);
                    line = reader.readLine();
                }
                fis.close();
                setAllIngredients();
                return "Got something!";
            } catch (FileNotFoundException e) {
                return "File not found! "+getFilesDir().getAbsolutePath()+"/"+recipe_file +" "+file.getAbsolutePath();
            } catch (IOException e) {
                return "Other exception!";
            }
        }
        else {
            return "not sure whatll happen "+file.getAbsolutePath();
        }
    }

    private void writeToFile(Recipe recipe) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(recipe_file, Context.MODE_APPEND));
            outputStreamWriter.write(recipe.name+"\t"+recipe.link+"\t"+recipe.ingredientsString+"\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }

    private void writeFullListToFile(List<Recipe> recipes) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(recipe_file, Context.MODE_PRIVATE));
            for(int i=0;i<recipes.size();i++) {
                outputStreamWriter.write(recipes.get(i).name + "\t" + recipes.get(i).link + "\t"+recipes.get(i).ingredientsString+"\n");
            }
            outputStreamWriter.close();
            all_ingredients.clear();
            setAllIngredients();
        }
        catch (IOException e) {
        }
    }

    private void setAllIngredients() {
        for(int i=0;i<recipe_list.size();i++)
        {
            for(int j=0;j<recipe_list.get(i).ingredients.size();j++){
                if(!all_ingredients.contains(recipe_list.get(i).ingredients.get(j))) {
                    all_ingredients.add(recipe_list.get(i).ingredients.get(j));
                }
            }
        }
    }

    private void addToAllIngredients(Recipe r)
    {
        for(int i=0;i<r.ingredients.size();i++){
            if(!all_ingredients.contains(r.ingredients.get(i))) {
                all_ingredients.add(r.ingredients.get(i));
            }
        }
    }


}