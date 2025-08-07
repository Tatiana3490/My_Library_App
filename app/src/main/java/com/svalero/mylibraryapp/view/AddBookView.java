package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;
import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.contract.RegisterBookContract;
import com.svalero.mylibraryapp.domain.Author;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.domain.BookCategory;
import com.svalero.mylibraryapp.presenter.RegisterBookPresenter;
import com.svalero.mylibraryapp.util.MapUtil;

public class AddBookView extends AppCompatActivity implements RegisterBookContract.View, Style.OnStyleLoaded, OnMapClickListener {

    private RegisterBookPresenter presenter;

    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    private GesturesPlugin gesturesPlugin;
    private Point currentPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_book);

        presenter = new RegisterBookPresenter(this, getApplicationContext());

        // Iniciamos el mapa y sus funcionalidades
        initializeMapView();
        pointAnnotationManager = MapUtil.initializePointAnnotationManager(mapView);
        initializeGesturesPlugin();
    }

    public void register(View view) {
        if (currentPoint == null) {
            Toast.makeText(this, "Debes seleccionar una ubicación", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            String title = ((EditText) findViewById(R.id.title)).getText().toString().trim();
            String genre = ((EditText) findViewById(R.id.genre)).getText().toString().trim();
            String pagesStr = ((EditText) findViewById(R.id.pages)).getText().toString().trim();
            String priceStr = ((EditText) findViewById(R.id.price)).getText().toString().trim();
            String categoryIdStr = ((EditText) findViewById(R.id.bookCategory)).getText().toString().trim();
            String authorIdStr = ((EditText) findViewById(R.id.authorId)).getText().toString().trim();
            boolean available = ((CheckBox) findViewById(R.id.available)).isChecked();

            if (title.isEmpty() || genre.isEmpty() || pagesStr.isEmpty() ||
                    priceStr.isEmpty() || categoryIdStr.isEmpty() || authorIdStr.isEmpty()) {
                showErrorMessage("Por favor, completa todos los campos.");
                return;
            }

            int pages = Integer.parseInt(pagesStr);
            double price = Double.parseDouble(priceStr);
            long categoryId = Long.parseLong(categoryIdStr);
            long authorId = Long.parseLong(authorIdStr);

            if (pages <= 0) {
                showErrorMessage("El número de páginas debe ser mayor que 0.");
                return;
            }

            if (price < 0) {
                showErrorMessage("El precio no puede ser negativo.");
                return;
            }

            // Creamos objetos mínimos con ID
            BookCategory category = new BookCategory("", "", true, null, 0);
            category.setId(categoryId);

            Author author = new Author(authorId);

            Book book = new Book(title, genre, categoryId, pages, price, available,
                    currentPoint.latitude(), currentPoint.longitude(), category, author);
            book.setFavorite(false);

            presenter.registerBook(book);

        } catch (NumberFormatException e) {
            showErrorMessage("Introduce valores válidos para páginas, precio, categoría y autor.");
        }
    }


    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(findViewById(R.id.add_book_button), message, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Snackbar.make(findViewById(R.id.add_book_button), message, BaseTransientBottomBar.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("bookCreated", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void initializeMapView() {
        mapView = findViewById(R.id.registerMapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, this);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        // Aquí podrías hacer más cosas si quieres añadir capas, estilos, etc.
    }

    private void initializeGesturesPlugin() {
        gesturesPlugin = GesturesUtils.getGestures(mapView);
        gesturesPlugin.addOnMapClickListener(this);
    }

    private void addMarker(double latitude, double longitude) {
        PointAnnotationOptions marker = new PointAnnotationOptions()
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                .withPoint(Point.fromLngLat(longitude, latitude));
        pointAnnotationManager.create(marker);
    }

    @Override
    public boolean onMapClick(@NonNull Point point) {
        pointAnnotationManager.deleteAll();
        currentPoint = point;
        addMarker(point.latitude(), point.longitude());
        return true;
    }
}
