package com.svalero.mylibraryapp.contract;

import com.svalero.mylibraryapp.domain.Book;

import java.util.List;

public interface BookListContract {

    interface Model {
        interface OnLoadBooksListener {
            void onLoadBooksSuccess(List<Book> bookList);
            void onLoadBooksError(String message);
            void onOfflineMode();
        }
        void loadBooks(OnLoadBooksListener listener); //para llamar a la API
    }


    interface View{
        void listBooks(List<Book> bookList); //Para listar los libros
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
        void showOfflineMessage();

    }

    interface Presenter{
        void loadBooks();
        void onOfflineMode();
    }
}
