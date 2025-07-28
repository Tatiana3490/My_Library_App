package com.svalero.mylibraryapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svalero.mylibraryapp.util.LocalDateAdapter;

import java.time.LocalDate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksApi {
    public static BooksApiInterface buildInstance() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/") // URL base (localhost del emulador)
                .addConverterFactory(GsonConverterFactory.create(gson)) // Usa el Gson personalizado
                .build();

        return retrofit.create(BooksApiInterface.class);
    }
}
