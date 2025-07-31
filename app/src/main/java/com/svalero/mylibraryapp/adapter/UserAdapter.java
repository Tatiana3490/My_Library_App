package com.svalero.mylibraryapp.adapter;

import static com.svalero.mylibraryapp.view.UserListView.REQUEST_USER_DETAIL;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.domain.User;
import com.svalero.mylibraryapp.view.UserDetailView;
import com.svalero.mylibraryapp.view.UserListView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private final List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout de cada usuario
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        // Obtiene el usuario actual
        User user = userList.get(position);

        // Asigna nombre y correo
        holder.userName.setText(user.getName());
        holder.userEmail.setText(user.getEmail());

        // Al hacer clic, lanza la vista de detalles del usuario
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), UserDetailView.class);
            intent.putExtra("user", user);

            // Asegúrate de que el contexto es la activity esperada
            if (view.getContext() instanceof UserListView) {
                ((UserListView) view.getContext()).startActivityForResult(intent, REQUEST_USER_DETAIL);
            } else {
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder para mostrar cada ítem del usuario
    public static class UserHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userEmail;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.item_userName);
            userEmail = itemView.findViewById(R.id.item_userEmail);
        }
    }
}
