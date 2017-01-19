package com.example.dianielm.widom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dianielm.widom.Contact.Contact;
import com.example.dianielm.widom.Contact.ContactCustomAdapter;
import com.example.dianielm.widom.Contact.ContactFirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactActivity extends AppCompatActivity {

    DatabaseReference dbContact;
    ContactFirebaseHelper helperContactActivity;
    ContactCustomAdapter adapterContactActivity;
    ListView lvContactActivity;
    EditText nameEditTxtContact, descTxtContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvContactActivity = (ListView) findViewById(R.id.lvContactPokazContact);

        //Inicjializacja bazy danych firebase
        dbContact = FirebaseDatabase.getInstance().getReference();
        helperContactActivity = new ContactFirebaseHelper(dbContact);

        //Adapter
        helperContactActivity.registerListener();
        adapterContactActivity = new ContactCustomAdapter(this, helperContactActivity.retrieveContact());
        lvContactActivity.setAdapter(adapterContactActivity);
    }

    //DISPLAY INPUT DIALOG
    private void displayInputDialog() {
        Dialog d = new Dialog(this);
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.input_dialog_contact);
        nameEditTxtContact = (EditText) d.findViewById(R.id.nameEditTextContact);
        descTxtContact = (EditText) d.findViewById(R.id.descEditTextContact);
        Button saveBtnContact = (Button) d.findViewById(R.id.saveBtnContact);
        //SAVE
        saveBtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String nameContact = nameEditTxtContact.getText().toString();
                String descContact = descTxtContact.getText().toString();
                //SET DATA
                Contact sContact = new Contact();
                sContact.setNameEditTextContact(nameContact);
                sContact.setDescEditTextContact(descContact);
                //SIMPLE VALIDATION
                if (nameContact != null && nameContact.length() > 0) {
                    //THEN SAVE
                    if (helperContactActivity.saveContact(sContact)) {
                        //IF SAVED CLEAR EDITXT
                        nameEditTxtContact.setText("");
                        descTxtContact.setText("");
                    }
                } else {
                    Toast.makeText(ContactActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }

    @Override
    protected void onDestroy() {
        helperContactActivity.unregisterListener();
        super.onDestroy();
    }

    //Metoda buduje menu z pliku które podaliśmy. Inflate dodaje elemeny do menu jeśli są dostępne
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_refresh_action_bar, menu);
        // getMenuInflater() - Przekształca XML'a menu w faktyczne obiekty jakie są potrzebne a nast. ten element jest dołączany do menu
        // Przygotowuje elementy w menu R.menu.menu_widom_list_activity + dodaje je do już instejącego menu przekazanego jako parametr 'menu"
        return true;
    }

    @Override
    //Wywoływana gdy menu jest kliknięte, przekazywany jest nasz item
    //Obsługa akcji kliknięcia action bar.
    public boolean onOptionsItemSelected(MenuItem item) {
        //Na podstawie id rozpoznajemy co zostało klikniete
        int id = item.getItemId();

        //Sprawdzamy co zostało kliknięte w menu actionBar
        if (id == R.id.action_refresh_contacts) {
            adapterContactActivity.notifyDataSetChanged();
        } else if (id == R.id.action_add_contacts){
            displayInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
