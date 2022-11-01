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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShowRecipe extends AppCompatActivity {

    List<Recipe> recipe_list = new ArrayList<Recipe>();
    RecipeSelecter selecter=new RecipeSelecter();
    List<String> excluded_recipes=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = new Intent(ShowRecipe.this, MainActivity.class);
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
        GridView excludedIngredientsView=(GridView) findViewById(R.id.excludeingredients);
        List<String> excluded_ingedient_list=new ArrayList<String>();
        ArrayAdapter<String> exclude_arr;
        exclude_arr=new ArrayAdapter<String>(this, R.layout.excluded_ingredients_layout, excluded_ingedient_list);
        excludedIngredientsView.setAdapter(exclude_arr);

        excludedIngredientsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                excluded_ingedient_list.remove(position);
                exclude_arr.notifyDataSetChanged();
            }
        });

        recipe_list = (List<Recipe>) in.getSerializableExtra("RecipeList");
        Recipe current_recipe=selecter.selectRecipe(recipe_list,excluded_recipes,excluded_ingedient_list);
        excluded_recipes.add(current_recipe.name);
        TextView tv = (TextView) findViewById(R.id.recipename);
        tv.setText("\n"+current_recipe.name+"\n");
        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = current_recipe.link;
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }
        });


        ListView l = findViewById(R.id.recipeingredientlist);
        List<String> ingedient_list= new ArrayList<String>();
        ingedient_list.addAll(current_recipe.ingredients);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ingedient_list);
        l.setAdapter(arr);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if(!excluded_ingedient_list.contains(ingedient_list.get(position))) {
                    excluded_ingedient_list.add(ingedient_list.get(position));
                    exclude_arr.notifyDataSetChanged();
                }
            }
        });


        Button btn =findViewById(R.id.randombutton_showrecipe);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Recipe selectedRecipe=selecter.selectRecipe(recipe_list,excluded_recipes,excluded_ingedient_list);

                if(!selectedRecipe.valid){
                    excluded_recipes.clear();
                    tv.setAllCaps(true);
                    tv.setClickable(false);
                    tv.setText("\nYou're being a picky bitch\n");
                    ingedient_list.clear();
                    arr.notifyDataSetChanged();
                }
                else {
                    excluded_recipes.add(selectedRecipe.name);

                    ingedient_list.clear();
                    for(int i=0;i<selectedRecipe.ingredients.size();i++){
                        ingedient_list.add(selectedRecipe.ingredients.get(i));
                    }
                    arr.notifyDataSetChanged();
                    tv.setAllCaps(true);
                    tv.setText("\n" + selectedRecipe.name+"\n");
                    tv.setClickable(true);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = selectedRecipe.link;
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

        Button recipebtn=findViewById(R.id.addbutton_showrecipe);
        recipebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mStartForResult.launch(new Intent(MainActivity.this, AddRecipe.class));
                Intent intent = new Intent(ShowRecipe.this, AddRecipe.class);
                intent.putExtra("RecipeList", (Serializable) recipe_list);
                intent.putExtra("AllIngredients", (Serializable) all_ingredients);
                activityResultLaunch.launch(intent);
            }
        });

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

    @Override
    public void onBackPressed() {
        setResult(101);
        super.onBackPressed();
    }
}