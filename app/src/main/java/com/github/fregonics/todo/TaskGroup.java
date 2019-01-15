package com.github.fregonics.todo;

import java.util.ArrayList;

public class TaskGroup {
    String name;
    private ArrayList<Task> tasks;

    public TaskGroup(String name) {
        this.name = name;
        tasks = new ArrayList<Task>();
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

    public void removeTask(int i) throws Exception{
        Task remove = tasks.remove(i);
        if (remove == null)
            throw new Exception("Task not found");
    }

}
