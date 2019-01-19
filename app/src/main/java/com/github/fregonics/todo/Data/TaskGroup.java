package com.github.fregonics.todo.Data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TaskGroup implements Parcelable {
    String name;
    private ArrayList<Task> tasks;

    private static final String TASK_TITLE_KEY = "task_title";
    private static final String TASK_DESCRIPTION_KEY = "task_description";
    private static final String TASKGROUP_NAME_KEY = "taskgroup_name";
    private static final String TASKGROUP_TASKS_KEY = "taskgroup_tasks";
    private static final int MAX_FILE_SIZE_TEMP = 999999;

    public TaskGroup(String name) {
        this.name = name;
        tasks = new ArrayList<Task>();
    }
    public TaskGroup(Parcel in) {
        name = in.readString();
        in.readTypedList(tasks, Task.CREATOR);
    }




    public Task getTask(int i) { return tasks.get(i); }
    public int getNumberOfTasks() { return tasks.size(); }





    public void addTask(String title) {
        Task t = new Task(title);
        tasks.add(t);
    }
    public void addTask(String title, String description){
        Task t = new Task(title,description);
        tasks.add(t);
    }
    public void addTask(Task t) {
        tasks.add(t);
    }
    public void addTask(JSONObject object) throws JSONException{
        Task t = new Task(object.getString(TASK_TITLE_KEY), object.getString(TASK_DESCRIPTION_KEY));
        tasks.add(t);
    }
    public void removeTask(int i) throws Exception{
        Task remove = tasks.remove(i);
        if (remove == null)
            throw new Exception("Task not found");
    }


    //*****************************
    // STORAGE INTERFACE
    //*****************************
    public void writeToFile(Context context) throws Exception {
        FileOutputStream file = context.openFileOutput(name, context.MODE_PRIVATE);

        JSONObject taskGroup = transformIntoJSON();
        String output = taskGroup.toString();

        file.write(output.getBytes());
        file.close();

        Log.d(TaskGroup.class.getSimpleName(), "ESCREVENDO NO ARQUIVO " + output);
    }
    public void readFromFile(Context context) throws Exception{
        FileInputStream file = context.openFileInput(name);
        byte[] bytes = new byte[MAX_FILE_SIZE_TEMP];
        file.read(bytes);


        String input = new String(bytes);
        int finalOfQuery = input.indexOf("}]}");
        String query = input.substring(0,finalOfQuery + 3);
        Log.d(TaskGroup.class.getSimpleName(), "LENDO DO ARQUIVO" + query);

        transformFromJSonString(query);
        file.close();


    }



    //**************************
    // JSON TRANSFORMATION FUNCTIONS
    //***************************
    public JSONObject transformIntoJSON() throws JSONException {
        JSONArray tasksArray = new JSONArray();
        JSONObject task;
        JSONObject result = new JSONObject();

        for(int i = 0; i < tasks.size(); i ++) {
            task = new JSONObject();
            task.put(TASK_TITLE_KEY, tasks.get(i).title);
            task.put(TASK_DESCRIPTION_KEY, tasks.get(i).description);

            tasksArray.put(task);
        }

        result.put(TASKGROUP_NAME_KEY, name);
        result.put(TASKGROUP_TASKS_KEY, tasksArray);
        return result;
    }
    public void transformFromJSonString(String jsonString) throws Exception{
        JSONObject group = new JSONObject(jsonString);
        Log.d(TaskGroup.class.getSimpleName(),"NOME " + group.getString(TASKGROUP_NAME_KEY));
        JSONArray tasksArray = group.getJSONArray(TASKGROUP_TASKS_KEY);
        //Log.d(TaskGroup.class.getSimpleName(), "PEGANDO DA STRING " + group.getString(TASK_TITLE_KEY));
        for(int i = 0; i < tasksArray.length(); i ++) {
            Log.d(TaskGroup.class.getSimpleName(), "NO LOOP");
            JSONObject object = tasksArray.getJSONObject(i);
            Log.d(TaskGroup.class.getSimpleName()
                    , object.toString());
            addTask(object);
        }
    }

    //*********************
    // SETTING PARCELABLE
    //*********************

    public static final Parcelable.Creator<TaskGroup> CREATOR =
            new Parcelable.Creator<TaskGroup>() {

                @Override
                public TaskGroup createFromParcel(Parcel source) {
                    return new TaskGroup(source);
                }

                @Override
                public TaskGroup[] newArray(int size) {
                    return new TaskGroup[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(tasks);
    }
}
