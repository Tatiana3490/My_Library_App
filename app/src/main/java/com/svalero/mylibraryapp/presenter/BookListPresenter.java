package com.svalero.mylibraryapp.presenter;

import android.content.Context;

import com.svalero.mylibraryapp.contract.BookListContract;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.model.BookListModel;

import java.util.List;

public class BookListPresenter implements BookListContract.Presenter, BookListContract.Model.OnLoadBooksListener{

    private BookListContract.View view;
    private BookListContract.Model model;

    public BookListPresenter(BookListContract.View view, Context context){
        this.view = view;
        model = new BookListModel(context);
    }

    @Override
    public void loadBooks(){
        model.loadBooks((this));
    }

    @Override
    public void onLoadBooksSuccess(List<Book> bookList){
        view.listBooks(bookList);
    }

    @Override
    public void onLoadBooksError(String message) { view.showErrorMessage(message);}

    @Override
    public void onOfflineMode() {
        view.showOfflineMessage();
    }
}
