package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.adapter.BookAdapter;
import com.svalero.mylibraryapp.contract.BookListContract;
import com.svalero.mylibraryapp.db.AppDatabase;
import com.svalero.mylibraryapp.db.BookDao;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.presenter.BookListPresenter;

import java.util.ArrayList;
import java.util.List;

public class BookListView extends AppCompatActivity implements BookListContract.View {

    private BookAdapter bookAdapter; // Adapter para mostrar libros
    private AppDatabase db;          // Base de datos Room
    private BookDao bookDao;         // DAO (Data Access Object)
    private ArrayList<Book> bookList;// Lista en memoria que alimenta el RecyclerView
    private BookListContract.Presenter presenter; // El cerebro que controla esta View
    private FloatingActionButton fabViewUsers;    // Botón para ir a la pantalla de usuarios
    public static final int REQUEST_BOOK_DETAIL = 3; // Constante para resultados de intents



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_view);

        presenter = new BookListPresenter(this,  getApplicationContext());

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "mylibraryapp-db"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        bookDao = db.bookDao();

        // Se prepara la lista visual
        bookList = new ArrayList<>();
        RecyclerView booksView = findViewById(R.id.books_view);
        booksView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        booksView.setLayoutManager(linearLayoutManager);


        bookAdapter = new BookAdapter(bookList,this);
        booksView.setAdapter(bookAdapter);

        // Botón flotante que te lleva a la vista de usuarios
        fabViewUsers = findViewById(R.id.fab_view_users);
        fabViewUsers.setOnClickListener(v -> {
            Intent intent = new Intent(BookListView.this, UserListView.class);
            startActivity(intent);
        });
    }

    // Cada vez que se vuelve a la pantalla
    @Override
    protected void onResume() {
        super.onResume();
        reloadBooksFromRoom();

    }

    @Override
    public void showOfflineMessage() {
        Toast.makeText(this, "Estás viendo datos en modo sin conexión", Toast.LENGTH_LONG).show();
    }


    // Para crear los botones de la action_bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);
        return true;
    }

    //Cuando alguien seleccione unos de los botones de la action_bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_map) {
            Intent intent = new Intent(this, MapBookView.class);
            intent.putParcelableArrayListExtra("bookList", bookList);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_register_book) {
            Intent intent = new Intent(this, AddBookView.class);
            startActivityForResult(intent, 1);
        }else if (item.getItemId() == R.id.action_view_favorites) {
            Intent intent = new Intent(this, FavoriteBookListView.class);
            startActivity(intent);
        }

        return true;
    }

    public void registerBook (View view) {
        Intent intent = new Intent(this, AddBookView.class);
        startActivityForResult(intent, 1);
    }

    /* Carga los libros desde la base de datos local (Room). Se llama cuando vuelves
     de otras pantallas o después de añadir/editar libros.*/
    private void reloadBooksFromRoom() {
        bookList.clear();
        bookList.addAll(bookDao.getAllBooks());
        bookAdapter.notifyDataSetChanged();
    }

    // Cuando vuelves de añadir o editar un libro, actualizas la lista.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            boolean bookUpdated = data.getBooleanExtra("bookUpdated", false);

            if (requestCode == 1 || requestCode == 2) {
                reloadBooksFromRoom();
            }

        }

    }


    @Override
    public void listBooks(List<Book> bookList) {
        this.bookList.clear();
        this.bookList.addAll(bookList);
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

    }
}
