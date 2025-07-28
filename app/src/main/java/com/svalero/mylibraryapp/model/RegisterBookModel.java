package com.svalero.mylibraryapp.model;


import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.google.gson.Gson;
import com.svalero.mylibraryapp.api.BooksApi;
import com.svalero.mylibraryapp.api.BooksApiInterface;
import com.svalero.mylibraryapp.contract.RegisterBookContract;
import com.svalero.mylibraryapp.db.AppDatabase;
import com.svalero.mylibraryapp.domain.Book;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterBookModel implements RegisterBookContract.Model {
    private Context context;

    public RegisterBookModel(Context context) {
        this.context = context;
    }

    @Override
    public void registerBook(Book book, RegisterBookContract.Model.OnRegisterBookListener listener) {
        // Mostrar la petición JSON en log
        Gson gson = new Gson();
        String json = gson.toJson(book);
        Log.d("API_REQUEST", "JSON enviado: " + json);

        // Crear instancia de la API
        BooksApiInterface booksApi = BooksApi.buildInstance();
        Log.d("RegisterBookModel", "Libro a registrar: " + book.toString());

        // Realizar llamada POST para registrar el libro
        Call<Book> callRegisterBook = booksApi.addBook(book);
        callRegisterBook.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                switch (response.code()) {
                    case 201:
                        // Respuesta exitosa: guardar también en Room
                        Book apiBook = response.body();

                        AppDatabase db = Room.databaseBuilder(
                                        context, AppDatabase.class, "mylibraryapp-db")
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build();

                        db.bookDao().insert(apiBook);
                        listener.onRegisterBookSuccess(apiBook);
                        break;

                    case 400:
                        listener.onRegisterBookError("Error validando la petición: " + response.message());
                        break;

                    case 500:
                        listener.onRegisterBookError("Error interno en la API: " + response.message());
                        break;

                    default:
                        listener.onRegisterBookError("Error invocando a la API: " + response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                listener.onRegisterBookError("No se pudo conectar con el origen de datos. " +
                        "Comprueba la conexión e inténtalo otra vez");
            }
        });
    }
}


