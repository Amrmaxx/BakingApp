package com.app.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.app.android.bakingapp.model.Recipe;
import com.app.android.bakingapp.R;

public class ListWidgetAdapter extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    public ListRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {


    }

    @Override
    public int getCount() {
        // This List view has only one item
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        // Getting string from sharedPreferences
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.SHARED_PREF_KEY), Context.MODE_PRIVATE);
        String name = preferences.getString(Recipe.RECIPE_NAME, "");
        String ingredients = preferences.getString(Recipe.RECIPE_INGREDIENTS_STRING, "");
        String steps = preferences.getString(Recipe.RECIPE_STEPS, "");
        String serving = preferences.getString(Recipe.RECIPE_SERVINGS, "");

        if (name.length() < 3) return null;
        // Setting up view
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_provider);
        views.setViewVisibility(R.id.cake_iv, View.GONE);
        views.setViewVisibility(R.id.ingredients_view, View.VISIBLE);
        views.setTextViewText(R.id.name_tv, name);
        views.setTextViewText(R.id.ingredients_tv, ingredients);

        // defining Fill InIntent for click
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Recipe.RECIPE_NAME, name);
        fillInIntent.putExtra(Recipe.RECIPE_INGREDIENTS_STRING, ingredients);
        fillInIntent.putExtra(Recipe.RECIPE_STEPS, steps);
        fillInIntent.putExtra(Recipe.RECIPE_SERVINGS, serving);
        views.setOnClickFillInIntent(R.id.baking_widget_layout, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}