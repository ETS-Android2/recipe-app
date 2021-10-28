package com.example.recipe;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {

    private Context mContext;
    private List<Recipes> recipes;
    private Boolean showingRecipes;
    List<Recipes> favouriteRecipes;

    public RecyclerViewAdapter(Context mContext, List<Recipes> mData, Boolean showingRecipes) {
        this.mContext = mContext;
        this.recipes = mData;
        this.showingRecipes = showingRecipes;

        if (!showingRecipes) {
            TinyDB tinydb = new TinyDB(mContext);
            favouriteRecipes = tinydb.getListRecipe("favouriteRecipes");
            this.recipes = favouriteRecipes;
        }
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.cardview_recipe, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder,
                                 @SuppressLint("RecyclerView") final int i) {

        TinyDB tinydb = new TinyDB(mContext);
        List<Recipes> favouriteRecipes = tinydb.getListRecipe("favouriteRecipes");

        Recipes recipe = recipes.get(i);

        myHolder.recipeTitle.setText(recipes.get(i).getRecipeName());
        myHolder.img_recipe_thumbnail.setImageResource(recipes.get(i).getThumbnail());

        if (favouriteRecipes.stream().anyMatch(o -> o.getRecipeName().equals(recipe.getRecipeName())) == true) {
            myHolder.favouriteButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        } else {
            myHolder.favouriteButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
        }

        myHolder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, RecipeActivity.class);

            intent.putExtra("RecipeName", recipes.get(i).getRecipeName());
            intent.putExtra("RecipeIngredients", recipes.get(i).getRecipeIngredients());
            intent.putExtra("RecipeMethodTitle", recipes.get(i).getRecipeMethodTitle());
            intent.putExtra("Recipe", recipes.get(i).getRecipe());
            intent.putExtra("Thumbnail", recipes.get(i).getThumbnail());

            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView recipeTitle;
        CardView cardView;
        ImageView img_recipe_thumbnail;
        ImageButton favouriteButton;
        ImageButton shareButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            TinyDB tinydb = new TinyDB(mContext);
            ArrayList<Recipes> favouriteRecipes = tinydb.getListRecipe("favouriteRecipes");

            recipeTitle = itemView.findViewById(R.id.recipe_text);
            img_recipe_thumbnail = itemView.findViewById(R.id.recipe_img_id);
            cardView = itemView.findViewById(R.id.cardview_id);
            shareButton = itemView.findViewById(R.id.shareButton);
            favouriteButton = itemView.findViewById(R.id.favouriteButton);

            shareButton.setOnClickListener(v -> {
                int i = getAdapterPosition();
                Recipes recipe = recipes.get(i);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Recipe: " + recipe.getRecipeName() +
                        "\n\n" + "Ingredients:\n" + recipe.getRecipeIngredients() + "\n\n" +
                        "Instructions:\n" + recipe.getRecipe());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                mContext.startActivity(shareIntent);
            });

            favouriteButton.setOnClickListener(view -> {
                int i = getAdapterPosition();
                Recipes recipe = recipes.get(i);

                if (recipe.getIsFavourite() == false) {
                    recipe.setIsFavourite(true);
                    favouriteButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    favouriteRecipes.add(recipe);
                    tinydb.putListObject("favouriteRecipes", favouriteRecipes);
                } else {
                    recipe.setIsFavourite(false);
                    favouriteButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                    removeFromFavouritesListInDB(recipe);
                    if (showingRecipes == false) {
                        //Remove from the recyclerList
                        recipes.removeIf(t -> t.getRecipeName() == recipe.getRecipeName());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), recipes.size());
                    }
                }
            });
        }
    }

    void removeFromFavouritesListInDB(Recipes recipe) {
        TinyDB tinydb = new TinyDB(mContext);
        ArrayList<Recipes> favouriteRecipes = tinydb.getListRecipe("favouriteRecipes");
        favouriteRecipes.removeIf(t -> t.getRecipeName().equals(recipe.getRecipeName()));
        tinydb.remove("favouriteRecipes");
        tinydb.putListObject("favouriteRecipes", favouriteRecipes);
    }
}
