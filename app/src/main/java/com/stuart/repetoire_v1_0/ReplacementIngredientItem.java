package com.stuart.repetoire_v1_0;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReplacementIngredientItem implements Parcelable, Serializable {
    String name;
    List<String> replacementIngredients;

    String replacementIngredientsString;

    public ReplacementIngredientItem(String Name, List<String> ReplacementIngredients)
    {
        this.name=Name;
        this.replacementIngredients=ReplacementIngredients;
        convertIngredientsToString();
    }

    private void convertIngredientsToString(){
        replacementIngredientsString ="";
        for(int i=0; i<replacementIngredients.size();i++){
            replacementIngredientsString = replacementIngredientsString +replacementIngredients.get(i)+"\t";
        }
    }

    private void convertStringToIngredients(){
        replacementIngredients=new ArrayList<String>();
        String[] ingredientParts= replacementIngredientsString.split("\t");
        for(int i=0;i<ingredientParts.length;i++){
            replacementIngredients.add(ingredientParts[i]);
        }
    }

    public void addReplacementIngredient(String ingredient){
        replacementIngredientsString ="";
        replacementIngredients.add(0,ingredient);
        convertIngredientsToString();
    }

    public void removeReplacementIngredients(String ingredient){
        replacementIngredientsString ="";
        if(replacementIngredients.contains(ingredient)){
            replacementIngredients.remove(ingredient);
        }
        convertIngredientsToString();
    }

    public void removeReplacementIngredients(int i){
        replacementIngredientsString ="";
        replacementIngredients.remove(i);

        convertIngredientsToString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Log.d("Here1\n\n\n\n\n\n\n", name+" "+replacementIngredientsString);
        parcel.writeStringArray(new String[] {this.name, this.replacementIngredientsString});
    }

    public static final Parcelable.Creator<ReplacementIngredientItem> CREATOR
            = new Parcelable.Creator<ReplacementIngredientItem>() {
        public ReplacementIngredientItem createFromParcel(Parcel in) {
            return new ReplacementIngredientItem(in);
        }

        public ReplacementIngredientItem[] newArray(int size) {
            return new ReplacementIngredientItem[size];
        }
    };

    private ReplacementIngredientItem(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);
        this.name=data[0];
        this.replacementIngredientsString =data[1];
        Log.d("Here\n\n\n\n\n\n\n", name+" "+replacementIngredientsString);
        convertStringToIngredients();
    }
}
