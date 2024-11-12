package com.example.orderrestaurantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderrestaurantapp.menu.Database;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private final String username = "bon";
    private final String password = "2023";
    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "mypref";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    MaterialButton button_SignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
        if (isLoggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        button_SignIn = findViewById(R.id.login);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username, password);
        editor.apply();
        button_SignIn.setOnClickListener(view -> {
            String username_input = ((TextInputEditText)findViewById(R.id.username)).getText().toString();
            String pass_input = ((TextInputEditText)findViewById(R.id.password)).getText().toString();
            if(username_input.isEmpty() || pass_input.isEmpty()){
                Database.setSnackbar(findViewById(R.id.container), getResources().getString(R.string.forgot_login));
            }else{

                if(username_input.equals(username) && pass_input.equals(password)) {
                    editor.putBoolean(IS_LOGGED_IN, true);
                    editor.apply();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Database.setSnackbar(findViewById(R.id.container), getResources().getString(R.string.error_login));

                }
            }

        });
    }
}
