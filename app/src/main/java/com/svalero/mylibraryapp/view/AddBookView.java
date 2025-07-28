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
import com.svalero.mylibraryapp.domain.Book;
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

    // Método que se llama al pulsar el botón "Añadir"
    public void register(View view) {
        if (currentPoint == null) {
            Toast.makeText(this, "Debes seleccionar una ubicación", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            // Obtenemos los valores del formulario
            String title = ((EditText) findViewById(R.id.title)).getText().toString();
            String genre = ((EditText) findViewById(R.id.genre)).getText().toString();
            int pages = Integer.parseInt(((EditText) findViewById(R.id.pages)).getText().toString());
            double price = Double.parseDouble(((EditText) findViewById(R.id.price)).getText().toString());
            int categoryId = Integer.parseInt(((EditText) findViewById(R.id.bookCategory)).getText().toString());
            boolean available = ((CheckBox) findViewById(R.id.available)).isChecked();

            // Creamos el objeto Book
            Book book = new Book(title, genre, categoryId, pages, price, available, currentPoint.latitude(), currentPoint.longitude());
            book.setIsFavorite(false);

            // Lo enviamos al presentador para que se guarde
            presenter.registerBook(book);

        } catch (NumberFormatException nfe) {
            showErrorMessage("Por favor, revisa los valores introducidos.");
        }
    }

    // Mostrar mensaje de error
    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(findViewById(R.id.add_book_button), message, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
    }

    // Mostrar mensaje de éxito y cerrar la actividad
    @Override
    public void showSuccessMessage(String message) {
        Snackbar.make(findViewById(R.id.add_book_button), message, BaseTransientBottomBar.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("bookCreated", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    // Inicializamos el MapView con el estilo por defecto
    private void initializeMapView() {
        mapView = findViewById(R.id.registerMapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, this);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        // Aquí podrías hacer más cosas si quieres añadir capas, estilos, etc.
    }

    // Activamos los gestos del mapa (clic, arrastre, zoom, etc.)
    private void initializeGesturesPlugin() {
        gesturesPlugin = GesturesUtils.getGestures(mapView);
        gesturesPlugin.addOnMapClickListener(this);
    }

    // Añadir marcador en el mapa
    private void addMarker(double latitude, double longitude) {
        PointAnnotationOptions marker = new PointAnnotationOptions()
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                .withPoint(Point.fromLngLat(longitude, latitude));
        pointAnnotationManager.create(marker);
    }

    // Se ejecuta al hacer clic en el mapa
    @Override
    public boolean onMapClick(@NonNull Point point) {
        pointAnnotationManager.deleteAll(); // Limpiamos marcadores previos
        currentPoint = point; // Guardamos la nueva ubicación
        addMarker(point.latitude(), point.longitude()); // Añadimos marcador
        return true;
    }
}
