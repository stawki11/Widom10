package com.example.dianielm.widom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.dianielm.widom.R.*;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmailLogin;
    private EditText editTextPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        editTextEmailLogin = (EditText) findViewById(id.emailLogin_et);
        editTextPassword = (EditText) findViewById(id.passwordLogin_et);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    //Link rejestracji
    public void link_Registration_Click(View v) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }
    

    //Metoda onClick na przycisku zaloguj
    public void btnUserLogin_Click(View v) {
        //Powiadomienie za pomocą dialogu ze stringów
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this,
                getString(string.login_reg_please_wait), getString(string.login_reg_processing), true);

        (firebaseAuth.signInWithEmailAndPassword(editTextEmailLogin.getText().toString(), editTextPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //Jeśli zalogowano poprawnie wyślij wiadomość
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, (LoginActivity.this.getString(string.proces_login_ok)), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, WidomListActivity.class);
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);
                            //W przeciwnym wypadku wyślij exception
                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

}
