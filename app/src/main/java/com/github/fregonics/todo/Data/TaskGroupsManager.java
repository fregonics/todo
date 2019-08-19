package com.github.fregonics.todo.Data;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;

import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TaskGroupsManager {

    public static void storeTaskGroupsNames(Context context, String[] taskgroups) throws Exception{
        FileOutputStream file = context.openFileOutput("TASKGROUPS", context.MODE_PRIVATE);
        JSONArray taskgroupsArray = new JSONArray();

        for(int i = 0; i < taskgroups.length; i ++)
            taskgroupsArray.put(taskgroups[i]);

        String output = taskgroupsArray.toString();
        file.write(output.getBytes());
        file.close();
    }

    public static String[] getTaskGroupsNames(Context context) throws Exception{
        FileInputStream file = context.openFileInput("TASKGROUPS");
        byte[] bytes = new byte[2803544];
        file.read(bytes);
        String input = new String(bytes);

        JSONArray taskGroupsArray = new JSONArray(input);
        String[] result = new String[taskGroupsArray.length()];
        for(int i = 0; i < taskGroupsArray.length(); i ++)
            result[i] = taskGroupsArray.getString(i);

        return result;
    }
}
