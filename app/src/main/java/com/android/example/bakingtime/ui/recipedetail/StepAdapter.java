package com.android.example.bakingtime.ui.recipedetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.model.Step;
import com.android.example.bakingtime.ui.OnListItemClickListener;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final List<Step> steps;
    private final OnListItemClickListener<Step> listener;

    public StepAdapter(@NonNull final List<Step> steps,
                       @NonNull final OnListItemClickListener<Step> listener) {
        this.steps = steps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void setSteps(@NonNull final List<Step> steps) {
        this.steps.clear();
        this.steps.addAll(steps);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView stepDescriptionTextView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            stepDescriptionTextView = itemView.findViewById(R.id.tv_step_short_description);
        }

        void bind(@NonNull final Step step) {
            stepDescriptionTextView.setText(step.getShortDescription());
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(steps.get(getAdapterPosition()));
        }
    }
}
