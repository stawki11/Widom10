package com.example.dianielm.widom.Task;

/**
 * Created by Daniel on 16/01/2017.
 *
 * To jest klasa Data Object. Musi mieć pusty konstruktor.
 * Można tworzyć, przekazywać dane i korzystać z innych konstruktorów.
 */


public class Task {

    String nameEditTextTask,authorEditTextTask, descEditTextTask;
    //TaskAuthorEditText

    public Task() {
    }

    public String getNameEditTextTask() {
        return nameEditTextTask;
    }

    public void setNameEditTextTask(String nameTxtTask) {
        this.nameEditTextTask = nameTxtTask;
    }

    public String getAuthorEditTextTask() {
        return authorEditTextTask;
    }

    public void setAuthorEditTextTask(String authorEditText) {
        this.authorEditTextTask=authorEditText;
    }

    public String getDescEditTextTask() {
        return descEditTextTask;
    }

    public void setDescEditTextTask(String descEditTextTask) {
        this.descEditTextTask = descEditTextTask;
    }
}