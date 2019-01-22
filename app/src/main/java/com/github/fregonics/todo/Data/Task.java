package com.github.fregonics.todo.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Task implements Parcelable {
    public String title;
    public String description;
    public boolean isDone;


    public Task(String title){
        this.title = title;
        isDone = false;
    }
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        isDone = false;
    }
    public Task(Parcel in) {
        Log.d("Task", "TENTOU RECRIAR");
        title = in.readString();
        description = in.readString();
        if(in.readInt() == 1) isDone = true;
        else isDone = false;
    }



    //*************************
    // SETTING PARCELABLE
    //*************************
    public static final Parcelable.Creator<Task> CREATOR =
            new Parcelable.Creator<Task>() {

                @Override
                public Task createFromParcel(Parcel source) {
                    return new Task(source);
                }

                @Override
                public Task[] newArray(int size) {
                    return new Task[0];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        if(isDone) dest.writeInt(1);
        else dest.writeInt(0);
    }
}
