package com.example.dianielm.widom.Contact;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Daniel on 16/01/2017.
 *
 *
 */

public class ContactFirebaseHelper {

    DatabaseReference dbContact;
    Boolean savedContact;
    ArrayList<Contact> contacts=new ArrayList<>();
    private ValueEventListener listener;

    /*
    Przekazanie referencji do bazy danych

    Zasadniczo to nasza klasa CRUD – od ang. create, read, update and delete
    Tutaj wykonujemy odczyt i zapis w bazie Firebase
    Wypełniamy ArrayList z obiektów moedlu
  */

    public ContactFirebaseHelper(DatabaseReference dbTaskReference) {
        this.dbContact = dbTaskReference.child("Contact");
    }
    //Zapisz jeśli nie jest nullem
    public Boolean saveContact(Contact contact)
    {
        if(contact==null)
        {
            savedContact=false;
        }else
        {
            try
            {
                dbContact.push().setValue(contact);
                savedContact=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                savedContact=false;
            }
        }
        return savedContact;
    }
    //Pobieranie danych
    //Po dodaniu addChildEventListener wszystkie puste metody się wygenerowały
    public ArrayList<Contact> retrieveContact()
    {
        return contacts;
    }

    public void registerListener() {
        if (this.listener != null) {
            dbContact.removeEventListener(this.listener);
        }
        this.listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    contacts.add(snapshot.getValue(Contact.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbContact.addValueEventListener(this.listener);
    }

    public void unregisterListener() {
        if (this.listener != null) {
            dbContact.removeEventListener(this.listener);
        }
    }
}