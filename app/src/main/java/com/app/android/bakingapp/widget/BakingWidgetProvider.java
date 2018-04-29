package com.app.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.app.android.bakingapp.R;
import com.app.android.bakingapp.activity.MainActivity;
import com.app.android.bakingapp.activity.RecipeActivity;
import com.app.android.bakingapp.model.Recipe;


public class BakingWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        RemoteViews views;
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.SHARED_PREF_KEY), Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(Recipe.RECIPE_NAME, "");

        if (name.length() < 3) {       // no Recipe is preferred, show default cake imageView
            views = loadEmptyFavorite(context);
        } else {    // A Recipe is favored, show ingredients
            views = loadFavoriteRecipe(context);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                        int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    // Method to load cake view as there is no recipe favored
    public static RemoteViews loadEmptyFavorite(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        views.setViewVisibility(R.id.cake_iv, View.VISIBLE);
        views.setViewVisibility(R.id.ingredients_view, View.GONE);
        // on click open main activity
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);
        views.setOnClickPendingIntent(R.id.baking_widget_layout, pendingIntent);
        return views;
    }

    // Method to Load Recipe Name & ingredients into list view of one item (to be scrollable)

    public static RemoteViews loadFavoriteRecipe(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_list);
        Intent intent = new Intent(context, ListWidgetAdapter.class);
        views.setRemoteAdapter(R.id.baking_widget_list, intent);
        Intent appIntent = new Intent(context, RecipeActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.baking_widget_list, appPendingIntent);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}