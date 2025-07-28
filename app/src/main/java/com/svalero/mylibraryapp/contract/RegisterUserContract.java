package com.svalero.mylibraryapp.contract;

import com.svalero.mylibraryapp.domain.User;

public interface RegisterUserContract {

    interface Model {
        interface OnRegisterUserListener {
            void onRegisterUserSuccess (User registeredUser);
            void onRegisterUserError(String message);
        }
        void registerUser (User user, RegisterUserContract.Model.OnRegisterUserListener listener);
    }

    interface View {
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void registerUser(User user);
    }
}
