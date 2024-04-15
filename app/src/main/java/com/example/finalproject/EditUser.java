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
import com.example.finalproject.databinding.ActivityEditUserBinding;

public class EditUser extends AppCompatActivity {

    ActivityEditUserBinding binding;

    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DbHelper(this);

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


        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldName = dbHelper.getLoggedInUsername();
                String username = binding.etUsername.getText().toString();
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                boolean check = true;

                if (username.isEmpty()) {
                    binding.etUsername.setError(getText(R.string.usernameEmpty));
                    check = false;
                } else if (username.length() < 4) {
                    binding.etUsername.setError(getText(R.string.usernameChNumber));
                    check = false;
                }

                if (email.isEmpty()) {
                    binding.etEmail.setError(getText(R.string.emailEmpty));
                    check = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.setError(getText(R.string.invalidEmail));
                    binding.etEmail.setTextColor(ContextCompat.getColor(EditUser.this, R.color.red));
                    check = false;
                } else {
                    binding.etEmail.setTextColor(ContextCompat.getColor(EditUser.this, R.color.white));
                }

                if (binding.cbPasswordNumber.isChecked() && binding.cbPasswordlength.isChecked() && binding.cbPasswordCharacter.isChecked() && check) {
                    if (dbHelper.updateAdmin(oldName,username, email, password)) {
                        Toast.makeText(getApplicationContext(), "Admin data updated successfully. Please log in again.", Toast.LENGTH_LONG).show();

                        // Redirect the user to the login screen or perform any relevant action
                        Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                    }
                } else {
                    binding.etPassword.setError(getText(R.string.invalidPassword));
                }



            }
        });


    }
}