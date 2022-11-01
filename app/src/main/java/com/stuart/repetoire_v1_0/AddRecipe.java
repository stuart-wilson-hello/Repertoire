package com.stuart.repetoire_v1_0;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        List<Recipe> recipe_list = (List<Recipe>) i.getSerializableExtra("RecipeList");
        List<String> all_ingredients= (List<String>) i.getSerializableExtra("AllIngredients");

        setContentView(R.layout.activity_add_recipe);

        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = new Intent(AddRecipe.this, MainActivity.class);
                        if(result.getResultCode() == 1) {
                            List<Recipe> recipe_list = (List<Recipe>) result.getData().getSerializableExtra("NewRecipeList");
                            intent.putExtra("NewRecipeList", (Serializable) recipe_list);
                            setResult(1, intent);
                        }
                        else {
                            setResult(result.getResultCode(), intent);
                        }
                        finish();
                    }
                });
        Button ingredientsAddb=findViewById(R.id.ingredientBtnAdd);
        ListView ingredientsListView=findViewById(R.id.ingredientList);
        AutoCompleteTextView ingredientsTxt= (AutoCompleteTextView) findViewById(R.id.ingredientEdtItemName);
        ArrayAdapter ingredients_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all_ingredients);
        ingredientsTxt.setAdapter(ingredients_adapter);
        ingredientsTxt.setThreshold(1);
        ingredientsTxt.setAdapter(ingredients_adapter);

        ArrayList<String> ingredientsList=new ArrayList<String>();
        ArrayAdapter<String> adapt=new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, ingredientsList);
        ingredientsListView.setAdapter(adapt);

        Button fab = findViewById(R.id.savebutton);
        EditText name_widget=findViewById(R.id.recipename);
        EditText link_widget=findViewById(R.id.recipetext);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddRecipe.this, MainActivity.class);
                Recipe recipe=new Recipe(name_widget.getText().toString().toUpperCase(), link_widget.getText().toString(), ingredientsList);
                intent.putExtra("Recipe", (Parcelable) recipe);
                if(recipe.name.equals("") || recipe.link.equals(""))
                {
                    setResult(321,intent);
                }
                else {
                    setResult(123, intent);
                }
                finish();
            }
        });

        Button editb=findViewById(R.id.editbutton);
        editb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddRecipe.this, RecipesList.class);
                intent.putExtra("RecipeList", (Serializable) recipe_list);
                activityResultLaunch.launch(intent);
            }
        });

        ingredientsAddb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingredient = ingredientsTxt.getText().toString().toUpperCase().trim();
                if (!ingredient.isEmpty() && !ingredientsList.contains(ingredient)) {
                    ingredientsList.add(0,ingredient);
                    adapt.notifyDataSetChanged();
                }
                ingredientsTxt.setText("");
            }
        });

        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ingredientsList.remove(position);
                adapt.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("BACKBUTTON", "Add Recipe back Button Pressed");
        setResult(101);
        super.onBackPressed();
    }
}