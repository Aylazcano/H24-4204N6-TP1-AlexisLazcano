package com.example.tp1clientandroid.http;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitUtil {

    private static Service instance;

    public static Service get(){
        if(instance == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client())
                    .baseUrl("http://10.0.2.2:8080/")
                    // Déploiement du code serveur sur une instance cloud
//                    .baseUrl("https://kickmyb-server-ayl.onrender.com/")
                    .build();
            instance = retrofit.create(Service.class);
        }
        return instance;
    }

    private static OkHttpClient client() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CookieJar jar = new SessionCookieJar();
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(jar)
                .addInterceptor(interceptor)
                .build();
        return client;

    }

}
