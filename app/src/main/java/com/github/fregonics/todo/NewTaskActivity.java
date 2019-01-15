package com.github.fregonics.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class NewTaskActivity extends AppCompatActivity {
    EditText mTaskTitleEditText;
    EditText mTaskDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mTaskTitleEditText = findViewById(R.id.et_title);
        mTaskDescriptionEditText = findViewById(R.id.et_description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_task_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int selectedItem = item.getItemId();
        if(selectedItem == R.id.bt_newtask_save) {
            saveAndExit();
        } else {
            setResult(0);
            finish();
        }
        return true;
    }

    void saveAndExit() {
        String taskTitle = mTaskTitleEditText.getText().toString();
        String taskDescription = mTaskDescriptionEditText.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.task_title), taskTitle)
                .putExtra(getString(R.string.task_description), taskDescription);

        setResult(1,intent);
        finish();
    }
}
