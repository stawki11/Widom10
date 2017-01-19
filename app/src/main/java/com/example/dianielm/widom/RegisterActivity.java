package com.example.dianielm.widom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmailAdress;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //powrót do LoginActivity

        //Inicjalizacja obiektów
        txtEmailAdress = (EditText) findViewById(R.id.emailRegistration_et);
        txtPassword = (EditText) findViewById(R.id.passwordRegistration_et);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //Metoda onClick linku do rejestracji
    public void btnRegisterationUser_Click(View v) {

        //Wyświetlenie powiadomienia za pomocą dialogu - ze stringów
        final ProgressDialog progressDialog = ProgressDialog.show(RegisterActivity.this,
                getString(R.string.login_reg_please_wait), getString(R.string.login_reg_processing), true);
        (firebaseAuth.createUserWithEmailAndPassword(txtEmailAdress.getText().toString(), txtPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //Jeśli zarejestrowano pomyślnie wyświetl toast
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, (RegisterActivity.this.getString(R.string.proces_register_ok)), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                        //W przeciwnym wypadku pokaz wyjątek
                        else
                        {
                            Log.e("Error", task.getException().toString());
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }


                });
    }

}
