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
import android.widget.CheckBox;
import android.widget.FrameLayout;
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
        void onListItemClick(int itemIndex, boolean isDoneState);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;

        public TaskViewHolder(@NonNull FrameLayout frameLayout) {
            super(frameLayout);
            checkBox = frameLayout.findViewById(R.id.cb_task);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mclickListener.onListItemClick(getAdapterPosition(), checkBox.isChecked());
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_layout, viewGroup, false);

        TaskViewHolder tvh = new TaskViewHolder(frameLayout);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.checkBox.setText(taskGroup.getTask(i).title);
        taskViewHolder.checkBox.setChecked(taskGroup.getTask(i).isDone);
    }

    @Override
    public int getItemCount() {
        if(taskGroup == null)
            return 0;
        else
            return taskGroup.getNumberOfTasks();
    }
}