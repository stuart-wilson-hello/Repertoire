package com.stuart.repetoire_v1_0;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable, Serializable {
    String name;
    String link;
    List<String> ingredients;
    boolean valid;

    String ingredientsString;

    public Recipe(){
        name="";
        link="";
        ingredients=new ArrayList<String>();
        convertIngredientsToString();
        valid=false;
    }
    public Recipe(String Name, String Link)
    {
        name=Name;
        link=Link;
        ingredients=new ArrayList<String>();
        convertIngredientsToString();
        valid=true;
    }

    public Recipe(String Name, String Link, List<String> Ingredients)
    {
        name=Name;
        link=Link;
        ingredients=Ingredients;
        convertIngredientsToString();
        valid=true;
    }

    private void convertIngredientsToString(){
        ingredientsString ="";
        for(int i=0; i<ingredients.size();i++){
            ingredientsString = ingredientsString +ingredients.get(i)+"\t";
        }
    }

    private void convertStringToIngredients(){
        ingredients=new ArrayList<String>();
        String[] ingredientParts= ingredientsString.split("\t");
        for(int i=0;i<ingredientParts.length;i++){
            ingredients.add(ingredientParts[i]);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.name, this.link, this.ingredientsString});
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        this.name=data[0];
        this.link=data[1];
        this.ingredientsString =data[2];
        this.valid=true;

        convertStringToIngredients();
    }
}
