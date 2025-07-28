package com.svalero.mylibraryapp.adapter;

import static com.svalero.mylibraryapp.view.BookListView.REQUEST_BOOK_DETAIL;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.db.AppDatabase;
import com.svalero.mylibraryapp.db.BookDao;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.view.BookDetailView;
import com.svalero.mylibraryapp.view.BookListView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {

    private List<Book> bookList;
    private BookDao bookDao;

    public BookAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;

        AppDatabase db = Room.databaseBuilder(
                context,
                AppDatabase.class,
                "mylibraryapp-db"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        this.bookDao = db.bookDao();
    }

    @NonNull
    @Override
    public BookAdapter.BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booksview_item, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookHolder holder, int position) {
        Book book = bookList.get(position);

        holder.bookTitle.setText(book.getTitle());

        // Cambia el icono según el estado actual del favorito
        if (book.isFavorite()) {
            holder.favoriteButton.setImageResource(R.drawable.ic_star_filled);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_star_outline);
        }

        // Manejador para ir al detalle
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BookDetailView.class);
            intent.putExtra("book", book);

            if (view.getContext() instanceof BookListView) {
                ((BookListView) view.getContext()).startActivityForResult(intent, REQUEST_BOOK_DETAIL);
            } else {
                view.getContext().startActivity(intent);
            }
        });

        // Toggle favorito al pulsar la estrella
        holder.favoriteButton.setOnClickListener(v -> {
            boolean newState = !book.isFavorite();
            book.setIsFavorite(newState); // Actualiza en el objeto local
            bookDao.setFavorite(book.getId(), newState); // Actualiza en la base de datos

            // Cambia la imagen del botón
            holder.favoriteButton.setImageResource(
                    newState ? R.drawable.ic_star_filled : R.drawable.ic_star_outline
            );

            notifyItemChanged(holder.getAdapterPosition()); // Refresca solo este item
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {

        private TextView bookTitle;
        ImageButton favoriteButton;

        public BookHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.item_title);
            favoriteButton = itemView.findViewById(R.id.item_favorite_button);
        }
    }
}
