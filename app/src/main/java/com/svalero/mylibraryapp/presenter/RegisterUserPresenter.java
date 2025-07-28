package com.svalero.mylibraryapp.presenter;

import com.svalero.mylibraryapp.contract.RegisterUserContract;
import com.svalero.mylibraryapp.domain.User;
import com.svalero.mylibraryapp.model.RegisterUserModel;

public class RegisterUserPresenter implements RegisterUserContract.Presenter, RegisterUserContract.Model.OnRegisterUserListener{

    private RegisterUserContract.Model model;
    private RegisterUserContract.View view;

    public RegisterUserPresenter(RegisterUserContract.View view) {
        this.view = view;
        this.model = new RegisterUserModel();
    }

    @Override
    public void registerUser(User user) {
        if (user.getName().isEmpty()) {
            view.showErrorMessage("El nombre del usuario no puede estar vacio");
            return;
        }
        model.registerUser(user, this);
    }

    @Override
    public void onRegisterUserSuccess(User registeredUser) {
        view.showSuccessMessage("Usuario registrado correctamente con el identificador " + registeredUser.getId());
    }

    @Override
    public void onRegisterUserError(String message) {
        view.showErrorMessage(message);
    }
}
