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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

import com.example.tp1clientandroid.databinding.ActivityConnexionBinding;
import com.example.tp1clientandroid.http.RetrofitUtil;
import com.example.tp1clientandroid.http.Service;

import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnexionActivity extends AppCompatActivity {
    private ActivityConnexionBinding binding;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonSignUp;
    private SigninRequest signinRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.connexion_activity_title);

        // Recherche des éléments de la vue
        editTextUsername = binding.editTextUsername;
        editTextPassword = binding.editTextPassword;
        buttonSignIn = binding.buttonSignIn;
        buttonSignUp = binding.buttonSignUp;

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrofit: SigninRequest
                signinRequest = new SigninRequest();
                signinRequest.username = editTextUsername.getText().toString();
                signinRequest.password = editTextPassword.getText().toString();

                // Retrofit: service Retrofit pour initaliser la connection
                final Service service = RetrofitUtil.get();
                service.signin(signinRequest).enqueue(new Callback<SigninResponse>() {
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
                                if (corpsErreur.contains("InternalAuthenticationServiceException")) {
                                    Snackbar.make(binding.getRoot(), R.string.invalid_credentials, Snackbar.LENGTH_SHORT).show();
                                }
                                if (corpsErreur.contains("BadCredentialsException")) {
                                    Snackbar.make(binding.getRoot(), R.string.invalid_credentials, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }else{
                            SigninResponse resultat = response.body();
                            Log.i("RETROFIT", resultat.username + " est connecté!");
                            UserManager.getInstance().setUsername(resultat.username);
                            Toast.makeText(ConnexionActivity.this, getString(R.string.valid_credentials) + " " + resultat.username + "!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConnexionActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<SigninResponse> call, Throwable t) {
                        // Code 500: Erreur de connection serveur
                        Log.i("RETROFIT", t.getMessage() + " service.signin(signinRequest) onFailure");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
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

       buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
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