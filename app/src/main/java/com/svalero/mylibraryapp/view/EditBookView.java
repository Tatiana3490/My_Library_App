package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.api.BooksApi;
import com.svalero.mylibraryapp.api.BooksApiInterface;
import com.svalero.mylibraryapp.domain.Book;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBookView  extends AppCompatActivity {

    private EditText etTitle, etGenre;
    private Button btnSave;
    private Book book;
    private BooksApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_view);

        etTitle = findViewById(R.id.et_title);
        etGenre = findViewById(R.id.et_genre);
        btnSave = findViewById(R.id.btn_save_book);
        api = BooksApi.buildInstance();

        book= getIntent().getParcelableExtra("book");

        if (book != null) {
            etTitle.setText(book.getTitle());
            etGenre.setText(book.getGenre());

            btnSave.setOnClickListener(v -> saveBook());
        }
    }

    private void saveBook() {
        book.setTitle(etTitle.getText().toString());
        book.setGenre(etGenre.getText().toString());

        Call<Book> call = api.updateBook(book.getId(), book);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditBookView.this, "Libro actualizado", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("book", book);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditBookView.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(EditBookView.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
