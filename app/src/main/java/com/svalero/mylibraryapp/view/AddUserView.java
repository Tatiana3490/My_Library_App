package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.domain.User;
import com.svalero.mylibraryapp.presenter.RegisterUserPresenter;
import com.svalero.mylibraryapp.util.DateUtil;

import java.text.ParseException;
import java.util.Date;

public class AddUserView extends AppCompatActivity implements RegisterUserContract.View{
    private EditText etUserName, etUserUsername, etPassword, etUserEmail, etCreationDate, etActive;
    private Switch switchActive;
    private Button btnRegisterUser;
    private RegisterUserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etUserName = findViewById(R.id.et_user_name);
        etUserUsername = findViewById(R.id.et_user_username);
        etPassword = findViewById(R.id.et_password);
        etUserEmail = findViewById(R.id.et_user_email);
        etCreationDate = findViewById(R.id.et_creationDate);
        switchActive = findViewById(R.id.switch_active);
        btnRegisterUser = findViewById(R.id.btn_register_user);

        presenter = new RegisterUserPresenter(this);

        btnRegisterUser.setOnClickListener(this::registerUser);
    }

    public void registerUser(View view) {
        try {
            String name = etUserName.getText().toString().trim();
            String username = etUserUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etUserEmail.getText().toString().trim();
            String dateText = etCreationDate.getText().toString().trim();
            boolean active = switchActive.isChecked();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || dateText.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
                return;
            }

            Date creationDate;
            try {
                creationDate = DateUtil.format(dateText);
            } catch (ParseException pe) {
                Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_LONG).show();
                return;
            }

            User user = new User(name, email, password, creationDate, active);
            presenter.registerUser(user);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al procesar el registro", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        Log.d("DEBUG", "Enviando RESULT_OK desde AddUserView antes de cerrar");

        // Crear Intent para enviar resultado
        Intent resultIntent = new Intent();
        resultIntent.putExtra("userAdded", true);
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
