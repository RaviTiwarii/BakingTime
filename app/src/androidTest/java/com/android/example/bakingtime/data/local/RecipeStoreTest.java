package com.android.example.bakingtime.data.local;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.android.example.bakingtime.data.model.Recipe;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeStoreTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void nullDirectory() {
        RecipeStore store = new RecipeStore(context, null);
        assertNotNull(store);
        assertNotNull(store.getRecipes());
        assertEquals(0, store.getRecipes().size());
    }

    @Test
    public void count() throws Exception {
        RecipeStore store = new RecipeStore(context, "baking.json");
        assertNotNull(store);
        assertNotNull(store.getRecipes());
        assertEquals(4, store.getRecipes().size());
    }

    @Test
    public void getNutellaPie() {
        RecipeStore store = new RecipeStore(context, "baking.json");
        Recipe recipe = store.getRecipe(1);
        assertNotNull(recipe);
        assertEquals("Nutella Pie", recipe.getName());
    }
}