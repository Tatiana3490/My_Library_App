package com.svalero.mylibraryapp.presenter;

import android.content.Context;

import com.svalero.mylibraryapp.contract.BookListContract;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.model.BookListModel;

import java.util.List;

/**
 * Presentador de la lista de libros.
 * Implementa la lógica para conectar la vista con el modelo (API y Room).
 */
public class BookListPresenter implements BookListContract.Presenter,
        BookListContract.Model.OnLoadBooksListener {

    // Referencia a la interfaz de la vista
    private BookListContract.View view;

    // Referencia al modelo (que maneja la API y la BD)
    private BookListContract.Model model;

    /**
     * Constructor: recibe la vista (Activity o Fragment) y el contexto para el modelo.
     */
    public BookListPresenter(BookListContract.View view, Context context) {
        this.view = view;
        this.model = new BookListModel(context);
    }

    /**
     * Método llamado desde la vista para iniciar la carga de libros.
     * Pide al modelo que obtenga los datos.
     */
    @Override
    public void loadBooks() {
        model.loadBooks(this); // `this` porque implementa el listener
    }

    /**
     * Callback de éxito: recibe la lista de libros desde el modelo.
     * Notifica a la vista para que los muestre.
     */
    @Override
    public void onLoadBooksSuccess(List<Book> bookList) {
        view.listBooks(bookList);
    }

    /**
     * Callback de error: muestra el mensaje de error en la vista.
     */
    @Override
    public void onLoadBooksError(String message) {
        view.showErrorMessage(message);
    }

    /**
     * Callback cuando no hay red y se usan datos locales.
     */
    @Override
    public void onOfflineMode() {
        view.showOfflineMessage();
    }
}
