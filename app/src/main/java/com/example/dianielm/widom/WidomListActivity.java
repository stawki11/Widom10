package com.example.dianielm.widom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dianielm.widom.Task.Task;
import com.example.dianielm.widom.Task.TaskFirebaseHelper;
import com.example.dianielm.widom.Task.TaskCustomAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

public class WidomListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference dbTask;
    TaskFirebaseHelper dbTaskHelper;
    TaskCustomAdapter adapterTaskActivity;
    ListView lvTaskActivity;
    EditText nameEditTxtTask, authorEditTextTask, descTxtTask;

    public static final int REQUEST_CODE = 123;
    //TAG'a używamy np gdy chcemy przekazać wiadomość
    private static final String TAG = "WidomListActivity";
    private final int PICK_CONTACT = 1;

    //Metoda budująca wygląd aplikacji
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widom_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button btnShowToken = (Button)findViewById(R.id.button_show_token);
        btnShowToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PRZYCISK DO POBRANIA TOKENA JEST USTAWIONY NA NIEWIDOCZNY! JESLI CHCEMY TOKEN TRZEBA W CONTENT WIDOMLIST USTAWIC NA WIDOCZNY I W NIEGO KLIKNAC
                //Pobierz token dla notyfikacji Firebase
                //Potrzebny, by wysłać notyfikacje do konkretnego urządzenia. Wyświetla się w konsoli i za pomoca toast.
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d(TAG, "Token: " + token);
                Toast.makeText(WidomListActivity.this, token, Toast.LENGTH_SHORT).show();
            }
        });

        lvTaskActivity = (ListView) findViewById(R.id.lv);
        //Adapter
        adapterTaskActivity = new TaskCustomAdapter(this);
        lvTaskActivity.setAdapter(adapterTaskActivity);
        //Inicjializacja bazy danych firebase
        dbTask = FirebaseDatabase.getInstance().getReference();
        dbTaskHelper = new TaskFirebaseHelper(dbTask, new TaskFirebaseHelper.OnTaskLoadedListener() {
            @Override
            public void onTaskLoaded(List<Task> fetchedTasks) {
                adapterTaskActivity.setTasks(fetchedTasks);
                adapterTaskActivity.notifyDataSetChanged();
            }
        });
        dbTaskHelper.registerListeners();
    }

    @Override
    protected void onDestroy() {
        dbTaskHelper.unregisterListeners();
        super.onDestroy();
    }

    //Metoda wywoływana gdy użytkownik wcisną przycisk wstecz
    //Domyślnie kończy działanie bieżącej aktywności
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Metoda buduje menu z pliku które podaliśmy. Inflate dodaje elemeny do menu jeśli są dostępne
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.widom_list_actionbar_menu, menu);
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
        if (id == R.id.action_refrsh) {
            //Odświerzamy adapter = liste zadan
            adapterTaskActivity.notifyDataSetChanged();
        }else if (id == R.id.action_add_task) {
            displayInputDialog();
        }else if (id == R.id.action_logout) {
            // Obsluga akcji akcji logout
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class); // wywolanie logowania
            startActivityForResult(intent, REQUEST_CODE); // REQUEST_CODE dzieki niemu mozemy rozroznic activity
            return true;
        }else if (id == R.id.action_exit){
            //Utworzenie okna dialogowego do wyjscia z aplikacji
            AlertDialog.Builder builder = new AlertDialog.Builder(WidomListActivity.this);
            builder.setMessage(WidomListActivity.this.getString(R.string.action_dialog_exit));
            builder.setCancelable(true);
            builder.setPositiveButton((WidomListActivity.this.getString(R.string.action_dialog_yes)), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    moveTaskToBack(true);
                }
            });
            builder.setNegativeButton((WidomListActivity.this.getString(R.string.action_dialog_no)), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            //Obsluga akcji dodaj zadanie
        }
        return super.onOptionsItemSelected(item);
    }

    //Obsługa rozwijanego menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Podobnie na podstawie id określamy co zostało kliknięte
        int id = item.getItemId();
        //Jeśli paseczki menu - to jest rozsuwamy
        if (id == R.id.nav_widom_list) {
            adapterTaskActivity.notifyDataSetChanged();
        } else if (id == R.id.nav_add_task) {
            displayInputDialog();
            //Jeśli kliknięty chat, odpalamy intencje tego okna
        } else if (id == R.id.nav_chat) {
            Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
            startActivity(intent);
            //Jeśli kliknięte mapy, odpalamy intencje tego okna
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
            //Jeśli kliknięte kontakty, odpalamy intencje tego okna
        } else if (id == R.id.nav_contact) {
//            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//            startActivityForResult(intent, PICK_CONTACT);
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
            //Jeśli kliknięty o nas, tworzymy okno dialogowe, ustalamy tytuł i widok
        } else if (id == R.id.nav_about_us) {
            Dialog dialog = new Dialog(WidomListActivity.this);
            dialog.setTitle("Pokaz tytul");
            //Został utworzony specjalny widok dla okna dialogowego, wyświetlany z metoda show
            dialog.setContentView(R.layout.window_dialog);
            dialog.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Wyświetlenie dialogu do wprowadzenia danych
    private void displayInputDialog() {
        Dialog d = new Dialog(this);
        d.setTitle(R.string.WidomList_add_task_save_database);
        d.setContentView(R.layout.input_dialog_widom_list);

        nameEditTxtTask = (EditText) d.findViewById(R.id.nameEditTextTask);
        authorEditTextTask = (EditText) d.findViewById(R.id.authorEditTextTask);
        descTxtTask = (EditText) d.findViewById(R.id.descEditTextTask);
        Button saveBtnTask = (Button) d.findViewById(R.id.saveBtnTask);
        //Zapisanie - na przycisku svaBtn, wywoływana jest metoda onClickListener
        saveBtnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Pobieranie danych
                String name = nameEditTxtTask.getText().toString();
                String author = authorEditTextTask.getText().toString();
                String desc = descTxtTask.getText().toString();

                //Ustawianie danych
                Task s = new Task();
                s.setNameEditTextTask(name);
                s.setAuthorEditTextTask(author);
                s.setDescEditTextTask(desc);

                //Prosta walidacja
                if (name != null && name.length() > 0) {
                    //Jeśli tak - zapis
                    if (dbTaskHelper.saveTask(s)) {
                        //Jeśli zostało zapisane, wyczyść editText
                        nameEditTxtTask.setText("");
                        authorEditTextTask.setText("");
                        descTxtTask.setText("");
                    }
                } else {
                    Toast.makeText(WidomListActivity.this, R.string.widomList_add_task_not_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }

    //Kiedy został kliknięte kontakty, miała odpalać się ta metoda i wykonywać połączenie na klikniętym kontakcie
    //Obecnie wyświetla tylko jaki element został kliknięty za pomoca toasta
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == ActionBarActivity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        Toast.makeText(this, "You've picked: " + name, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}

