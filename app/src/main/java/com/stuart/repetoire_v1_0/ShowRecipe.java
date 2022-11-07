package com.stuart.repetoire_v1_0;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShowRecipe extends AppCompatActivity {
    Intent intent;

    List<Recipe> recipe_list = new ArrayList<Recipe>();
    List<Recipe> recipes_in_menu = new ArrayList<Recipe>();
    RecipeSelecter selecter=new RecipeSelecter();
    List<String> excluded_recipes=new ArrayList<String>();
    List<String> excluded_ingedient_list= new ArrayList<String>();
    Recipe currentDisplayedRecipe;
    List<String> ingedientDisplaylist=new ArrayList<String>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);
        intent = new Intent(ShowRecipe.this, MainActivity.class);
        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result.getResultCode()==101){

                        }
                        else {
                            if (result.getResultCode() == 123) {
                                Recipe new_recipe = result.getData().getParcelableExtra("Recipe");
                                intent.putExtra("Recipe", (Parcelable) new_recipe);
                                setResult(123, intent);
                            } else if (result.getResultCode() == 321) {
                                setResult(321, intent);
                            } else if (result.getResultCode() == 1) {
                                if (result.getResultCode() == 1) {
                                    List<Recipe> recipe_list = (List<Recipe>) result.getData().getSerializableExtra("NewRecipeList");
                                    intent.putExtra("NewRecipeList", (Serializable) recipe_list);
                                    setResult(1, intent);
                                }

                            } else {
                                setResult(result.getResultCode(), intent);
                            }
                            finish();
                        }
                    }
                });

        Intent in = getIntent();
        List<String> all_ingredients= (List<String>) in.getSerializableExtra("AllIngredients");
        recipe_list = (List<Recipe>) in.getSerializableExtra("RecipeList");
        recipes_in_menu = (List<Recipe>) in.getSerializableExtra("MenuRecipes");

        GridView excludedIngredientsView=(GridView) findViewById(R.id.excludeingredients);
        excluded_ingedient_list=new ArrayList<String>();
        ArrayAdapter<String> exclude_arr;
        exclude_arr=new ArrayAdapter<String>(this, R.layout.excluded_ingredients_layout, excluded_ingedient_list);
        excludedIngredientsView.setAdapter(exclude_arr);

        excludedIngredientsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                excluded_ingedient_list.remove(position);
                exclude_arr.notifyDataSetChanged();
            }
        });

        TextView tv = (TextView) findViewById(R.id.recipename);
        ListView l = findViewById(R.id.recipeingredientlist);
        Button btn =findViewById(R.id.randombutton_showrecipe);
        Button addToMenuBtn=findViewById(R.id.addtomenu_showrecipe);
        Button recipebtn=findViewById(R.id.back_showrecipe);
        //From Here
        updateCurrentRecipe();
        tv.setText("\n"+currentDisplayedRecipe.name+"\n"); //Leave
        tv.setClickable(true); //Leave
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = currentDisplayedRecipe.link;
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }
        });

        ArrayAdapter<String> arr; //Leave
        arr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ingedientDisplaylist); //Leave (update)
        l.setAdapter(arr);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if(!excluded_ingedient_list.contains(ingedientDisplaylist.get(position))) {
                    excluded_ingedient_list.add(ingedientDisplaylist.get(position));
                    exclude_arr.notifyDataSetChanged();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCurrentRecipe();

                if(!currentDisplayedRecipe.valid){
                    tv.setAllCaps(true);
                    tv.setClickable(false);
                    tv.setText("\nYou're being a picky bitch\n");
                    arr.notifyDataSetChanged();
                }
                else {
                    arr.notifyDataSetChanged();
                    tv.setAllCaps(true);
                    tv.setText("\n" + currentDisplayedRecipe.name+"\n");
                    tv.setClickable(true);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = currentDisplayedRecipe.link;
                            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                url = "http://" + url;
                            }
                            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(urlIntent);
                        }
                    });
                }
            }
        });

        addToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkIfRecipeInRecipeList(recipes_in_menu ,currentDisplayedRecipe)){
                    if(currentDisplayedRecipe.valid) {
                        recipes_in_menu.add(currentDisplayedRecipe);
                        recipebtn.setText("Save Menu\nand Back");
                    }
                } else {
                    Toast.makeText(ShowRecipe.this, "Recipe already on Your Menu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recipebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("UpdatedMenu",(Serializable) recipes_in_menu);
                setResult(5,intent);
                finish();
            }
        });

    }

    public void updateCurrentRecipe(){
        currentDisplayedRecipe=selecter.selectRecipe(recipe_list,excluded_recipes,excluded_ingedient_list);
        ingedientDisplaylist.clear();
        if(!currentDisplayedRecipe.valid){
            excluded_recipes.clear();
        }
        else{
            ingedientDisplaylist.addAll(currentDisplayedRecipe.ingredients);
            excluded_recipes.add(currentDisplayedRecipe.name);
        }
    }

    @Override
    public void onBackPressed() {
        intent.putExtra("UpdatedMenu",(Serializable) recipes_in_menu);
        setResult(5,intent);
        super.onBackPressed();
    }

    private boolean checkIfRecipeInRecipeList(List<Recipe> list, Recipe r)
    {
        for (Recipe r1: list) {
            if(checkRecipeNamesEqual(r1, r)){
                return true;
            }
        }
        return false;
    }

    private boolean checkRecipeNamesEqual(Recipe r1, Recipe r2)
    {
        if(r1.name.equals(r2.name)){
            return true;
        }
        else{
            return false;
        }
    }
}