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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.List;

public class AddRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        List<Recipe> recipe_list = (List<Recipe>) i.getSerializableExtra("RecipeList");

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
                            setResult(2, intent);
                        }
                        finish();
                    }
                });
        Button fab = findViewById(R.id.savebutton);
        EditText name_widget=findViewById(R.id.recipename);
        EditText link_widget=findViewById(R.id.recipetext);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddRecipe.this, MainActivity.class);
                Recipe recipe=new Recipe(name_widget.getText().toString(), link_widget.getText().toString());
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
    }
}