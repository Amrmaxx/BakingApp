package com.app.android.bakingapp.model;

public class Recipe {

    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_INGREDIENTS_STRING = "recipe_ingredients_string";
    public static final String RECIPE_STEPS = "recipe_steps_string";
    public static final String RECIPE_SERVINGS = "recipe_servings";

    private String id;
    private String name;
    private String ingredientsString;
    private String stepsString;
    private String servings;
    private String image;


    // Empty Constructor
    public Recipe() {
    }

    // Constructor
    public Recipe(String id, String name, String ingredientsString, String stepsString, String servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredientsString = ingredientsString;
        this.stepsString = stepsString;
        this.servings = servings;
        this.image = image;
    }

    // Getter Methods
    public String getRecipeID() {
        return id;
    }

    public String getRecipeName() {
        return name;
    }

    public String getRecipeIngredientsString() {
        return ingredientsString;
    }

    public String getRecipeStepsString() {
        return stepsString;
    }

    public String getRecipeServings() {
        return servings;
    }

    public String getRecipeImage() {
        return image;
    }

    // Inner Class for Step
    public static class Step {

        public static final String STEP_ID = "step_id";
        public static final String STEP_SHORT_DESCRIPTION = "step_short_description";
        public static final String STEP_DESCRIPTION = "step_descripton";
        public static final String STEP_VIDEO_URL = "step_video_url";
        public static final String STEP_VIDEO_THUMBNAIL = "step_video_thumbnail";

        private int id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        // Empty Constructor
        public Step() {
        }

        // Constructor
        public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoURL = videoURL;
            this.thumbnailURL = thumbnailURL;
        }

        // Getter Methods
        public int getStepID() {
            return id;
        }

        public String getStepShortDescription() {
            return shortDescription;
        }

        public String getStepsDescription() {
            return description;
        }

        public String getStepVideoURL() {
            return videoURL;
        }

        public String getStepThumbnailURL() {
            return thumbnailURL;
        }

    }
}