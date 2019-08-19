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
        void onListItemLongClick(int itemIndex, FrameLayout item);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CheckBox checkBox;
        FrameLayout frameLayout;

        public TaskViewHolder(@NonNull FrameLayout frameLayout) {
            super(frameLayout);
            this.frameLayout = frameLayout;
            checkBox = frameLayout.findViewById(R.id.cb_task);
            checkBox.setOnClickListener(this);
            checkBox.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mclickListener.onListItemClick(getAdapterPosition(), checkBox.isChecked());
            taskGroup.getTask(getAdapterPosition()).isDone = checkBox.isChecked();
        }

        @Override
        public boolean onLongClick(View v) {
            mclickListener.onListItemLongClick(getAdapterPosition(),frameLayout);
            return true;
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