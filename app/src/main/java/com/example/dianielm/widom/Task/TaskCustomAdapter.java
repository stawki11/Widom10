package com.example.dianielm.widom.Task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dianielm.widom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 14/01/2017.
 */

public class TaskCustomAdapter extends BaseAdapter {


    /*
     Gdy chcemy użyć w Androidzie pinową listę przeiwjanych elementów ListView musimy,
    użyć adaptera do wyświetlania danych. ArrayAdapter orzejszta kca ArrayList obiektów w polu i ładuje dane
    do ListView
     */

    Context cTask;
    ArrayList<Task> tasks = new ArrayList<>();
    //Konstruktor
    public TaskCustomAdapter(Context cTask) {
        this.cTask = cTask;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(cTask).inflate(R.layout.model_zadania_widom_list,parent,false);
        }

        TextView nameTxtTask = (TextView) convertView.findViewById(R.id.nameTxtTask);
        TextView authTxtTask = (TextView) convertView.findViewById(R.id.authorTxtTask);
        TextView descTxtTask = (TextView) convertView.findViewById(R.id.descTxtTask);

        final Task sTask = (Task) this.getItem(position);

        nameTxtTask.setText(sTask.getNameEditTextTask());
        authTxtTask.setText(sTask.getAuthorEditTextTask());
        descTxtTask.setText(sTask.getDescEditTextTask());

        //W momencie klikniecia na element zostaje wyświetlony toast z nazwa elementu
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(cTask, sTask.getNameEditTextTask(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}





