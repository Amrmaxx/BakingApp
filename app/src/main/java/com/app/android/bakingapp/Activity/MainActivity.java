package com.app.android.bakingapp.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.android.bakingapp.Activity.Adapters.RecipeAdapter;
import com.app.android.bakingapp.Model.Recipe;
import com.app.android.bakingapp.R;
import com.app.android.bakingapp.Utilities.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String RESPONSE_JSON_STRING = "response_json";
    private String mResponse;
    private List<Recipe> mRecipes;
    private RecipeAdapter mAdapter;
    private ConnectionBroadcastReceiver mConnectionBroadcastReceiver = new ConnectionBroadcastReceiver();
    private String url;

    // Binding Views
    @BindView(R.id.error_view)
    LinearLayout errorView;
    @BindView(R.id.recipes_RV)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Getting recipe request URL
        url = getResources().getString(R.string.BASE_RECIPES_URL_STRING);

        if (savedInstanceState == null) {
            showLoading();
            loadRecipes(url);

        } else {
            mResponse = savedInstanceState.getString(RESPONSE_JSON_STRING);
            populateUI();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Saving Json data response
        outState.putString(RESPONSE_JSON_STRING, mResponse);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
    }

    private void finishedLoading() {
        mRecyclerView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        mRecyclerView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
    }

    // Method to Populate UI with loaded data
    private void populateUI() {

        if (mResponse == null) {
            showError();
            return;
        }
        // if device is in portrait = 1, if in landscape = 2, luckily this fits number of columns in Recycler view at needed orientation
        int orientation = getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager = new GridLayoutManager(this, orientation);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipes = JsonUtils.parseRecipes(mResponse);
        mAdapter = new RecipeAdapter(this, mRecipes, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        finishedLoading();
    }

    // Method to start Network thread via Volley
    private void loadRecipes(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResponse = response;
                        populateUI();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError();
            }
        });
        queue.add(stringRequest);
    }

    // OnClick Listener for Recipe items
    @Override
    public void onRecipeClick(int index) {
        Recipe recipe = mRecipes.get(index);
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(Recipe.RECIPE_NAME, recipe.getRecipeName());
        intent.putExtra(Recipe.RECIPE_INGREDIENTS_STRING, recipe.getRecipeIngredientsString());
        intent.putExtra(Recipe.RECIPE_STEPS, recipe.getRecipeStepsString());
        intent.putExtra(Recipe.RECIPE_SERVINGS, recipe.getRecipeServings());
        startActivity(intent);
    }

    //  In case App started with no internet and it came back onReceive will be called to load data
    public class ConnectionBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = null;
            if (cm != null) {
                activeNetwork = cm.getActiveNetworkInfo();
            }
            if (activeNetwork != null && mResponse == null) {
                showLoading();
                loadRecipes(url);
            }
        }
    }

    //  Registering a broadcast receiver to find when internet is back
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(mConnectionBroadcastReceiver, intentFilter);
    }

    // Unregister receiver when leaving app
    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mConnectionBroadcastReceiver);
    }
}