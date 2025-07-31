package com.svalero.mylibraryapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svalero.mylibraryapp.domain.User;

import java.time.LocalDate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksApi {

    private static BooksApiInterface api;

    public static BooksApiInterface buildInstance() {
        if (api == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class,
                            (com.google.gson.JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                                    new com.google.gson.JsonPrimitive(src.toString()))
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.16:8082/") // o la IP real
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            api = retrofit.create(BooksApiInterface.class);
        }

        return api;
    }
}

/*http://10.0.2.2:8082/*/
/*http://192.168.0.16:8082/*/