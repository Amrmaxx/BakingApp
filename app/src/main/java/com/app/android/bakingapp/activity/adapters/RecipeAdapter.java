package com.app.android.bakingapp.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.bakingapp.R;
import com.app.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> mRecipes;
    private Context mContext;
    private RecipeAdapterOnClickHandler mOnRecipeClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onRecipeClick(int index);
    }

    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mRecipes = recipes;
        mOnRecipeClickHandler = clickHandler;
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int recipeItemID = R.layout.recipe_item;
        View view = LayoutInflater.from(mContext).inflate(recipeItemID, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView recipeTV;
        ImageView recipeImage;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            recipeTV = itemView.findViewById(R.id.recipe_tv);
            recipeImage = itemView.findViewById(R.id.recipe_iv);

            itemView.setOnClickListener(this);
        }

        void bind(int index) {
            String name = mRecipes.get(index).getRecipeName();
            recipeTV.setText(name);

            // Loading Recipe Url if exists
            if (mRecipes.get(index).getRecipeImage().isEmpty()) {
                recipeImage.setVisibility(View.GONE);
            } else {
                recipeImage.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(mRecipes.get(index).getRecipeImage()).into(recipeImage);
            }

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mOnRecipeClickHandler.onRecipeClick(adapterPosition);
        }
    }
}