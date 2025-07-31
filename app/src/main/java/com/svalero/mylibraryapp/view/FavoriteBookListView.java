package com.svalero.mylibraryapp.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.adapter.BookAdapter;
import com.svalero.mylibraryapp.db.AppDatabase;
import com.svalero.mylibraryapp.db.BookDao;
import com.svalero.mylibraryapp.domain.Book;

import java.util.List;

/**
 * Muestra una lista de libros favoritos almacenados en la base de datos local.
 */
public class FavoriteBookListView extends AppCompatActivity {

    private BookAdapter adapter;
    private List<Book> favoriteBooks;
    private BookDao bookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book_list_view);

        // Inicializa la base de datos y DAO en el hilo principal
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "mylibraryapp-db"
        ).allowMainThreadQueries().build();

        bookDao = db.bookDao();

        // Obtiene libros favoritos
        favoriteBooks = bookDao.getFavoriteBooks();

        // Configura el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.favorite_book_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookAdapter(favoriteBooks, this);
        recyclerView.setAdapter(adapter);
    }
}
