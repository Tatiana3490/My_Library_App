package com.svalero.mylibraryapp.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.adapter.BookAdapter;
import com.svalero.mylibraryapp.db.AppDatabase;
import com.svalero.mylibraryapp.db.BookDao;
import com.svalero.mylibraryapp.domain.Book;

import java.util.List;

public class FavouriteBookListView extends AppCompatActivity  {

    private BookAdapter adapter;
    private List<Book> favoriteBooks;
    private BookDao bookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book_list_view);

        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "mylibraryapp-db"
        ).allowMainThreadQueries().build();

        bookDao= db.bookDao();
        favoriteBooks = bookDao.getFavoriteBooks();

        RecyclerView recyclerView = findViewById(R.id.favorite_book_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookAdapter(favoriteBooks, this);
        recyclerView.setAdapter(adapter);
    }
}
