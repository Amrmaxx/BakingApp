package com.app.android.bakingapp.Activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.bakingapp.Activity.Adapters.StepAdapter;
import com.app.android.bakingapp.Model.Recipe;
import com.app.android.bakingapp.R;
import com.app.android.bakingapp.Utilities.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    private String mIngredients;
    private String mStepsString;
    private String mServings;
    private String mIngredientsAndServings;
    private List<Recipe.Step> mSteps;
    private StepAdapter mAdapter;
    private int mScrollPosition;
    private final static String SCROLL_POSITION = "scroll_position";
    private boolean mShowIngredients = false;
    private LinearLayoutManager mLayoutManager;
    private OnStepClickListener mListener;

    // Binding Views
    @BindView(R.id.ingredients_list_tv)
    TextView mIngredients_tv;
    @BindView(R.id.steps_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.expand_icon)
    ImageView mExpandIcon;


    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    // Empty Constructor
    public RecipeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        prepareData(intent.getExtras());
        populateUI();

        // Saving Scroll Position of Steps
        if (savedInstanceState != null) {
            mScrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
        }
        return rootView;
    }

    // Method to Prepare Strings and layout for population
    private void prepareData(Bundle bundle) {

        // Getting data from intent bundle
        mIngredients = bundle.getString(Recipe.RECIPE_INGREDIENTS_STRING);
        mStepsString = bundle.getString(Recipe.RECIPE_STEPS);
        mServings = bundle.getString(Recipe.RECIPE_SERVINGS);

        // Preparing a single String to show ingredients and servings
        mIngredientsAndServings = mIngredients + "\n\nSuitable for " + mServings + " persons.";

        // Parsing Steps
        mSteps = JsonUtils.parseSteps(mStepsString);

        // Preparing UI
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new StepAdapter(getActivity(), mSteps, this);
    }

    // Method to Populate UI
    private void populateUI() {
        mIngredients_tv.setText(mIngredientsAndServings);
        mRecyclerView.setAdapter(mAdapter);
    }

    // call back from click (implemented on the host Activity)
    @Override
    public void onStepClick(int index) {
        mListener.onStepSelected(index);
    }

    // Call back from show / hide ingredients button
    @OnClick(R.id.expand_icon)
    public void changeView() {
        mScrollPosition = mLayoutManager.findLastVisibleItemPosition();
        showOrHideIngredients(mShowIngredients);
    }

    // Method to show / hide ingredients
    public void showOrHideIngredients(boolean showIngredients) {
        if (showIngredients) {
            mIngredients_tv.setVisibility(View.VISIBLE);
            mExpandIcon.setImageResource(R.drawable.ic_arrow_drop_up);
            mShowIngredients = false;
        } else {
            mIngredients_tv.setVisibility(View.GONE);
            mExpandIcon.setImageResource(R.drawable.ic_arrow_drop_down);
            mShowIngredients = true;
        }
        mLayoutManager.scrollToPosition(mScrollPosition);
    }

    // saving scroll position for steps
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mScrollPosition = mLayoutManager.findLastVisibleItemPosition();
        outState.putInt(SCROLL_POSITION, mScrollPosition);
    }

    // detaching listener to no leak
    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
}