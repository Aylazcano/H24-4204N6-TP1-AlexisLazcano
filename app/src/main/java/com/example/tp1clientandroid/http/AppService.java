package com.example.tp1clientandroid.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.tp1clientandroid.ConnexionActivity;
import com.example.tp1clientandroid.R;
import com.example.tp1clientandroid.http.RetrofitUtil;
import com.example.tp1clientandroid.http.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppService {
    public static void signout(Context context) {
        Service service = RetrofitUtil.get();
        service.signout().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Log.i("RETROFIT", "service.signout(): " + response.code());
                } else {
                    Toast.makeText(context, R.string.logout, Toast.LENGTH_SHORT).show();
                    Log.i("RETROFIT", "Utilisateur déconnecté");
                    Intent intent = new Intent(context, ConnexionActivity.class);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle logout failure
                Log.i("RETROFIT", "service.signout() onFailure: " + t.getMessage());
            }
        });
    }
}
