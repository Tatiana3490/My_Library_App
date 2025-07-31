package com.svalero.mylibraryapp.contract;

import com.svalero.mylibraryapp.domain.Book;

import java.util.List;

public interface BookListContract {

    interface Model {
        interface OnLoadBooksListener {
            void onLoadBooksSuccess(List<Book> bookList); //Cuando la API responde como debe y te da libros.
            void onLoadBooksError(String message); //Cuando la API decide enviar errores
            void onOfflineMode(); //Cuando en el movil no hay conexión
        }
        void loadBooks(OnLoadBooksListener listener); //para llamar a la API
    }


    interface View{
        void listBooks(List<Book> bookList); //Para listar los libros. Pasa los libros al adapter y da scroll.
        void showErrorMessage(String message); // muestra un Toast, Snackbar
        void showSuccessMessage(String message); //cuando funciona
        void showOfflineMessage(); //cuando no hay red

    }

    interface Presenter{
        void loadBooks(); //llama la vista, y delega al modelo.
        void onOfflineMode();
    }
}
