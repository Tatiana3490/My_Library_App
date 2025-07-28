package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.adapter.UserAdapter;
import com.svalero.mylibraryapp.api.BooksApi;
import com.svalero.mylibraryapp.api.BooksApiInterface;
import com.svalero.mylibraryapp.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private BooksApiInterface api;
    private FloatingActionButton fabAddUser;
    private static final int REQUEST_ADD_USER = 1;
    public static final int REQUEST_USER_DETAIL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_view);

        recyclerView = findViewById(R.id.recycler_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddUser = findViewById(R.id.fab_add_user);
        fabAddUser.setOnClickListener(v -> {
            Log.d("DEBUG", "Botón de agregar usuario presionado");

            Intent intent = new Intent(UserListView.this, AddUserView.class);
            startActivity(intent);
        });

        api = BooksApi.buildInstance();
        fetchUsers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("DEBUG", "onActivityResult() llamado en UserListView");

        if (resultCode == RESULT_OK) {
            Log.d("DEBUG", "RESULT_OK recibido");

            if (requestCode == REQUEST_ADD_USER) {
                Log.d("DEBUG", "Resultado de AddUserView recibido");

                boolean userAdded = data.getBooleanExtra("userAdded", false);
                if (userAdded) {
                    Log.d("DEBUG", "Usuario agregado, actualizando lista...");
                    fetchUsers();
                } else {
                    Log.d("DEBUG", "No se agregó usuario, no se actualiza la lista.");
                }
            } else if (requestCode == REQUEST_USER_DETAIL) {
                boolean userUpdated = data.getBooleanExtra("userUpdated", false);
                if (userUpdated) {
                    fetchUsers();
                }
            }
        } else {
            Log.d("DEBUG", "RESULT_OK no recibido en UserListView");

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DEBUG", "onResume() llamado en UserListView, recargando usuarios...");
        fetchUsers();
    }


    private void fetchUsers() {
        Log.d("DEBUG", "fetchUsers() llamado, actualizando lista...");

        Call<List<User>> call = api.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    adapter = new UserAdapter(userList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(UserListView.this, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("USER_LIST", "Error de conexión: " + t.getMessage());
                Toast.makeText(UserListView.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
