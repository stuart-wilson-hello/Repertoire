package com.stuart.repetoire_v1_0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ShoppingListViewAdapter extends ArrayAdapter<ShoppingListElement> {

    Context c1;
    ArrayList<ShoppingListElement> dataSet;

    public ShoppingListViewAdapter(ArrayList<ShoppingListElement> data, Context context) {
        super(context, R.layout.support_simple_spinner_dropdown_item, data);
        this.dataSet = data;
        c1=context;

    }

    public View getView(int position, android.view.View v, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) c1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = li.inflate(R.layout.shopping_list_components_layout, null);

        TextView t1=(TextView)v.findViewById(R.id.ingredientName_shoppinglist);
        t1.setText(dataSet.get(position).Name);

        TextView t2=(TextView)v.findViewById(R.id.ingredientqty_shoppinglist);
        t2.setText(dataSet.get(position).Quantity.toString());

        return v;
    }
}
