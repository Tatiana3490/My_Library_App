package com.svalero.mylibraryapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
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
                            (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                                    new JsonPrimitive(src.toString()))
                    .registerTypeAdapter(LocalDate.class,
                            (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                                    LocalDate.parse(json.getAsString()))
                    .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.16:8082/") // La IP de mi equipo
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            api = retrofit.create(BooksApiInterface.class);
        }

        return api;
    }
}

/*http://10.0.2.2:8082/*/
/*http://192.168.0.16:8082/*/