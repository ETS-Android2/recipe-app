package com.example.recipe;

public class Recipes {

    private String RecipeName;
    private String RecipeIngredients;
    private String RecipeMethodTitle;
    private String Recipe;
    private int Thumbnail;
    private  Boolean IsFavourite;


    public Recipes(String name, String recipeIngredients, String recipeMethodTitle,String recipe, int thumbnail, Boolean isFavourite){

        RecipeName = name;
        RecipeIngredients = recipeIngredients;
        RecipeMethodTitle = recipeMethodTitle;
        Recipe = recipe;
        Thumbnail = thumbnail;
        IsFavourite = isFavourite;

    }


    public  String getRecipeName(){

        return RecipeName;
    }
    public String getRecipeIngredients(){
        return RecipeIngredients;
    }

    public String getRecipeMethodTitle(){
        return RecipeMethodTitle;
    }

    public String getRecipe(){
        return Recipe;
    }

    public int getThumbnail(){
        return Thumbnail;
    }

    public boolean getIsFavourite(){
        return IsFavourite;
    }

    public void setIsFavourite(Boolean value) {
        this.IsFavourite = value;
    }
}
