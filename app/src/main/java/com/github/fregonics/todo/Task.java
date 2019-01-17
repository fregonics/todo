package com.github.fregonics.todo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Task implements Parcelable {
    String title;
    String description;

    public Task(String title){this.title = title;}
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public Task(Parcel in) {
        Log.d("Task", "TENTOU RECRIAR");
        title = in.readString();
        description = in.readString();
    }

    String getTitle() { return title; }
    String getDescription() { return description; }

    void setDescription(String description) { this.description = description; }




    public static final Parcelable.Creator<Task> CREATOR =
            new Parcelable.Creator<Task>() {

                @Override
                public Task createFromParcel(Parcel source) {
                    Log.d("Task", "PASSOU PELO CREATOR");
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
        Log.d("Task", "TENTOU ESCREVER");
        dest.writeString(title);
        dest.writeString(description);
    }
}
