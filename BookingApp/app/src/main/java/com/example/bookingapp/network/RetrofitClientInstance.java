package com.example.bookingapp.network;


import android.content.Context;

import com.example.bookingapp.BuildConfig;
import com.example.bookingapp.model.TokenManager;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://"+ BuildConfig.IP_ADDR+":8084/";



    public static Response executeRequest() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL)
              //  .addHeader("Authorization", "Bearer your_token")  // Dodajte header direktno na nivou zahteva
                .build();

        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Retrofit getRetrofitInstance() {
        Context context = MyApplication.getAppContext();

        if (retrofit == null) {

//            // Kreiranje interceptor-a
//            MyInterceptor myInterceptor = new MyInterceptor(context);
//
//            // Kreiranje OkHttp klijenta
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(myInterceptor)
//                    // Dodajte druge interceptore ili postavke prema potrebi


//                    .build();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + TokenManager.getJwtToken()) // Dodajte vaš JWT token ovde
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    })
                    .build();

             retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

          //  UserService userService = retrofit.create(UserService.class);

//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                 //   .client(client)
//                    .build();
        }
        return retrofit;
    }



//    public static Retrofit getRetrofitInstance() {
//        OkHttpClient client = createClient(context);
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }

}
