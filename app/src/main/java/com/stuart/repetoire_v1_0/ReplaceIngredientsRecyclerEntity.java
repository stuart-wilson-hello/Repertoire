package com.stuart.repetoire_v1_0;

import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import java.util.List;

public class ReplaceIngredientsRecyclerEntity {
    private List<String> replacementIngredients;

    public ReplaceIngredientsRecyclerEntity(){

    }

    public ReplaceIngredientsRecyclerEntity(List<String> ReplacementIngredients) {
        this.replacementIngredients=ReplacementIngredients;
    }

    public void setReplacementIngredients(List<String> ReplacementIngredients){
        this.replacementIngredients=ReplacementIngredients;
    }

    public List<String> getReplacementIngredientsetReplacementIngredients(){
        return replacementIngredients;
    }

}
