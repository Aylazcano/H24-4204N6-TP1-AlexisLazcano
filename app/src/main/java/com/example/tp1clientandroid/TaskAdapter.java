package com.example.tp1clientandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    public List<Task> taskList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName;
        public TextView completionPercentage;
        public TextView timeElapsedPercentage;
        public TextView deadlineDate;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            taskName = view.findViewById(R.id.taskName);
            completionPercentage = view.findViewById(R.id.completionPercentage);
            timeElapsedPercentage = view.findViewById(R.id.timeElapsedPercentage);
            deadlineDate = view.findViewById(R.id.deadlineDate);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskAdapter() {
        taskList = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_recycler_view, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Task currentTask = taskList.get(position);
        viewHolder.taskName.setText(currentTask.taskName);
        viewHolder.deadlineDate.setText(currentTask.deadlineDate.toString());
        viewHolder.completionPercentage.setText(String.valueOf(currentTask.completionPercentage) + "%");
        viewHolder.timeElapsedPercentage.setText(String.valueOf(currentTask.timeElapsedPercentage) + "%");

        // Format the deadline date to yyyy/MM/dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDeadlineDate = sdf.format(currentTask.deadlineDate);
        viewHolder.deadlineDate.setText(formattedDeadlineDate);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
