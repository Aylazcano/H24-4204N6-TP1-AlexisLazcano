package com.example.tp1clientandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tp1clientandroid.databinding.ActivityInscriptionBinding;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;

//    private EditText editUsername;
//    private EditText editPassword;
//    private EditText editConfirmPassword;
//    private Button buttonSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.inscription_activity_title);

        // Recherche des éléments de la vue
//        editUsername = findViewById(R.id.edit_text_username);
//        editPassword = findViewById(R.id.edit_text_password);
//        editConfirmPassword = findViewById(R.id.edit_text_confirm_password);
//        buttonSignUp = findViewById(R.id.button_sign_up);

        EditText editTextUsername = binding.editTextUsername;
        EditText editTextPassword = binding.editTextPassword;
        EditText editTextConfirmPassword = binding.editTextConfirmPassword;
        Button buttonSingUp = binding.buttonSignUp;




        // Affichage de l'icône de menu et interaction
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (!password.equals(confirmPassword)){
                    Toast.makeText(InscriptionActivity.this, R.string.confirm_password_error, Toast.LENGTH_SHORT).show();
                    return;
                }



                Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}