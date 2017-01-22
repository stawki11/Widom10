package com.example.dianielm.widom.Task;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Daniel on 14/01/2017.
 */

public class TaskFirebaseHelper {
    DatabaseReference dbTask;
    Boolean savedTask;
    ArrayList<Task> tasks=new ArrayList<>();

    /*
 Przekazanie referencji do bazy danych

Zasadniczo to nasza klasa CRUD – od ang. create, read, update and delete
Tutaj wykonujemy odczyt i zapis w bazie Firebase
Wypełniamy ArrayList z obiektów moedlu
     */
    public TaskFirebaseHelper(DatabaseReference dbTaskReference) {
        this.dbTask = dbTaskReference;
    }
    //Zapisz jeśli nie jest nullem
    public Boolean saveTask(Task task)
    {
        if(task==null)
        {
            savedTask=false;
        }else
        {
            try
            {
                dbTask.child("Task").push().setValue(task);
                savedTask=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                savedTask=false;
            }
        }
        return savedTask;
    }
    //Implementacja pobrania danych i wypełnienie arraylist
    private void fetchDataTask(DataSnapshot dataSnapshot)
    {
        tasks.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Task task=ds.getValue(Task.class);
            tasks.add(task);
        }
    }
    //Pobieranie danych
    //Po dodaniu addChildEventListener wszystkie puste metody się wygenerowały
    public ArrayList<Task> retrieveTask()
    {
        dbTask.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchDataTask(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchDataTask(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return tasks;
    }
}