package com.github.fregonics.todo;

public class Task {
    String title;
    String description;

    public Task(String title){this.title = title;}
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    String getTitle() { return title; }
    String getDescription() { return description; }

    void setDescription(String description) { this.description = description; }
}
