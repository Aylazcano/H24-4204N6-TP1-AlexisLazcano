package com.example.tp1clientandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

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
    private ProgressBar progressBar;

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
        progressBar = binding.progressBar;

        // Affichage de l'icône de menu et interaction
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Désactiver le bouton pendant l'envoi de la requête
                buttonSingUp.setEnabled(false);

                // Afficher l'indicateur de progression
                progressBar.setVisibility(View.VISIBLE);

                // Retrofit: SignupResquest
                signupRequest = new SignupRequest();
                signupRequest.username = editTextUsername.getText().toString();
                signupRequest.password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (!signupRequest.password.equals(confirmPassword)){
                    // Réactiver le bouton
                    buttonSingUp.setEnabled(true);

                    // Masquer l'indicateur de progression
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(InscriptionActivity.this, R.string.confirm_password_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Retrofit: service Retrofit pour initaliser la connection
                final Service service = RetrofitUtil.get();
                service.signup(signupRequest).enqueue(new Callback<SigninResponse>() {
                    @Override
                    public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                        // Réactiver le bouton
                        buttonSingUp.setEnabled(true);

                        // Masquer l'indicateur de progression
                        progressBar.setVisibility(View.GONE);

                        if (!response.isSuccessful()){
                            // Code erreur http 400 404
                            try {
                                String corpsErreur = response.errorBody().string();
                                Log.i("RETROFIT", "le code " + response.code());
                                Log.i("RETROFIT", "le message " + response.message());
                                Log.i("RETROFIT", "le corps " + corpsErreur);
                                Log.i("RETROFIT", "le corps encore " + response.errorBody().string());
                                if (corpsErreur.contains("UsernameTooShort")) {
                                    Snackbar.make(binding.getRoot(), R.string.username_too_short, Snackbar.LENGTH_SHORT).show();
                                }
                                if (corpsErreur.contains("PasswordTooShort")) {
                                    Snackbar.make(binding.getRoot(), R.string.password_too_short, Snackbar.LENGTH_SHORT).show();
                                }
                                if (corpsErreur.contains("UsernameAlreadyTaken")) {
                                    Snackbar.make(binding.getRoot(), R.string.username_already_taken, Snackbar.LENGTH_SHORT).show();
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
                        // Réactiver le bouton
                        buttonSingUp.setEnabled(true);

                        // Masquer l'indicateur de progression
                        progressBar.setVisibility(View.GONE);

                        // Code 500: Erreur de connection serveur
                        Log.i("RETROFIT", t.getMessage() + "service.signup(signupRequest) onFailure");
                        AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionActivity.this);
                        builder.setTitle(R.string.error_dialog_title)
                                .setMessage(R.string.error_network)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Fermer le dialogue
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(R.drawable.error_icon)
                                .show();
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