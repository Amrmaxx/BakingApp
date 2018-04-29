package com.app.android.bakingapp.utilities;

import com.app.android.bakingapp.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    // Method to parse recipes list
    public static List<Recipe> parseRecipes(String jsonRecipesArray) {

        List<Recipe> Recipes = new ArrayList<>();
        try {
            JSONArray baseJsonResponse = new JSONArray(jsonRecipesArray);
            for (int i = 0; i < baseJsonResponse.length(); i++) {
                JSONObject jsonRecipe = baseJsonResponse.optJSONObject(i);
                String id = jsonRecipe.optString("id");
                String name = jsonRecipe.optString("name");
                String ingredients = jsonRecipe.optString("ingredients");
                ingredients = parseIngredients(ingredients);
                String steps = jsonRecipe.optString("steps");
                String servings = jsonRecipe.optString("servings");
                String image = jsonRecipe.optString("image");
                Recipe recipe = new Recipe(id, name, ingredients, steps, servings, image);
                Recipes.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Recipes;
    }

    // Method to parse ingredients
    // Since ingredients are repeated String in form of :  3 CUP of milk
    // it will be concatenated in a single string.
    private static String parseIngredients(String jsonIngredientsArray) {

        String ingredients="";
        try {
            JSONArray baseJsonResponse = new JSONArray(jsonIngredientsArray);
            for (int i = 0; i < baseJsonResponse.length(); i++) {
                JSONObject jsonRecipe = baseJsonResponse.optJSONObject(i);
                String quantity = jsonRecipe.optString("quantity");
                String measure = jsonRecipe.optString("measure");
                String ingredientDescription = jsonRecipe.optString("ingredient");

                ingredients += quantity +
                        " " + measure +
                        " of" +
                        " " + ingredientDescription
                        + ".\n";
            }
            ingredients = ingredients.substring(0,ingredients.length()-1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    // Method to parse steps list
    public static List<Recipe.Step> parseSteps(String jsonStepsArray) {

        List<Recipe.Step> Steps = new ArrayList<>();
        try {
            JSONArray baseJsonResponse = new JSONArray(jsonStepsArray);
            for (int i = 0; i < baseJsonResponse.length(); i++) {
                JSONObject jsonStep = baseJsonResponse.optJSONObject(i);
                int id = jsonStep.optInt("id");
                String shortDescription = jsonStep.optString("shortDescription");
                String description = jsonStep.optString("description");
                String videoURL = jsonStep.optString("videoURL");
                String thumbnailURL = jsonStep.optString("thumbnailURL");
                Recipe.Step step = new Recipe.Step(id, shortDescription, description, videoURL, thumbnailURL);
                Steps.add(step);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Steps;
    }
}