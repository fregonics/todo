package com.github.fregonics.todo;

//**************************
//SETTING RECYCLERVIEW
//**************************

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.fregonics.todo.Data.TaskGroup;

public class ListOfTasksAdapter extends RecyclerView.Adapter<ListOfTasksAdapter.TaskViewHolder> {
    private TaskGroup taskGroup;
    final private ListItemClickListener mclickListener;


    public ListOfTasksAdapter(TaskGroup taskGroup, ListItemClickListener clickListener) {
        this.taskGroup = taskGroup;
        mclickListener = clickListener;
    }

    public interface ListItemClickListener{
        void onListItemClick(int itemIndex);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        public TaskViewHolder(@NonNull TextView textView) {
            super(textView);
            this.textView = textView;
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(ListOfTasksAdapter.class.getSimpleName(), "DETECTOU CLIQUE " + getAdapterPosition());
            mclickListener.onListItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_layout, viewGroup, false);
        TaskViewHolder tvh = new TaskViewHolder(textView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.textView.setText(taskGroup.getTask(i).title);
    }

    @Override
    public int getItemCount() {
        if(taskGroup == null)
            return 0;
        else
            return taskGroup.getNumberOfTasks();
    }
}