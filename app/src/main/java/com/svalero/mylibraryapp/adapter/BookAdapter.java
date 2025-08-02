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
     * Constructor que recibe la lista de libros y el contexto.
     * También inicializa la base de datos local usando Room.
     */
    public BookAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;


        AppDatabase db = Room.databaseBuilder(
                        context,
                        AppDatabase.class,
                        "mylibraryapp-db"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries() //se usa sabiendo que son pocos datos los que se almacenan
                .build();

        this.bookDao = db.bookDao();
    }

    /**
     * Crea e infla el layout de cada ítem del RecyclerView.
     */
    @NonNull
    @Override
    public BookAdapter.BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booksview_item, parent, false);
        return new BookHolder(view);
    }

    /**
     * Asigna los datos de un libro a cada vista (ítem) del RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookHolder holder, int position) {
        Book book = bookList.get(position);

        // Muestra el título del libro
        holder.bookTitle.setText(book.getTitle());

        // Establece el icono de favorito
        holder.favoriteButton.setImageResource(
                book.isFavorite() ? R.drawable.ic_star_filled : R.drawable.ic_star_outline
        );

        // Acción al hacer click en el ítem completo: abrir la vista de detalle
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BookDetailView.class);
            intent.putExtra("book", book); // Book es Parcelable

            // Si venimos desde BookListView, lanzamos con startActivityForResult
            if (view.getContext() instanceof BookListView) {
                ((BookListView) view.getContext()).startActivityForResult(intent, REQUEST_BOOK_DETAIL);
            } else {
                view.getContext().startActivity(intent);
            }
        });

        // Acción al pulsar el botón de favorito (estrella)
        holder.favoriteButton.setOnClickListener(v -> {
            boolean newState = !book.isFavorite(); // Invertimos el estado
            book.setFavorite(newState); // Actualizamos en memoria
            bookDao.setFavorite(book.getId(), newState); // Actualizamos en base de datos

            // Actualizamos el icono visualmente
            holder.favoriteButton.setImageResource(
                    newState ? R.drawable.ic_star_filled : R.drawable.ic_star_outline
            );

            // También actualizamos el contentDescription para accesibilidad
            holder.favoriteButton.setContentDescription(
                    newState ? "Desmarcar como favorito" : "Marcar como favorito"
            );
        });
    }

    /**
     * Devuelve el número de ítems a mostrar en el RecyclerView.
     */
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    /**
     * Permite actualizar la lista de libros desde fuera del adapter.
     */
    public void updateBooks(List<Book> newBookList) {
        this.bookList = newBookList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder: contiene las referencias a los elementos visuales de cada ítem.
     */
    public static class BookHolder extends RecyclerView.ViewHolder {
        TextView bookTitle;
        ImageButton favoriteButton;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.item_title);
            favoriteButton = itemView.findViewById(R.id.item_favorite_button);
        }
    }
}
