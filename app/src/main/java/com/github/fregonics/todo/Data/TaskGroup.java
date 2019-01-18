package com.github.fregonics.todo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TaskGroup implements Parcelable {
    String name;
    private ArrayList<Task> tasks;



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
    public void removeTask(int i) throws Exception{
        Task remove = tasks.remove(i);
        if (remove == null)
            throw new Exception("Task not found");
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
