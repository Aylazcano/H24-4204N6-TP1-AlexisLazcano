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
                            // TODO: ameliorer le message (snack, dialogue?)
                            try {
                                String corpsErreur = response.errorBody().string();
                                Log.i("RETROFIT", "le code " + response.code());
                                Log.i("RETROFIT", "le message " + response.message());
                                Log.i("RETROFIT", "le corps " + corpsErreur);
                                Log.i("RETROFIT", "le corps encore " + response.errorBody().string());
                                if (corpsErreur.contains("InternalAuthenticationServiceException")) {
                                    // TODO remplacer par un objet graphique mieux qu'un toast
                                    Toast.makeText(ConnexionActivity.this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                                }
                                if (corpsErreur.contains("BadCredentialsException")) {
                                    // TODO remplacer par un objet graphique mieux qu'un toast
                                    Toast.makeText(ConnexionActivity.this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
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
                        // TODO: Message d'erreur code 500 a l'Utilisateur: Erreur de connection au serveur
                        // Code 500: Erreur de connection serveur
                        Log.i("RETROFIT", t.getMessage() + " service.signin(signinRequest) onFailure");
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