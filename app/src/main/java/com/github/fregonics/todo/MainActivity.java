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

public class MainActivity extends AppCompatActivity implements ListOfTasksAdapter.ListItemClickListener{
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

        mFloatingActionButton = findViewById(R.id.fab_new_task);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNewTaskActivity();
            }
        });
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
    protected void onStop() {
        super.onStop();
        try {
            main.writeToFile(getApplicationContext());
        } catch (Exception e) {
            Log.d(MainActivity.class.getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onListItemClick(int itemIndex, boolean isDoneState) {
        Log.d(MainActivity.class.getSimpleName(), "DETECTOU CLIQUE " + itemIndex);
        Toast.makeText(getApplicationContext(),main.getTask(itemIndex).title,Toast.LENGTH_SHORT).show();
        main.getTask(itemIndex).isDone = isDoneState;
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
        mListOfTasksAdapter = new ListOfTasksAdapter(main,this);
        mListOfTasks.setAdapter(mListOfTasksAdapter);
    }



}
