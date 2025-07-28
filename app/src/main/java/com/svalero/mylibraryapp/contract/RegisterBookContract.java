package com.svalero.mylibraryapp.contract;

import com.svalero.mylibraryapp.domain.Book;

public interface RegisterBookContract {
    interface Model {
        interface OnRegisterBookListener {
            void onRegisterBookSuccess (Book registeredBook);
            void onRegisterBookError(String message);
        }
        void registerBook (Book book, OnRegisterBookListener listener);
    }

    interface View {
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void registerBook(Book book);
    }
}
