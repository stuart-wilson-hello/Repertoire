package com.stuart.repetoire_v1_0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeSelecter {

    public RecipeSelecter(){

    }

    public Recipe selectRecipe(List<Recipe> allRecipes, List<String> excludedRecipeNames, List<String> excludeIngredients)
    {
        Recipe selectedRecipe=new Recipe();
        Random rd =new Random();
        if(allRecipes.size() < 5){
            int temp = rd.nextInt(allRecipes.size());
            selectedRecipe=allRecipes.get(temp);
            selectedRecipe.valid=true;
            return selectedRecipe;
        }



        int startingPoint=rd.nextInt(allRecipes.size());
        int currentPoint=startingPoint;
        boolean foundRecipe=false;
        while(!foundRecipe) {
            if (!excludedRecipeNames.contains(allRecipes.get(currentPoint).name)) {
                //Not excluded based on name
                if (checkIngredients(allRecipes.get(currentPoint), excludeIngredients)) {
                    selectedRecipe=allRecipes.get(currentPoint);
                    foundRecipe = true;
                    break;
                }
            }

            currentPoint++;
            if (currentPoint == allRecipes.size()) {
                //Looped around
                currentPoint = 0;
            }
            if (currentPoint == startingPoint) {
                //No Recipes match!!
                selectedRecipe.valid = false;
                foundRecipe=true;
            }
        }

        return selectedRecipe;
    }

    private boolean checkIngredients(Recipe currentRecipe, List<String> excludeIngredients)
    {
        for(int i=0; i<currentRecipe.ingredients.size();i++)
        {
            if(excludeIngredients.contains(currentRecipe.ingredients.get(i))){
                return false;
            }
        }
        return true;
    }

}
