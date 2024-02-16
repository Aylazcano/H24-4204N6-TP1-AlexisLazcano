package com.example.tp1clientandroid;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    public List<Task> taskList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskNameTV;
        public TextView completionPercentageTV;
        public TextView timeElapsedPercentageTV;
        public TextView deadlineDateTV;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            taskNameTV = view.findViewById(R.id.taskNameRV);
            completionPercentageTV = view.findViewById(R.id.completionPercentageRV);
            timeElapsedPercentageTV = view.findViewById(R.id.timeElapsedPercentageRV);
            deadlineDateTV = view.findViewById(R.id.deadlineDateRV);
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
        viewHolder.taskNameTV.setText(currentTask.taskName);
        viewHolder.deadlineDateTV.setText(currentTask.deadlineDate.toString());
        viewHolder.completionPercentageTV.setText(String.valueOf(currentTask.completionPercentage) + "%");
        viewHolder.timeElapsedPercentageTV.setText(String.valueOf(currentTask.timeElapsedPercentage) + "%");

        // Format the deadline date to yyyy/MM/dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDeadlineDate = sdf.format(currentTask.deadlineDate);
        viewHolder.deadlineDateTV.setText(formattedDeadlineDate);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task selectedTask = taskList.get(position);
                Intent intent = new Intent(view.getContext(),TaskConsultationActivity.class);

                intent.putExtra("selectedTaskName", selectedTask.taskName);
                intent.putExtra("selectedTaskCompletionPercentage", String.valueOf(selectedTask.completionPercentage));
                intent.putExtra("selectedTaskTimeElapsedPercentage", String.valueOf(selectedTask.timeElapsedPercentage));
                intent.putExtra("selectedTaskDeadlineDate", formattedDeadlineDate);

                view.getContext().startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
