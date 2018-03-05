package com.android.example.bakingtime.ui.recipelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.model.Recipe;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    @NonNull
    private final Context context;
    @NonNull
    private final List<Recipe> recipes;

    public RecipeAdapter(@NonNull Context context, @NonNull List<Recipe> recipes) {
        super(context, R.layout.list_item_recipe, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_item_recipe, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recipe currentRecipe = recipes.get(position);
        holder.bind(currentRecipe);

        return convertView;
    }

    public void setRecipes(@NonNull final List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private final TextView recipeNameTextView;

        ViewHolder(@NonNull final View view) {
            recipeNameTextView = view.findViewById(R.id.tv_recipe_name);
        }

        void bind(@NonNull final Recipe recipe) {
            recipeNameTextView.setText(recipe.getName());
        }
    }
}
