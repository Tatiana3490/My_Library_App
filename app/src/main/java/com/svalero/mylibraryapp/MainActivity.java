package com.svalero.mylibraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.mylibraryapp.adapter.BookAdapter;
import com.svalero.mylibraryapp.domain.Book;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private BookAdapter bookAdapter;
    public static List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateList();

        RecyclerView booksView = findViewById(R.id.books_view);
        booksView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        booksView.setLayoutManager(linearLayoutManager);

        bookAdapter = new BookAdapter(bookList);
        booksView.setAdapter(bookAdapter);
    }

    //Cuando alguien haga clic en los botones del menú de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    //Cuando alguien seleccione un item del menu opciones
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            startActivity(new Intent(this, MapActivity.class));
            return true;
        } else if (id == R.id.action_register_book) {
            startActivity(new Intent(this, RegisterBook.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Boton para registrar libros
    public void registerBook(View view){
        Intent intent = new Intent(this, RegisterBook.class);
        startActivity(intent);
    }

    //Para probar
    private void populateList(){
        bookList = new ArrayList<>();
        bookList.add(new Book("La vida de Tatiana", "Tatiana Alcubilla", "Drama", 250, 29.99,39.8581, -4.02263 ));
        bookList.add(new Book("El señor de los Anillos", "J.R.R. Tolkien", "Fantasía épica", 1200, 35.50,39.8581, -4.02284 ));
        bookList.add(new Book("Alicia en el País de las Maravillas", "Lewis Carrol", "Fantasía", 192, 10.00,39.8581, -4.02212 ));
        bookList.add(new Book("1984", "George Orwell", "Política", 400, 12.00,39.8581, -4.02255 ));
    }
}