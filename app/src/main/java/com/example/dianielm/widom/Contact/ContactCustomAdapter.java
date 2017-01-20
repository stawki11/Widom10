package com.example.dianielm.widom.Contact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dianielm.widom.R;

import java.util.ArrayList;

/**
 * Created by Daniel on 16/01/2017.
 */

public class ContactCustomAdapter extends BaseAdapter {

    Context cContact;
    ArrayList<Contact> contacts;

    //Konstruktor
    public ContactCustomAdapter(Context cContact, ArrayList<Contact> contacts) {
        this.cContact = cContact;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final ContactViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ContactViewHolder(parent);
        } else {
            viewHolder = (ContactViewHolder) convertView.getTag();
        }
        final Contact contact = (Contact) this.getItem(position);
        viewHolder.fillData(contact);

        //W momencie klikniecia na element zostaje wyświetlony toast z nazwa elementu
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(cContact, contact.getNameEditTextContact(), Toast.LENGTH_SHORT).show();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse((contact.getNumberEditTextContact())));

                if (ActivityCompat.checkSelfPermission(cContact, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                cContact.startActivity(callIntent);
        }
        });

        return viewHolder.view;
    }


    private static class ContactViewHolder {
        View view;
        TextView nameText;
        TextView numberText;

        public ContactViewHolder(ViewGroup parent) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_zadania_contact, parent, false);
            view.setTag(this);
            nameText = (TextView) view.findViewById(R.id.nameTxtContact);
            numberText = (TextView) view.findViewById(R.id.numberTxtContact);
        }

        public void fillData(Contact contact) {
            nameText.setText(contact.getNameEditTextContact());
            numberText.setText(contact.getNumberEditTextContact());
        }
    }
}

