package com.svalero.mylibraryapp.model;

import android.content.Context;

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

                    new Thread(() -> {
                        try {
                            AppDatabase db = AppDatabase.getInstance(context);
                            BookDao bookDao = db.bookDao();

                            bookDao.deleteAll();
                            for (Book book : apiBooks) {
                                bookDao.insert(book);
                            }

                            List<Book> localBooks = bookDao.getAllBooks();
                            listener.onLoadBooksSuccess(localBooks);

                        } catch (Exception e) {
                            listener.onLoadBooksError("Error al guardar datos: " + e.getMessage());
                        }
                    }).start();

                } else if (response.code() == 500) {
                    listener.onLoadBooksError("La API no está disponible.");
                } else {
                    listener.onLoadBooksError("Código de error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                new Thread(() -> {
                    try {
                        AppDatabase db = AppDatabase.getInstance(context);
                        BookDao bookDao = db.bookDao();
                        List<Book> localBooks = bookDao.getAllBooks();

                        if (localBooks.isEmpty()) {
                            listener.onLoadBooksError("No hay conexión y no hay datos locales.");
                        } else {
                            listener.onLoadBooksSuccess(localBooks);

                            if (listener instanceof BookListContract.Presenter) {
                                ((BookListContract.Presenter) listener).onOfflineMode();
                            }
                        }
                    } catch (Exception e) {
                        listener.onLoadBooksError("Error local: " + e.getMessage());
                    }
                }).start();
            }
        });
    }
}
