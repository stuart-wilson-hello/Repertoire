package com.stuart.repetoire_v1_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShoppingList extends AppCompatActivity {

    HashMap<String, Integer> shoppingListDictionary = new HashMap<String, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ListView l=findViewById(R.id.shoppingList);
        Intent in = getIntent();
        List<Recipe> recipes_in_menu = (List<Recipe>) in.getSerializableExtra("MenuRecipes");
        for(int i=0;i<recipes_in_menu.size();i++){
            for(int j=0;j<recipes_in_menu.get(i).ingredients.size();j++){
                if(shoppingListDictionary.containsKey(recipes_in_menu.get(i).ingredients.get(j))){
                    int value= shoppingListDictionary.get(recipes_in_menu.get(i).ingredients.get(j));
                    shoppingListDictionary.put(recipes_in_menu.get(i).ingredients.get(j), value+1);
                }
                else{
                    shoppingListDictionary.put(recipes_in_menu.get(i).ingredients.get(j), 1);
                }
            }
        }
        ArrayList<ShoppingListElement> shoppingList=new ArrayList<ShoppingListElement>();
        for (Map.Entry<String, Integer> entry : shoppingListDictionary.entrySet()) {
            shoppingList.add(new ShoppingListElement(entry.getKey(),entry.getValue()));
        }

        ShoppingListViewAdapter adapt=new ShoppingListViewAdapter(shoppingList, this);
        l.setAdapter(adapt);

        Button doneBtn=findViewById(R.id.donebuttonshoppinglist);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(101);
                finish();
            }
        });
    }
}