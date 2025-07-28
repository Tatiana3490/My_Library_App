package com.svalero.mylibraryapp.presenter;

import android.content.Context;

import com.svalero.mylibraryapp.contract.RegisterBookContract;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.model.RegisterBookModel;

public class RegisterBookPresenter implements RegisterBookContract.Presenter,
        RegisterBookContract.Model.OnRegisterBookListener {

    private RegisterBookContract.Model model;
    private RegisterBookContract.View view;

    // Constructor recibe contexto de forma explícita para evitar casting frágil
    public RegisterBookPresenter(RegisterBookContract.View view, Context context) {
        this.view = view;
        this.model = new RegisterBookModel(context);
    }

    // Valida que el título no esté vacío antes de enviar al modelo
    @Override
    public void registerBook(Book book) {
        if (book.getTitle().isEmpty()) {
            view.showErrorMessage("El campo del título no puede estar vacío");
            return;
        }

        model.registerBook(book, this);  // Callback hacia el modelo
    }

    // Se ejecuta si el registro fue exitoso
    @Override
    public void onRegisterBookSuccess(Book registeredBook) {
        view.showSuccessMessage("El libro se ha registrado correctamente con el identificador: " + registeredBook.getId());
    }

    // Se ejecuta si hay error en el modelo
    @Override
    public void onRegisterBookError(String message) {
        view.showErrorMessage(message);
    }
}
