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

    private BookAdapter bookAdapter;
    private AppDatabase db;
    private BookDao bookDao;
    private ArrayList<Book> bookList; // 💡 Declaración aquí, inicialización más abajo
    private BookListContract.Presenter presenter;
    private FloatingActionButton fabViewUsers;

    public static final int REQUEST_BOOK_DETAIL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_view);

        presenter = new BookListPresenter(this, getApplicationContext());

        // Inicializa la base de datos
        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "mylibraryapp-db"
        ).allowMainThreadQueries().build();
        bookDao = db.bookDao();

        //Inicializa la lista para que no sea null (era tu error)
        bookList = new ArrayList<>();

        // Si la base de datos está vacía, añade libros de prueba
        if (bookDao.getAllBooks().isEmpty()) {
            Book book1 = new Book("1984", "Ciencia Ficción", 1, 328, 19.99, false, 40.4168, -3.7038);
            Book book2 = new Book("Fahrenheit 451", "Distopía", 2, 249, 10.50, true, 48.8566, 2.3522);
            bookDao.insert(book1);
            bookDao.insert(book2);
        }

        // Inicializa el RecyclerView y su adaptador
        RecyclerView booksView = findViewById(R.id.books_view);
        booksView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(bookList, this);
        booksView.setAdapter(bookAdapter);

        // Botón flotante para ir a la vista de usuarios
        fabViewUsers = findViewById(R.id.fab_view_users);
        fabViewUsers.setOnClickListener(v -> {
            startActivity(new Intent(this, UserListView.class));
        });

        // Carga inicial de libros
        reloadBooksFromRoom();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadBooksFromRoom();
    }

    @Override
    public void showOfflineMessage() {
        Toast.makeText(this, "Estás viendo datos en modo sin conexión", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_map) {
            Intent mapIntent = new Intent(this, MapBookView.class);
            mapIntent.putParcelableArrayListExtra("bookList", bookList);
            startActivity(mapIntent);
            return true;
        } else if (itemId == R.id.action_register_book) {
            startActivityForResult(new Intent(this, AddBookView.class), 1);
            return true;
        } else if (itemId == R.id.action_view_favorites) {
            startActivity(new Intent(this, FavoriteBookListView.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void registerBook(View view) {
        startActivityForResult(new Intent(this, AddBookView.class), 1);
    }

    private void reloadBooksFromRoom() {
        bookList.clear(); // ← aquí te explotaba antes porque `bookList` era null
        bookList.addAll(bookDao.getAllBooks());
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == 1 || requestCode == 2)) {
            reloadBooksFromRoom();
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
