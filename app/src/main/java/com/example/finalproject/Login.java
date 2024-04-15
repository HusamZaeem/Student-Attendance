package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;

    SharedPreferences sharedPreferences;

    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DbHelper(getApplicationContext());
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        binding.tvNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Register.class));
                finish();

            }
        });


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (username.isEmpty()) {
                    binding.etUsername.setError(getText(R.string.usernameEmpty));
                }

                if (password.length() == 0) {
                    binding.etPassword.setError(getText(R.string.passwordEmpty));
                }





                boolean isAdmin = dbHelper.checkAdmin(username, password);

                if (isAdmin) {
                    if (binding.cbRememberMe.isChecked()) {

                        dbHelper.updateLoggedInFlag(username, true);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putBoolean("rememberMe", true);
                        editor.apply();
                    } else {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.remove("rememberMe");
                        editor.apply();
                    }

                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();

                    if (!binding.cbRememberMe.isChecked()){
                        dbHelper.updateLoggedInFlag(username, true);
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check username and password", Toast.LENGTH_LONG).show();
                }




            }
        });

        if (sharedPreferences.getBoolean("rememberMe", false)) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");
            // Pre-fill the login fields
            binding.etUsername.setText(savedUsername);
            binding.etPassword.setText(savedPassword);
            binding.cbRememberMe.setChecked(true);
        }

    }


}


