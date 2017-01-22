package com.example.dianielm.widom.Task;

import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 14/01/2017.
 */

public class TaskFirebaseHelper {
    public static final String taskNodeName = "Task";
    DatabaseReference databaseReference;
    List<Task> tasksCache = new ArrayList<>();
    private final OnTaskLoadedListener taskLoadedListener;
    private ValueEventListener eventListener;

    /*
     Przekazanie referencji do bazy danych

    Zasadniczo to nasza klasa CRUD – od ang. create, read, update and delete
    Tutaj wykonujemy odczyt i zapis w bazie Firebase
    Wypełniamy ArrayList z obiektów moedlu
     */
    public TaskFirebaseHelper(DatabaseReference firebaseRootReference, @Nullable OnTaskLoadedListener listener) {
        this.databaseReference = firebaseRootReference.child(taskNodeName);
        this.taskLoadedListener = listener;
    }

    public void registerListeners() {
        unregisterListeners();
        eventListener = this.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                persistTaskData(dataSnapshot);
                if (taskLoadedListener != null) {
                    taskLoadedListener.onTaskLoaded(tasksCache);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // no operation
            }
        });
    }

    public void unregisterListeners() {
        if (eventListener != null) {
            this.databaseReference.removeEventListener(eventListener);
        }
    }

    //Zapisz jeśli nie jest nullem
    public Boolean saveTask(Task task) {
        if (task != null) {
            try {
                databaseReference.push().setValue(task);
                return true;
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //Implementacja pobrania danych i wypełnienie arraylist
    private void persistTaskData(DataSnapshot dataSnapshot) {
        tasksCache.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Task task = ds.getValue(Task.class);
            tasksCache.add(task);
        }
    }

    public static interface OnTaskLoadedListener {
        void onTaskLoaded(List<Task> fetchedTasks);
    }
}