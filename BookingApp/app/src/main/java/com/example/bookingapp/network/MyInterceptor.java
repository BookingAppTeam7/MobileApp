package com.example.bookingapp.network;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class MyInterceptor implements Interceptor {

    private final Context context;

    public MyInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Dobijanje vrednosti iz SharedPreferences
       // String token = preferences.getString("role", "");

        // Dodajte header prema potrebama
        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + preferences)
                .build();

        return chain.proceed(modifiedRequest);
    }
}
