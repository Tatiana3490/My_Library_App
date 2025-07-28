package com.svalero.mylibraryapp.model;

import android.util.Log;

import com.svalero.mylibraryapp.api.BooksApi;
import com.svalero.mylibraryapp.api.BooksApiInterface;
import com.svalero.mylibraryapp.contract.RegisterUserContract;
import com.svalero.mylibraryapp.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserModel implements RegisterUserContract.Model{

    @Override
    public void registerUser(User user, OnRegisterUserListener listener) {
        BooksApiInterface booksApi = BooksApi.buildInstance();
        Log.d("RegisterBookModel", "Usuario a registrar: " + user.toString());

        Call<User> callRegisterUser = booksApi.addUser(user);
        callRegisterUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()) {
                    case 201:
                        listener.onRegisterUserSuccess(response.body());
                        break;
                    case 400:
                        listener.onRegisterUserError("Error validando la petición: " + response.message());
                        break;
                    case 500:
                        listener.onRegisterUserError("Error interno en la API: " + response.message());
                        break;
                    default:
                        listener.onRegisterUserError("Error invocando a la API: " + response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onRegisterUserError("No se pudo conectar con el origen de datos. " +
                        "Comprueba la conexión e inténtalo otra vez.");
            }
        });
    }
}
