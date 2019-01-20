package com.github.fregonics.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.fregonics.todo.Data.TaskGroup;

public class MainActivity extends AppCompatActivity {
    private final String TASKGROUP_KEY = "main";

    private TaskGroup main;
    private RecyclerView mListOfTasks;
    private RecyclerView.LayoutManager mListOfTasksLayoutManager;
    private RecyclerView.Adapter mListOfTasksAdapter;
    private FloatingActionButton mFloatingActionButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
            main = savedInstanceState.getParcelable(TASKGROUP_KEY);
        else {
            main = new TaskGroup("main");
            try {
                main.readFromFile(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setRecyclerView();
    }




    //********************
    // SETTING ACTIVITIES BEHAVIOR
    //********************

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(main != null) {
            if (main.getNumberOfTasks() > 0)
                outState.putParcelable(TASKGROUP_KEY, main);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String newTaskTitle, newTaskDescription;


        if(resultCode == 1) {
            newTaskTitle = data.getStringExtra(getString(R.string.task_title));
            newTaskDescription = data.getStringExtra(getString(R.string.task_description));

            if(newTaskDescription != "")
                main.addTask(newTaskTitle,newTaskDescription);
            else
                main.addTask(newTaskTitle);

            try {
                main.writeToFile(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    //*********************
    // SPECIFIC ACTION FUNCTIONS
    //*********************

    void callNewTaskActivity() {
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivityForResult(intent,1);
    }

    void setRecyclerView() {
        mListOfTasks = findViewById(R.id.tasks_list);
        mListOfTasks.setHasFixedSize(true);
        mListOfTasksLayoutManager = new LinearLayoutManager(this);
        mListOfTasks.setLayoutManager(mListOfTasksLayoutManager);
        mListOfTasksAdapter = new ListOfTasksAdapter(main);
        mListOfTasks.setAdapter(mListOfTasksAdapter);

        mFloatingActionButton = findViewById(R.id.fab_new_task);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNewTaskActivity();
            }
        });
    }




    //**************************
    //SETTING RECYCLERVIEW
    //**************************

    class ListOfTasksAdapter extends RecyclerView.Adapter<ListOfTasksAdapter.TaskViewHolder> {
        private TaskGroup taskGroup;

        public ListOfTasksAdapter(TaskGroup taskGroup) {
            this.taskGroup = taskGroup;
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public TaskViewHolder(@NonNull TextView textView) {
                super(textView);
                this.textView = textView;
            }
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            TextView textView = (TextView) LayoutInflater.from(getApplicationContext())
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
}
