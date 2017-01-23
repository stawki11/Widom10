package com.example.dianielm.widom;

import android.*;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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

public class ContactActivity extends AppCompatActivity implements PermissionChief {
    private static final int CALL_PHONE_REQUEST = 18852; //unique request id

    DatabaseReference dbContact;
    ContactFirebaseHelper helperContactActivity;
    ContactCustomAdapter adapterContactActivity;
    ListView lvContactActivity;
    EditText nameEditTxtContact, numberTxtContact;

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

        if (!hasNeededPermissions()) {
            requestPermission();
        }
    }

    @Override
    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(this)
                    .setMessage("Dostęp do telefonu jest niezbędny do działania aplikacji")
                    .setPositiveButton("Nadaj uprawnienia", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(ContactActivity.this, new String[] {android.Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST);
                }
            })
                    .setNegativeButton("Anuluj", null)
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST);
        }
    }
    @Override
    public boolean hasNeededPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    //DISPLAY INPUT DIALOG
    private void displayInputDialog() {
        Dialog d = new Dialog(this);
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.input_dialog_contact);
        nameEditTxtContact = (EditText) d.findViewById(R.id.nameEditTextContact);
        numberTxtContact = (EditText) d.findViewById(R.id.numberEditTextContact);
        Button saveBtnContact = (Button) d.findViewById(R.id.saveBtnContact);
        //SAVE
        saveBtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String nameContact = nameEditTxtContact.getText().toString();
                String descContact = numberTxtContact.getText().toString();
                //SET DATA
                Contact sContact = new Contact();
                sContact.setNameEditTextContact(nameContact);
                sContact.setNumberEditTextContact(descContact);
                //SIMPLE VALIDATION
                if (nameContact != null && nameContact.length() > 0) {
                    //THEN SAVE
                    if (helperContactActivity.saveContact(sContact)) {
                        //IF SAVED CLEAR EDITXT
                        nameEditTxtContact.setText("");
                        numberTxtContact.setText("");
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
