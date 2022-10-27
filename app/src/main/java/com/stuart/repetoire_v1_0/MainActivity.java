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
    List<Integer> excludeList=new ArrayList<Integer>();

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
                        } else if(result.getResultCode() == 321) {
                            tv.setClickable(false);
                            tv.setAllCaps(false);
                            tv.setText("Cant add blank link or name");
                        } else if(result.getResultCode() == 1 || result.getResultCode() == 2) {
                            if(result.getResultCode() == 1){
                                recipe_list=(List<Recipe>) result.getData().getSerializableExtra("NewRecipeList");
                            }
                            else{
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
                    if(excludeList.size() == recipe_list.size()){
                        excludeList.removeAll(excludeList);
                        tv.setAllCaps(true);
                        tv.setClickable(false);
                        tv.setText("You're being a picky bitch");
                    }
                    else {
                        Collections.sort(excludeList);
                        int recipeNumber = selectRecipeNumber(excludeList);
                        excludeList.add(recipeNumber);
                        tv.setAllCaps(true);
                        tv.setText("Here we are: \n\n" + recipe_list.get(recipeNumber).name);
                        tv.setClickable(true);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = recipe_list.get(recipeNumber).link;
                                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                    url = "http://" + url;
                                }
                                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(urlIntent);
                            }
                        });
                    }
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
                    stored_recipe=new Recipe(line_parts[0], line_parts[1]);
                    recipe_list.add(stored_recipe);
                    line = reader.readLine();
                }
                fis.close();
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
            outputStreamWriter.write(recipe.name+"\t"+recipe.link+"\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }

    private void writeFullListToFile(List<Recipe> recipes) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(recipe_file, Context.MODE_PRIVATE));
            for(int i=0;i<recipes.size();i++) {
                outputStreamWriter.write(recipes.get(i).name + "\t" + recipes.get(i).link + "\n");
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }

    private int selectRecipeNumber(List<Integer> excludeList)
    {
        Random rd =new Random();
        if(recipe_list.size() < 5){
            int temp = rd.nextInt(recipe_list.size());
            return temp;
        }
        else{
            int temp = rd.nextInt(recipe_list.size()-excludeList.size());
            for(int i=0;i<excludeList.size();i++){
                if(excludeList.get(i)>temp){
                    break;
                }
                else{
                    temp++;
                }
            }
            return temp;
        }

    }


}