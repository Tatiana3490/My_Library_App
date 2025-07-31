package com.svalero.mylibraryapp.model;

import android.content.Context;
import android.util.Log;

import com.svalero.mylibraryapp.api.BooksApi;
import com.svalero.mylibraryapp.api.BooksApiInterface;
import com.svalero.mylibraryapp.contract.BookListContract;
import com.svalero.mylibraryapp.db.AppDatabase;
import com.svalero.mylibraryapp.db.BookDao;
import com.svalero.mylibraryapp.domain.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListModel implements BookListContract.Model {

    private final Context context;

    public BookListModel(Context context) {
        this.context = context;
    }

    @Override
    public void loadBooks(OnLoadBooksListener listener) {
        BooksApiInterface booksApi = BooksApi.buildInstance();
        Call<List<Book>> getBooksCall = booksApi.getAllBooks(null, null, null);

        getBooksCall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<Book> apiBooks = response.body();

                    // Guardamos libros en Room en hilo separado
                    new Thread(() -> {
                        try {
                            AppDatabase db = AppDatabase.getInstance(context);
                            BookDao bookDao = db.bookDao();

                            // Actualización segura: borrar todo y meter lo nuevo
                            bookDao.deleteAll();
                            bookDao.insertAll(apiBooks);

                            // Obtenemos desde base local para asegurarnos
                            List<Book> localBooks = bookDao.getAllBooks();
                            listener.onLoadBooksSuccess(localBooks);

                        } catch (Exception e) {
                            Log.e("BookListModel", "Error guardando en Room", e);
                            listener.onLoadBooksError("Error al guardar datos: " + e.getMessage());
                        }
                    }).start();

                } else if (response.code() == 500) {
                    listener.onLoadBooksError("La API está en huelga (500).");
                } else {
                    listener.onLoadBooksError("Código de error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                // Si la API falla, tiramos de la copia local (si existe)
                new Thread(() -> {
                    try {
                        AppDatabase db = AppDatabase.getInstance(context);
                        BookDao bookDao = db.bookDao();
                        List<Book> localBooks = bookDao.getAllBooks();

                        if (localBooks == null || localBooks.isEmpty()) {
                            listener.onLoadBooksError("No hay conexión y tampoco libros locales.");
                        } else {
                            listener.onOfflineMode(); // Ahora lo llamamos correctamente
                            listener.onLoadBooksSuccess(localBooks);
                        }
                    } catch (Exception e) {
                        Log.e("BookListModel", "Error accediendo a Room", e);
                        listener.onLoadBooksError("Error local inesperado: " + e.getMessage());
                    }
                }).start();
            }
        });
    }
}
