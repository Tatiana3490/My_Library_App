package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.contract.RegisterUserContract;
import com.svalero.mylibraryapp.domain.User;
import com.svalero.mylibraryapp.presenter.RegisterUserPresenter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddUserView extends AppCompatActivity implements RegisterUserContract.View {

    // Elementos del formulario
    private EditText etUserName;
    private EditText etUserUsername;
    private EditText etPassword;
    private EditText etUserEmail;
    private EditText etCreationDate;
    private Switch switchActive;
    private Button btnRegisterUser;

    private RegisterUserPresenter presenter;

    // Este formato debe coincidir con lo que espera el backend: yyyy-MM-dd
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Inicializamos todos los campos del formulario
        etUserName = findViewById(R.id.et_user_name);
        etUserUsername = findViewById(R.id.et_user_username);
        etPassword = findViewById(R.id.et_password);
        etUserEmail = findViewById(R.id.et_user_email);
        etCreationDate = findViewById(R.id.et_creationDate);
        switchActive = findViewById(R.id.switch_active);
        btnRegisterUser = findViewById(R.id.btn_register_user);
        LocalDate today = LocalDate.now();
        etCreationDate.setText(today.format(dateFormatter)); /*Para que la fecha salga la actual del dia*/
       /* Log.d("DEBUG", "Fecha automática: " + etCreationDate.getText().toString());*/

        presenter = new RegisterUserPresenter(this);

        // Evento de click del botón
        btnRegisterUser.setOnClickListener(this::registerUser);
    }

    public void registerUser(View view) {
        // Obtenemos y limpiamos los valores del formulario
        String name = etUserName.getText().toString().trim();
        String username = etUserUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etUserEmail.getText().toString().trim();
        String dateText = etCreationDate.getText().toString().trim();
        boolean active = switchActive.isChecked();

        // Validación básica
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || dateText.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            return;
        }

        //Validación email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Correo electrónico no válido", Toast.LENGTH_LONG).show();
            return;
        }


        // Creamos el usuario y lo enviamos al presenter
        User user = new User(name, username, password, email, dateText, active);
        // Mostrar el JSON enviado en Logcat
        Gson gson = new Gson();
        Log.d("DEBUG", "Enviando JSON: " + gson.toJson(user));
        presenter.registerUser(user);

    }

    // --- Métodos que recibe desde el presenter si fue bien o mal ---
    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", "Usuario creado correctamente. Cerrando vista.");

        // Devolvemos resultado OK
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
