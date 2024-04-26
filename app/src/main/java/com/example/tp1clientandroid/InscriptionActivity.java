package com.example.tp1clientandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tp1clientandroid.databinding.ActivityInscriptionBinding;
import com.example.tp1clientandroid.http.RetrofitUtil;
import com.example.tp1clientandroid.http.Service;

import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSingUp;
    private SignupRequest signupRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.inscription_activity_title);

        // Recherche des éléments de la vue
        editTextUsername = binding.editTextUsername;
        editTextPassword = binding.editTextPassword;
        editTextConfirmPassword = binding.editTextConfirmPassword;
        buttonSingUp = binding.buttonSignUp;

        // Affichage de l'icône de menu et interaction
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrofit: SignupResquest
                signupRequest = new SignupRequest();
                signupRequest.username = editTextUsername.getText().toString();
                signupRequest.password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (!signupRequest.password.equals(confirmPassword)){
                    Toast.makeText(InscriptionActivity.this, R.string.confirm_password_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Retrofit: service Retrofit pour initaliser la connection
                final Service service = RetrofitUtil.get();
                service.signup(signupRequest).enqueue(new Callback<SigninResponse>() {
                    @Override
                    public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                        if (!response.isSuccessful()){
                            // Code erreur http 400 404
                            try {
                                String corpsErreur = response.errorBody().string();
                                Log.i("RETROFIT", "le code " + response.code());
                                Log.i("RETROFIT", "le message " + response.message());
                                Log.i("RETROFIT", "le corps " + corpsErreur);
                                Log.i("RETROFIT", "le corps encore " + response.errorBody().string());
                                if (corpsErreur.contains("UsernameTooShort")) {
                                    // TODO remplacer par un objet graphique mieux qu'un toast
                                    Toast.makeText(InscriptionActivity.this, R.string.username_too_short, Toast.LENGTH_SHORT).show();
                                }
                                if (corpsErreur.contains("PasswordTooShort")) {
                                    // TODO remplacer par un objet graphique mieux qu'un toast
                                    Toast.makeText(InscriptionActivity.this, R.string.password_too_short, Toast.LENGTH_SHORT).show();
                                }
                                if (corpsErreur.contains("UsernameAlreadyTaken")) {
                                    // TODO remplacer par un objet graphique mieux qu'un toast
                                    Toast.makeText(InscriptionActivity.this, R.string.username_already_taken, Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            SigninResponse resultat = response.body();
                            Log.i("RETROFIT", response.body().username + " est inscrit!");
                            UserManager.getInstance().setUsername(resultat.username);
                            Toast.makeText(InscriptionActivity.this, getString(R.string.valid_credentials) + " " + resultat.username + "!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<SigninResponse> call, Throwable t) {
                        // TODO: Message d'erreur code 500 a l'Utilisateur: Erreur de connection au serveur
                        // Code 500: Erreur de connection serveur
                        Toast.makeText(InscriptionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("RETROFIT", t.getMessage() + "service.signup(signupRequest) onFailure");
                    }
                });

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