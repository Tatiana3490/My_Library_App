package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.api.BooksApi;
import com.svalero.mylibraryapp.api.BooksApiInterface;
import com.svalero.mylibraryapp.db.AppDatabase;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.util.MapUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailView extends AppCompatActivity {

    private TextView title, genre, pages, bookCoordinates;
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    private Button btnEdit, btnDelete;
    private Book book;
    private BooksApiInterface api;


    private static final int REQUEST_EDIT_BOOK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail_view);

        // Inicialización de vistas
        title = findViewById(R.id.title);
        genre = findViewById(R.id.genre);
        pages = findViewById(R.id.pages);
        bookCoordinates = findViewById(R.id.book_coordinates);
        mapView = findViewById(R.id.mapView);
        btnEdit = findViewById(R.id.btn_edit_book);
        btnDelete = findViewById(R.id.btn_delete_book);

        // Instancia de la API
        api = BooksApi.buildInstance();

        // Recogemos el libro enviado desde la vista anterior
        book = getIntent().getParcelableExtra("book");

        if (book != null) {
            title.setText(book.getTitle());
            genre.setText("Género: " + book.getGenre());
            pages.setText("Páginas: " + book.getPages());
            bookCoordinates.setText("Lat: " + book.getLatitude() + ", Lon: " + book.getLongitude());

            // Carga el mapa y centra la cámara en la ubicación del libro
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {
                pointAnnotationManager = MapUtil.initializePointAnnotationManager(mapView);

                mapView.getMapboxMap().setCamera(
                        new CameraOptions.Builder()
                                .center(Point.fromLngLat(book.getLongitude(), book.getLatitude()))
                                .zoom(11.0)
                                .build()
                );

                // Añade marcador con el título del libro
                addMarker(book.getTitle(), book.getLatitude(), book.getLongitude());
            });

            btnEdit.setOnClickListener(v -> editBook());
            btnDelete.setOnClickListener(v -> confirmDelete());
        }
    }

    private void addMarker(String title, double latitude, double longitude) {
        PointAnnotationOptions marker = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude))
                .withTextField(title);
        pointAnnotationManager.create(marker);
    }

    private void editBook() {
        Intent intent = new Intent(this, EditBookView.class);
        intent.putExtra("book", book);
        startActivityForResult(intent, REQUEST_EDIT_BOOK);
    }

    // Recoge el resultado tras editar el libro
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_BOOK && resultCode == RESULT_OK) {
            book = data.getParcelableExtra("book");
            updateUI();

            // Guarda cambios en Room
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mylibraryapp-db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
            db.bookDao().update(book);

            // Devuelve la info al `BookListView`
            Intent resultIntent = new Intent();
            resultIntent.putExtra("bookUpdated", true);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private void updateUI() {
        title.setText(book.getTitle());
        genre.setText("Género: " + book.getGenre());
        pages.setText("Páginas: " + book.getPages());
        bookCoordinates.setText("Lat: " + book.getLatitude() + ", Lon: " + book.getLongitude());
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar libro")
                .setMessage("¿Estás seguro de que quieres eliminar este libro?")
                .setPositiveButton("Sí", (dialog, which) -> deleteBook())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteBook() {
        Call<Void> call = api.deleteBook(book.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Eliminamos también en Room
                    AppDatabase db = Room.databaseBuilder(
                                    getApplicationContext(), AppDatabase.class, "mylibraryapp-db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                    db.bookDao().delete(book);

                    Toast.makeText(BookDetailView.this, "Libro eliminado", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("bookUpdated", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(BookDetailView.this, "Error al eliminar el libro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BookDetailView.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
