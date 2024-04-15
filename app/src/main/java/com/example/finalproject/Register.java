package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.database.DbHelper;
import com.example.finalproject.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    DbHelper dbHelper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (binding.etUsername.length() < 4) {
                    binding.etUsername.setTextColor(ContextCompat.getColor(Register.this, R.color.red));
                } else
                    binding.etUsername.setTextColor(ContextCompat.getColor(Register.this, R.color.white));


            }
        });


        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String password = binding.etPassword.getText().toString();

                if (binding.etPassword.length() > 7) {
                    binding.cbPasswordlength.setChecked(true);
                } else if (binding.etPassword.length() < 8) {
                    binding.cbPasswordlength.setChecked(false);
                }

                if (password.contains("$") || password.contains("%") || password.contains("*") || password.contains("#") || password.contains("@")) {
                    binding.cbPasswordCharacter.setChecked(true);

                } else binding.cbPasswordCharacter.setChecked(false);

                if (!password.matches(".*[0-9].*")) {
                    binding.cbPasswordNumber.setChecked(false);

                } else binding.cbPasswordNumber.setChecked(true);

            }
        });


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = binding.etUsername.getText().toString();
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                boolean check = false;

                if (username.isEmpty()) {
                    binding.etUsername.setError((getText(R.string.usernameEmpty)));
                    check=false;
                } else if (username.length() < 4) {
                    binding.etUsername.setError(getText(R.string.usernameChNumber));
                    check=false;
                }else check=true;

                if (email.isEmpty()) {
                    binding.etEmail.setError(getText(R.string.emailEmpty));
                    check=false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.setError(getText(R.string.invalidEmail));
                    binding.etEmail.setTextColor(ContextCompat.getColor(Register.this, R.color.red));
                    check=false;
                } else{
                    binding.etEmail.setTextColor(ContextCompat.getColor(Register.this, R.color.white));
                    check=true;
                }



//                if (password.isEmpty()) {
//                    binding.etPassword.setHelperText(getText(R.string.passwordEmpty));
//                } else if (password.length() < 9) {
//
//                    binding.etPassword.setHelperText(getText(R.string.invalidPassword));
//
//                } else if (!password.matches(".*[0-9].*")) {
//                    binding.etPassword.setHelperText(getText(R.string.invalidPassword));
//
//                } else if (password.contains("$") || password.contains("%") || password.contains("*") || password.contains("#") || password.contains("@")){
//
//                    binding.etPassword.setHelperText(getText(R.string.invalidPassword));
//
//
//                }


//                if(password.isEmpty() || password.length() < 9 || !password.matches(".*[0-9].*") || (!password.contains("$") || !password.contains("%") || !password.contains("*") || !password.contains("#") || !password.contains("@")) ){
//                    binding.etPassword.setError(getText(R.string.invalidPassword));

                if (dbHelper.checkAdmin(username, password)) {
                    Toast.makeText(getApplicationContext(), "account already registered", Toast.LENGTH_LONG).show();
                    check=false;
                }

                if (binding.cbPasswordNumber.isChecked() && binding.cbPasswordlength.isChecked() && binding.cbPasswordCharacter.isChecked() && (check=true)) {

                    dbHelper.insertAdmin(username, email, password);
                    Toast.makeText(getApplicationContext(), "successful registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                } else binding.etPassword.setError(getText(R.string.invalidPassword));






            }
        });

    }


}