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

    private List<User> userList;


    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getName());
        holder.userEmail.setText(user.getEmail());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), UserDetailView.class);
            intent.putExtra("user", user);
            //view.getContext().startActivity(intent);
            ((UserListView) view.getContext()).startActivityForResult(intent, REQUEST_USER_DETAIL);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private TextView userEmail;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.item_userName);
            userEmail = itemView.findViewById(R.id.item_userEmail);
        }
    }
}
