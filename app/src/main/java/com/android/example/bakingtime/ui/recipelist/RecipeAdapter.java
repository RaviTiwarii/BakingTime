package com.android.example.bakingtime.ui.recipelist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.ui.OnListItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for the recipes recycler view.
 *
 * @author Ravi Tiwari
 * @version 1.0
 * @since 1.0
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    @NonNull
    private final List<Recipe> recipes;
    @NonNull
    private final OnListItemClickListener<Recipe> listener;

    public RecipeAdapter(@NonNull List<Recipe> recipes,
                         @NonNull OnListItemClickListener<Recipe> listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(@NonNull List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView recipeImageView;
        private final TextView recipeNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            recipeImageView = itemView.findViewById(R.id.recipe_image);
            recipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
        }

        void bind(@NonNull final Recipe recipe) {
            if (TextUtils.isEmpty(recipe.getImageUrl()))
                recipeImageView.setImageResource(R.drawable.ic_recipes);
            else
                Picasso.with(recipeImageView.getContext())
                        .load(recipe.getImageUrl())
                        .placeholder(R.drawable.ic_recipes)
                        .error(R.drawable.ic_recipes)
                        .into(recipeImageView);

            recipeNameTextView.setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {
            Recipe currentRecipe = recipes.get(getAdapterPosition());
            listener.onItemClick(currentRecipe);
        }
    }
}
