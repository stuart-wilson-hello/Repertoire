package com.stuart.repetoire_v1_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeSearch extends AppCompatActivity {

    List<Recipe> recipe_list = new ArrayList<Recipe>();
    List<Recipe> valid_recipes = new ArrayList<Recipe>();
    //List<Recipe> recipes_in_menu = new ArrayList<Recipe>();
    List<ReplacementIngredientItem> replacementIngredientsMap;
    List<String> included_ingedient_list= new ArrayList<String>();
    List<String> displayed_ingredients;
    List<String> valid_ingredients=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);

        Intent in = getIntent();
        List<String> all_ingredients= (List<String>) in.getSerializableExtra("AllIngredients");
        Collections.sort(all_ingredients);
        recipe_list = (List<Recipe>) in.getSerializableExtra("RecipeList");
        findValidRecipes();
        findValidIngredients();
        displayed_ingredients=new ArrayList<>(valid_ingredients);

        //recipes_in_menu = (List<Recipe>) in.getSerializableExtra("MenuRecipes");
        replacementIngredientsMap =(List<ReplacementIngredientItem>) in.getSerializableExtra("ReplacementIngredients");


        //All Recipe List
        RecyclerView recipeListView= (RecyclerView)findViewById(R.id.AllRecipeList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recipeListView.setLayoutManager(manager);
        recipeListView.setHasFixedSize(true);
        recipeListView.addItemDecoration(new DividerItemDecoration(recipeListView.getContext(), DividerItemDecoration.VERTICAL));
        RecipeListAdapter recipe_adapt=new RecipeListAdapter(valid_recipes, new RecipeListAdapter.OnRecipeClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                String url = recipe.link;
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(urlIntent);
            }
        });
        recipeListView.setAdapter(recipe_adapt);
        EditText searchIngredientsView=findViewById(R.id.ingredientSearch);
        //Included Ingredients Grid
        GridView includedIngredientsView=(GridView) findViewById(R.id.includeingredients);
        included_ingedient_list=new ArrayList<String>();
        ArrayAdapter<String> include_arr;
        ListView ingredientsListView=findViewById(R.id.AllIngredientList);
        findValidIngredients();
        ArrayAdapter<String> adapt=new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, displayed_ingredients);
        ingredientsListView.setAdapter(adapt);

        include_arr=new ArrayAdapter<String>(this, R.layout.included_ingredients_layout, included_ingedient_list);
        includedIngredientsView.setAdapter(include_arr);
        includedIngredientsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                included_ingedient_list.remove(position);
                include_arr.notifyDataSetChanged();
                searchIngredientsView.setText("");
                findValidRecipes();
                findValidIngredients();
                displayed_ingredients.clear();
                displayed_ingredients.addAll(valid_ingredients);
                searchIngredientsView.setText("");
                adapt.notifyDataSetChanged();
                recipe_adapt.setData(valid_recipes);
            }
        });


        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(!included_ingedient_list.contains(displayed_ingredients.get(position))) {
                    included_ingedient_list.add(displayed_ingredients.get(position));
                    include_arr.notifyDataSetChanged();
                    //all_ingredients.remove(displayed_ingredients.get(position));
                    findValidRecipes();
                    findValidIngredients();
                    displayed_ingredients.clear();
                    displayed_ingredients.addAll(valid_ingredients);
                    searchIngredientsView.setText("");
                    adapt.notifyDataSetChanged();
                    recipe_adapt.setData(valid_recipes);
                }
            }
        });

        //Done Button
        Button doneBtn=findViewById(R.id.back_recipesearch);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(101);
                finish();
            }
        });

        searchIngredientsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                String textString=text.toString();
                if(text.length()>=1)
                {
                    displayed_ingredients.clear();
                    for(int k=0;k<valid_ingredients.size();k++){
                        if(valid_ingredients.get(k).startsWith(textString)){
                            displayed_ingredients.add(valid_ingredients.get(k));
                        }
                    }
                }
                else{
                    displayed_ingredients.clear();
                    displayed_ingredients.addAll(valid_ingredients);
                }
                adapt.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void findValidRecipes()
    {
        valid_recipes.removeAll(recipe_list);
        Log.d("ValidRecipes", "1 ValidRecipes size "+valid_recipes.size());
        for(int i=0; i<recipe_list.size();i++){
            boolean add=true;
            Recipe recipe_to_check=recipe_list.get(i);
            for(int j=0; j<included_ingedient_list.size();j++){
                String ingredient_to_check=included_ingedient_list.get(j);
                if(!recipe_to_check.ingredients.contains(ingredient_to_check)){
                    add=false;
                    for(int k=0; k<replacementIngredientsMap.size(); k++)
                    {
                        ReplacementIngredientItem ingredient_replacement=replacementIngredientsMap.get(k);
                        if(ingredient_replacement.replacementIngredients.contains(ingredient_to_check)){
                            if(recipe_to_check.ingredients.contains(ingredient_replacement.name)) {
                                add = true; //We have a replacement for this ingredient
                            }
                        }
                    }
                }
            }
            if(add){
                valid_recipes.add(recipe_list.get(i));
            }
        }

        Log.d("ValidRecipes", "2 ValidRecipes size "+valid_recipes.size());
    }

    private void findValidIngredients()
    {
        valid_ingredients.clear();
        for(int i=0; i<valid_recipes.size(); i++)
        {
             for(int j=0; j<valid_recipes.get(i).ingredients.size(); j++){
                 String this_ingredient=valid_recipes.get(i).ingredients.get(j);
                 if(!valid_ingredients.contains(this_ingredient) && !included_ingedient_list.contains(this_ingredient)){
                     valid_ingredients.add(this_ingredient);
                 }
             }
        }
        Collections.sort(valid_ingredients);
        Log.d("ValidIngredients", " ValidIngredients size "+valid_ingredients.size());
    }
}