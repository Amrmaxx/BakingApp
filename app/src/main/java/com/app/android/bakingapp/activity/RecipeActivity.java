package com.app.android.bakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.android.bakingapp.R;
import com.app.android.bakingapp.model.Recipe;
import com.app.android.bakingapp.widget.BakingWidgetProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements com.app.android.bakingapp.activity.RecipeFragment.OnStepClickListener {

    private String mName;
    private String mIngredients;
    private String mStepsString;
    private String mServing;
    private boolean recipeIsFavorite;
    private SharedPreferences mPref;
    private static final String FAVORITE_KEY = "favorite";
    private boolean mTwoPane;

    @BindView(R.id.ingredients_title)
    TextView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);

        // Getting data from Intent
        Intent intent = getIntent();
        mName = intent.getStringExtra(Recipe.RECIPE_NAME);
        mIngredients = intent.getStringExtra(Recipe.RECIPE_INGREDIENTS_STRING);
        mStepsString = intent.getStringExtra(Recipe.RECIPE_STEPS);
        mServing = intent.getStringExtra(Recipe.RECIPE_SERVINGS);
        setTitle(mName);

        // Getting whether this recipe is favorite or no
        mPref = this.getSharedPreferences(getResources().getString(R.string.SHARED_PREF_KEY), MODE_PRIVATE);

        // Saving Pref Key
        if (savedInstanceState == null) {
            String prefRecipeName = mPref.getString(Recipe.RECIPE_NAME, "");
            if (mName.equals(prefRecipeName)) {
                recipeIsFavorite = true;
            }
        } else {
            recipeIsFavorite = savedInstanceState.getBoolean(FAVORITE_KEY);
        }
        // if Step fragment is in layout then it is wide screen (TwoPane)
        mTwoPane = findViewById(R.id.step_fragment) != null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAVORITE_KEY, recipeIsFavorite);
    }

    // Creating setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recipe_menu_options, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_favorites);
        if (recipeIsFavorite) {
            item.setIcon(R.drawable.ic_action_star);
        } else {
            item.setIcon(R.drawable.ic_action_star_border);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // checking which option was clicked
        int id = item.getItemId();

        //  clicking star icon will make recipe favorite or not
        if (id == R.id.action_favorites) {
            // Saving / Deleting from favorites
            if (recipeIsFavorite) {
                removeFromFavorites(item);
            } else {
                addToFavorites(item);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // This method is called when user clicks empty star to make Recipe favorite
    private void addToFavorites(MenuItem item) {
        // Setting new icon as recipe going to sharedPref
        item.setIcon(R.drawable.ic_action_star);
        recipeIsFavorite = true;
        mPref.edit().putString(Recipe.RECIPE_NAME, mName).apply();
        mPref.edit().putString(Recipe.RECIPE_INGREDIENTS_STRING, mIngredients).apply();
        mPref.edit().putString(Recipe.RECIPE_STEPS, mStepsString).apply();
        mPref.edit().putString(Recipe.RECIPE_SERVINGS, mServing).apply();

        Snackbar.make(mView, mName + " Is Your Favorite", Snackbar.LENGTH_LONG).show();

        // Updating Widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.baking_widget_list);
        //Now update all widgets
        BakingWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds);
    }

    // This method is called when user clicks star to make Recipe NOT favorite
    private void removeFromFavorites(MenuItem item) {
        // Setting new icon as recipe going out sharedPref
        item.setIcon(R.drawable.ic_action_star_border);
        recipeIsFavorite = false;
        mPref.edit().clear().apply();
        Snackbar.make(mView, mName + " Is Not Your Favorite Anymore", Snackbar.LENGTH_LONG).show();

        // After favorite recipe change / remove, update App widget
        Intent intent = new Intent(this, BakingWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), BakingWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    // Interface implementation for call back
    @Override
    public void onStepSelected(int position) {

        // if Tablet, update step fragment
        if (mTwoPane) {
            StepFragment stepFragment;
            stepFragment = (StepFragment) getSupportFragmentManager().findFragmentById(R.id.step_fragment);
            stepFragment.navigateToStep(position);
        } else {    // if normal phone (Portrait) Start StepActivity
            Intent intent = new Intent(this, com.app.android.bakingapp.activity.StepActivity.class);
            intent.putExtra(Recipe.RECIPE_STEPS, mStepsString);
            intent.putExtra(Recipe.Step.STEP_ID, position);
            intent.putExtra(Recipe.RECIPE_NAME, mName);
            startActivity(intent);
        }
    }
}