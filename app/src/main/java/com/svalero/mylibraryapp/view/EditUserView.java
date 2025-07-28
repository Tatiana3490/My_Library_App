package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.api.BooksApi;
import com.svalero.mylibraryapp.api.BooksApiInterface;
import com.svalero.mylibraryapp.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserView extends AppCompatActivity {

    private EditText etUserName, etUserUsername,etPassword, etUserEmail;
    private Button btnSaveUser;
    private BooksApiInterface api;
    private User user;
    private Switch switchActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_view);

        etUserName = findViewById(R.id.et_user_name);
        etUserUsername = findViewById(R.id.et_user_username);
        etPassword = findViewById(R.id.et_password);
        etUserEmail = findViewById(R.id.et_user_email);
        switchActive = findViewById(R.id.switch_active);
        btnSaveUser = findViewById(R.id.btn_save_user);
        api = BooksApi.buildInstance();

        user = getIntent().getParcelableExtra("user");

        if (user != null) {
            etUserName.setText(user.getName());
            etUserUsername.setText(user.getUsername());
            etUserEmail.setText(user.getEmail());
            switchActive.setChecked(user.isActive());

            btnSaveUser.setOnClickListener(v -> saveUser());
        }
    }

    private void saveUser() {

        String newPassword = etPassword.getText().toString().trim();
        user.setName(etUserName.getText().toString());
        user.setUsername(etUserUsername.getText().toString());
        user.setEmail(etUserEmail.getText().toString());
        user.setActive(switchActive.isChecked());


        if (!newPassword.isEmpty()) {
            user.setPassword(newPassword);
        }

        Call<User> call = api.updateUser(user.getId(), user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditUserView.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("user", user);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditUserView.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditUserView.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
