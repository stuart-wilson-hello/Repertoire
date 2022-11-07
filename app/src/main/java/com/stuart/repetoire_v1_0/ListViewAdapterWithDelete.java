package com.stuart.repetoire_v1_0;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListViewAdapterWithDelete extends ArrayAdapter<String>
{
    Context c1;
    List<String> s1;
    List<Recipe> recipes;

    ListViewAdapterWithDelete(Context c, List<Recipe> Recipes, List<String> s)
    {
        super(c,R.layout.support_simple_spinner_dropdown_item,s);
        this.c1=c;
        this.s1=s;
        recipes=Recipes;
    }

    public View getView(int position, View v, ViewGroup parent)
    {
        LayoutInflater li=(LayoutInflater) c1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        v=li.inflate(R.layout.recipe_menu_component_layout,null);
        TextView tv=(TextView)v.findViewById(R.id.recipename_menucomponent);
        tv.setText(s1.get(position));
        tv.setClickable(true);
        tv.setTag(position);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = recipes.get(position).link;
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                c1.startActivity(urlIntent);
            }
        });

        Button bt = (Button) v.findViewById(R.id.deletebtn);
        bt.setTag(position); //important so we know which item to delete on button click

        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                int positionToRemove = (int)v.getTag(); //get the position of the view to delete stored in the tag
                s1.remove(positionToRemove);
                recipes.remove(positionToRemove);
                notifyDataSetChanged(); //remove the item
            }
        });

        return v;
    }


    public int getCount() {
        return s1.size();
    }
}
