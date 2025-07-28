package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.api.BooksApi;
import com.svalero.mylibraryapp.api.BooksApiInterface;
import com.svalero.mylibraryapp.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailView extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail, tvUserActive, tvCreationDate;
    private Button btnEditUser, btnDeleteUser;
    private User user;
    private BooksApiInterface api;
    private static final int REQUEST_EDIT_USER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        tvUserName = findViewById(R.id.tv_user_name);
        tvUserEmail = findViewById(R.id.tv_user_email);
        tvUserActive = findViewById(R.id.tv_user_active);
        tvCreationDate = findViewById(R.id.tv_creation_date);
        btnEditUser = findViewById(R.id.btn_edit_user);
        btnDeleteUser = findViewById(R.id.btn_delete_user);

        api = BooksApi.buildInstance();

        user = getIntent().getParcelableExtra("user");

        if (user != null) {
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
            tvUserActive.setText(user.isActive() ? "Activo" : "Inactivo");
            tvCreationDate.setText(user.getCreationDate().toString());

            btnEditUser.setOnClickListener(v -> editUser());
            btnDeleteUser.setOnClickListener(v -> confirmDeleteUser());
        }
    }

    private void editUser() {
        Intent intent = new Intent(this, EditUserView.class);
        intent.putExtra("user", user);
        startActivityForResult(intent, REQUEST_EDIT_USER);
    }

    // Para recibir el resultado después de editar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_USER && resultCode == RESULT_OK) {
            user = data.getParcelableExtra("user");
            updateUI();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("userUpdated", true);
            setResult(RESULT_OK, resultIntent);
        }
    }

    private void updateUI() {
        tvUserName.setText(user.getName());
        tvUserEmail.setText(user.getEmail());
        tvUserActive.setText(user.isActive() ? "Activo" : "Inactivo");
        tvCreationDate.setText(user.getCreationDate().toString());
    }

    private void confirmDeleteUser() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar usuario")
                .setMessage("¿Seguro que deseas eliminar este usuario?")
                .setPositiveButton("Sí", (dialog, which) -> deleteUser())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteUser() {
        Call<Void> call = api.deleteUser(user.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserDetailView.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("userDeleted", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(UserDetailView.this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserDetailView.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
