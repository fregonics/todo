package com.github.fregonics.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.fregonics.todo.Data.Task;

public class TaskDetailsActivity extends AppCompatActivity {
    Intent mParentIntent;
    TextView mTitle, mDescription;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        mParentIntent = getIntent();
        mTitle = findViewById(R.id.tv_details_title);
        mDescription = findViewById(R.id.tv_details_description);

        task = mParentIntent.getParcelableExtra(MainActivity.INTENT_TASK_KEY);

        if(task.isDone)
            mTitle.setText(task.title + " " + getString(R.string.task_details_completed_text));
        else
            mTitle.setText(task.title);
        mDescription.setText(task.description);
    }
}
