package com.stuart.repetoire_v1_0;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ReplacementIngredientsListViewAdapter extends ArrayAdapter<ReplacementIngredientItem> {

    List<ReplacementIngredientItem> replacements;
    Context c1;
    List<String> all_ingredients;

    public ReplacementIngredientsListViewAdapter(@NonNull Context context, List<ReplacementIngredientItem> Ingredients, List<String> all_ingredients) {
        super(context, R.layout.support_simple_spinner_dropdown_item, Ingredients);
        this.c1=context;
        replacements = Ingredients;
        this.all_ingredients=all_ingredients;
    }

    public View getView(int position, View v, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) c1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v=li.inflate(R.layout.replacement_ingredient_list_component,null);

        TextView tv=(TextView)v.findViewById(R.id.ingredientname);
        tv.setTag(position);
        tv.setText(replacements.get(position).name);
        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb=new AlertDialog.Builder(c1);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + replacements.get(position).name);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        replacements.remove(position);
                        notifyDataSetChanged();
                    }});
                adb.show();
            }
        });

        GridView lv=(GridView) v.findViewById(R.id.replacementIngredients_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(c1, R.layout.support_simple_spinner_dropdown_item, replacements.get(position).replacementIngredients);
        lv.setAdapter(arr);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int unit_position, long id) {
                replacements.get(position).removeReplacementIngredients(unit_position);
                arr.notifyDataSetChanged();
            }
        });

        AutoCompleteTextView ed =(AutoCompleteTextView)v.findViewById(R.id.replacementIngredientsEdtItemName);
        ArrayAdapter autofilladapter = new ArrayAdapter(c1, android.R.layout.simple_list_item_1, all_ingredients);
        ed.setAdapter(autofilladapter);
        ed.setThreshold(1);
        ed.setAdapter(autofilladapter);

        ed.setTag(position);
        Button ingredientButton=v.findViewById(R.id.replacementIngredientsBtnAdd);
        ingredientButton.setTag(position);
        ingredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingredient = ed.getText().toString().toUpperCase().trim();
                int positionOnList=(int)view.getTag();
                if (!ingredient.isEmpty() && !replacements.get(positionOnList).replacementIngredients.contains(ingredient)) {
                    replacements.get(positionOnList).addReplacementIngredient(ingredient);
                    notifyDataSetChanged();
                }
                ed.setText("");
                arr.notifyDataSetChanged();
            }
        });

        return v;

    }

    public int getCount() {
        return replacements.size();
    }
}
