package com.stuart.repetoire_v1_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReplacementIngredientDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement_ingredient_display);

        Intent intent = new Intent(ReplacementIngredientDisplay.this, MainActivity.class);
        Intent in = getIntent();

        List<ReplacementIngredientItem> ingredient_replacements = (List<ReplacementIngredientItem>) in.getSerializableExtra("ReplacementIngredient");
        ListView l=findViewById(R.id.replacementIngredients_list);

        List<String> all_ingredients = (List<String>) in.getSerializableExtra("AllIngredients");

        ReplacementIngredientsListViewAdapter adapter=new ReplacementIngredientsListViewAdapter(this, ingredient_replacements, all_ingredients);
        l.setAdapter(adapter);

        Button addButton = findViewById(R.id.mainIngredientBtnAdd);
        AutoCompleteTextView editText= (AutoCompleteTextView) findViewById(R.id.mainIngredientEdtItemName);
        ArrayAdapter ingredients_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all_ingredients);
        editText.setAdapter(ingredients_adapter);
        editText.setThreshold(1);
        editText.setAdapter(ingredients_adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingredient=editText.getText().toString().toUpperCase().trim();
                if(!ingredient.isEmpty()) {
                    ingredient_replacements.add(new ReplacementIngredientItem(editText.getText().toString().toUpperCase().trim(), new ArrayList<String>()));
                    adapter.notifyDataSetChanged();
                }
                editText.setText("");
            }
        });

        Button doneBtn=findViewById(R.id.donereplacementIngredients);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("UpdatedReplacementIngredients",(Serializable) adapter.replacements);
                setResult(505,intent);
                finish();
            }
        });
    }
}