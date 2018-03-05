package com.android.example.bakingtime.data.local;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.android.example.bakingtime.data.model.Recipe;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeStoreTest {

    private RecipeStore recipeStore;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        recipeStore = RecipeStore.get(context);
    }

    @Test
    public void nullDirectory() {
        assertNotNull(recipeStore);
        assertNotNull(recipeStore.getRecipes());
        assertEquals(0, recipeStore.getRecipes().size());
    }

    @Test
    public void count() throws Exception {
        assertNotNull(recipeStore);
        assertNotNull(recipeStore.getRecipes());
        assertEquals(4, recipeStore.getRecipes().size());
    }

    @Test
    public void getNutellaPie() {
        Recipe recipe = recipeStore.getRecipe(1);
        assertNotNull(recipe);
        assertEquals("Nutella Pie", recipe.getName());
    }
}