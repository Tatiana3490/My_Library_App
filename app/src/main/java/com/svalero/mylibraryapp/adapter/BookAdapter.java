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

    /**
     * Constructor: recibe lista de libros y contexto para acceder a la base de datos local.
     */
    public BookAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;

        // Inicializa Room DB para poder actualizar campo favorito
        AppDatabase db = Room.databaseBuilder(
                        context,
                        AppDatabase.class,
                        "mylibraryapp-db"
                ).fallbackToDestructiveMigration()
                .allowMainThreadQueries() // ⚠️ cuidado: esto solo es aceptable si el dataset es pequeño
                .build();

        this.bookDao = db.bookDao();
    }

    /**
     * Crea un nuevo `ViewHolder` inflando el layout de cada item.
     */
    @NonNull
    @Override
    public BookAdapter.BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booksview_item, parent, false);
        return new BookHolder(view);
    }

    /**
     * Asigna los valores a los elementos visuales de cada item.
     */
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookHolder holder, int position) {
        Book book = bookList.get(position);

        // Muestra el título
        holder.bookTitle.setText(book.getTitle());

        // Cambia la imagen del botón según favorito
        holder.favoriteButton.setImageResource(
                book.isFavorite() ? R.drawable.ic_star_filled : R.drawable.ic_star_outline
        );

        // Abre detalle al pulsar cualquier parte del item
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BookDetailView.class);
            intent.putExtra("book", book); // book es Parcelable

            // Si estás en BookListView, usa startActivityForResult
            if (view.getContext() instanceof BookListView) {
                ((BookListView) view.getContext()).startActivityForResult(intent, REQUEST_BOOK_DETAIL);
            } else {
                view.getContext().startActivity(intent);
            }
        });

        // Al pulsar la estrella se cambia el estado de favorito
        holder.favoriteButton.setOnClickListener(v -> {
            boolean newState = !book.isFavorite();
            book.setFavorite(newState); // Cambia el estado en memoria
            bookDao.setFavorite(book.getId(), newState); // Y en Room

            // Actualiza la estrella visualmente
            holder.favoriteButton.setImageResource(
                    newState ? R.drawable.ic_star_filled : R.drawable.ic_star_outline
            );

            notifyItemChanged(holder.getAdapterPosition()); // Refresca el item
        });
    }

    /**
     * Devuelve el número de libros a mostrar.
     */
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    /**
     * ViewHolder para los elementos del RecyclerView.
     */
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
