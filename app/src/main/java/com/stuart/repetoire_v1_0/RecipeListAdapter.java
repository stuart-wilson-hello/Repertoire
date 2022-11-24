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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public interface OnRecipeClickListener {
        void onItemClick(Recipe recipe); //This is what will be returned when the click occurs
    }
    private OnRecipeClickListener listener;
    private List<Recipe> recipes;

    RecipeListAdapter(List<Recipe> Recipes, OnRecipeClickListener Listener)
    {
        recipes=Recipes;
        listener=Listener;
    }
    public void setData(List<Recipe> data) {
        recipes = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.simple_recipe_vertical_list, parent, false);
        RecipeViewHolder holder=new RecipeViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        RecipeViewHolder holder = (RecipeViewHolder) rawHolder;
        holder.setRecipe(recipes.get(position));
        holder.itemView.setTag(position);
        holder.bind(listener);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        public TextView text;
        public Recipe recipe;
        //public String recipe_name;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.recipe_name);
        }

        public void setRecipe(Recipe r)
        {
            recipe=r;
            text.setText(recipe.name);
        }
        public void bind(final OnRecipeClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(recipe);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
