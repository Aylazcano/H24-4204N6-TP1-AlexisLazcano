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
                            Log.i("RETROFIT", response.code() + " service.signup(signupRequest) onResponse");
                        }else{
                            // TODO: Message d'erreur a l'Utilisateur (Retrofit Erreur, Throws)
//                            try {
                                SigninResponse resultat = response.body();
                                Log.i("RETROFIT", response.body().username + " est inscrit!");
                                UserManager.getInstance().setUsername(resultat.username);
                                Toast.makeText(InscriptionActivity.this, getString(R.string.valid_credentials) + " " + resultat.username + "!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
                                startActivity(intent);
//                            } catch (UsernameTooShort e) {
//                                // Gérer le nom d'utilisateur trop court
//                                showErrorDialog("Le nom d'utilisateur est trop court.");
//                            } catch (PasswordTooShort e) {
//                                // Gérer le mot de passe trop court
//                                showErrorDialog("Le mot de passe est trop court.");
//                            } catch (UsernameAlreadyTaken e) {
//                                // Gérer le nom d'utilisateur déjà pris
//                                showErrorDialog("Le nom d'utilisateur est déjà pris.");
//                            }

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