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

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private List<Recipe.Step> mSteps;
    private Context mContext;
    private StepAdapterOnClickHandler mOnStepClickHandler;

    public interface StepAdapterOnClickHandler {
        void onStepClick(int index);
    }

    public StepAdapter(Context context, List<Recipe.Step> Steps, StepAdapter.StepAdapterOnClickHandler clickHandler) {
        mContext = context;
        mSteps = Steps;
        mOnStepClickHandler = clickHandler;
    }

    @Override
    public StepAdapter.StepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int StepItemID = R.layout.step_item;
        View view = LayoutInflater.from(mContext).inflate(StepItemID, parent, false);
        return new StepAdapter.StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapter.StepAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class StepAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView stepId;
        TextView stepDescription;
        ImageView videoIcon;


        public StepAdapterViewHolder(View itemView) {
            super(itemView);

            stepId = itemView.findViewById(R.id.step_id);
            stepDescription = itemView.findViewById(R.id.step_description);
            videoIcon = itemView.findViewById(R.id.video_icon);

            itemView.setOnClickListener(this);
        }

        void bind(int index) {
            int id = mSteps.get(index).getStepID();
            String idString = "" + (id + 1);
            String name = mSteps.get(index).getStepShortDescription();
            stepId.setText(idString);
            stepDescription.setText(name);

            // showing / hiding video Icon if step has / has not video guide
            if (mSteps.get(index).getStepVideoURL().equals("")) {
                videoIcon.setVisibility(View.INVISIBLE);
            } else {
                videoIcon.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mOnStepClickHandler.onStepClick(adapterPosition);
        }
    }
}