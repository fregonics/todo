package com.github.fregonics.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.fregonics.todo.Data.TaskGroup;
import com.github.fregonics.todo.Data.TaskGroupsManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ListOfTasksAdapter.ListItemClickListener{
    private final String TASKGROUP_KEY = "main";
    public static final String INTENT_TASK_KEY = "task";
    private final int MENU_SELECTED_DETAILS_INDEX = 0;
    private final int MENU_SELECTED_DELETE_INDEX = 1;
    private final int MENU_SELECTED_CANCEL_INDEX = 2;

    private TaskGroup main;
    private RecyclerView mListOfTasks;
    private RecyclerView.LayoutManager mListOfTasksLayoutManager;
    private RecyclerView.Adapter mListOfTasksAdapter;
    private FloatingActionButton mFloatingActionButton;
    private MenuItem mDetailsTask, mDeleteTask, mCancelTaskSelection;
    private int mSelectedTask;
    private FrameLayout mSelectedTaskItem;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mDrawerMenuItems;

    private String[] mTaskGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mTaskGroups = TaskGroupsManager.getTaskGroupsNames(getApplicationContext());
        } catch (Exception e) {
            Log.d(MainActivity.class.getSimpleName(), "Storing taskgroups for the first time");

            mTaskGroups = new String[5];
            for(int i = 0; i < 5; i ++)
                mTaskGroups[i] = "teste" + i;

            try { TaskGroupsManager.storeTaskGroupsNames(getApplicationContext(), mTaskGroups); }
            catch (Exception e1) { Log.d(MainActivity.class.getSimpleName(), e1.getMessage()); }
        }

        mDrawerMenuItems = new String[mTaskGroups.length + 1];
        for(int i = 0; i < mDrawerMenuItems.length; i ++) {
            if(i < mTaskGroups.length)
                mDrawerMenuItems[i] = mTaskGroups[i];
            else
                mDrawerMenuItems[i] = getString(R.string.new_taskgroup);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.lv_left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, mDrawerMenuItems));
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            String[] newTaskgroups;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < mTaskGroups.length)
                    Toast.makeText(getApplicationContext(), mDrawerMenuItems[position], Toast.LENGTH_LONG).show();
                else {
                    newTaskgroups = new String[mTaskGroups.length + 1];
                    for(int i = 0; i < newTaskgroups.length; i ++) {
                        if(i < mTaskGroups.length)
                            newTaskgroups[i] = mTaskGroups[i];
                        else
                            newTaskgroups[i] = "teste" + i;
                    }
                    mTaskGroups = newTaskgroups;
                }
                mDrawerMenuItems = new String[mTaskGroups.length + 1];
                for(int i = 0; i < mDrawerMenuItems.length; i ++) {
                    if(i < mTaskGroups.length)
                        mDrawerMenuItems[i] = mTaskGroups[i];
                    else
                        mDrawerMenuItems[i] = getString(R.string.new_taskgroup);
                }
                mDrawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.drawer_item, mDrawerMenuItems));
            }
        });

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

        mSelectedTask = -1;
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
        try {
            TaskGroupsManager.storeTaskGroupsNames(getApplicationContext(), mTaskGroups);
        } catch (Exception e) {
            Log.d(MainActivity.class.getSimpleName(), e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selected_task_options_menu, menu);

        mDetailsTask = menu.getItem(MENU_SELECTED_DETAILS_INDEX);
        mDeleteTask = menu.getItem(MENU_SELECTED_DELETE_INDEX);
        mCancelTaskSelection = menu.getItem(MENU_SELECTED_CANCEL_INDEX);
        mDetailsTask.setVisible(false);
        mDeleteTask.setVisible(false);
        mCancelTaskSelection.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item == mDeleteTask) {
            try { main.removeTask(mSelectedTask); }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "An error has ocurred", Toast.LENGTH_LONG);
            }
            setRecyclerView();
        }
        else if(item == mCancelTaskSelection) {
            mSelectedTaskItem.setBackgroundColor(getResources().getColor(R.color.taskDefaultBkgColor));
        }
        else if(item == mDetailsTask) {
            callDetailsTaskActivity(mSelectedTask);
        }
        unSelectTaskItem();
        return true;
    }

    @Override
    public void onListItemClick(int itemIndex, boolean isDoneState) {
        if(mSelectedTask == itemIndex) {
            CheckBox checkBoxTask = mSelectedTaskItem.findViewById(R.id.cb_task);
            checkBoxTask.setChecked(!checkBoxTask.isChecked());
            unSelectTaskItem();
        }
        else if(mSelectedTask != -1) {
            unSelectTaskItem();
            main.getTask(itemIndex).isDone = isDoneState;
        }
        else {
            main.getTask(itemIndex).isDone = isDoneState;
        }
    }

    @Override
    public void onListItemLongClick(int itemIndex, FrameLayout item) {
        if(mSelectedTask != -1)
            mSelectedTaskItem.setBackgroundColor(getResources().getColor(R.color.taskDefaultBkgColor));

        mSelectedTaskItem = item;
        mSelectedTaskItem.setBackgroundColor(getResources().getColor(R.color.colorTaskSelected));
        mSelectedTask = itemIndex;
        mSelectedTaskItem = item;
        mDetailsTask.setVisible(true);
        mDeleteTask.setVisible(true);
        mCancelTaskSelection.setVisible(true);
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

    void callDetailsTaskActivity(int i) {
        Intent intent = new Intent(this, TaskDetailsActivity.class);
        intent.putExtra(INTENT_TASK_KEY, main.getTask(i));
        startActivity(intent);
    }

    void setRecyclerView() {
        mListOfTasks = findViewById(R.id.tasks_list);
        mListOfTasks.setHasFixedSize(true);
        mListOfTasksLayoutManager = new LinearLayoutManager(this);
        mListOfTasks.setLayoutManager(mListOfTasksLayoutManager);
        mListOfTasksAdapter = new ListOfTasksAdapter(main,this);
        mListOfTasks.setAdapter(mListOfTasksAdapter);
    }
    void unSelectTaskItem() {
        mSelectedTask = -1;
        mSelectedTaskItem.setBackgroundColor(getResources().getColor(R.color.taskDefaultBkgColor));
        mDetailsTask.setVisible(false);
        mDeleteTask.setVisible(false);
        mCancelTaskSelection.setVisible(false);
    }



}
