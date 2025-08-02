package com.svalero.mylibraryapp.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.util.MapUtil;

import java.util.List;

/**
 * Activity que muestra un mapa con marcadores para cada libro de la lista.
 * Utiliza Mapbox para visualizar la información geolocalizada de los libros.
 */
public class MapBookView extends AppCompatActivity implements Style.OnStyleLoaded {

    private List<Book> bookList; // Lista de libros recibida por intent
    private MapView mapView; // Vista del mapa
    private PointAnnotationManager annotationManager; // Gestor de marcadores
    private Button zoomInButton, zoomOutButton; // Botones de zoom
    private double zoomLevel = 14.0; // Nivel de zoom inicial

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_book);

        initializeUI();            // Vincula los elementos visuales
        retrieveBookList();       // Obtiene los libros del intent

        // Carga el estilo del mapa y aplica los marcadores cuando esté listo
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, this);

        // Inicializa el gestor de anotaciones para poner marcadores
        annotationManager = MapUtil.initializePointAnnotationManager(mapView);

        setupZoomControls(); // Asigna listeners a los botones de zoom
    }

    /**
     * Inicializa referencias a las vistas del layout.
     */
    private void initializeUI() {
        mapView = findViewById(R.id.mapView);
        zoomInButton = findViewById(R.id.btn_zoom_in);
        zoomOutButton = findViewById(R.id.btn_zoom_out);
    }

    /**
     * Extrae la lista de libros enviada desde otra Activity.
     */
    private void retrieveBookList() {
        Intent intent = getIntent();
        bookList = intent.getParcelableArrayListExtra("bookList");
    }

    /**
     * Configura la lógica de zoom para los botones + y -.
     */
    private void setupZoomControls() {
        zoomInButton.setOnClickListener(v -> changeZoom(1));
        zoomOutButton.setOnClickListener(v -> changeZoom(-1));
    }

    /**
     * Cambia el nivel de zoom del mapa.
     *
     * @param delta el valor a incrementar o decrementar
     */
    private void changeZoom(double delta) {
        zoomLevel += delta;
        mapView.getMapboxMap().setCamera(
                new CameraOptions.Builder()
                        .zoom(zoomLevel)
                        .build()
        );
    }

    /**
     * Añade marcadores al mapa para todos los libros disponibles.
     */
    private void displayBookMarkers() {
        if (bookList == null || bookList.isEmpty()) {
            Log.w("MapBookView", "No books to display on the map.");
            return;
        }

        for (Book book : bookList) {
            addMarkerForBook(book);
        }
    }

    /**
     * Añade un marcador individual al mapa para un libro.
     * Ignora libros sin coordenadas válidas.
     *
     * @param book el libro del que se desea mostrar la ubicación
     */
    private void addMarkerForBook(Book book) {
        // Opcional: evita mostrar libros sin ubicación definida
        if (book.getLatitude() == 0 && book.getLongitude() == 0) {
            Log.d("MapBookView", "Book '" + book.getTitle() + "' has no coordinates.");
            return;
        }

        PointAnnotationOptions options = new PointAnnotationOptions()
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                .withTextField(book.getTitle())
                .withPoint(Point.fromLngLat(book.getLongitude(), book.getLatitude()));

        annotationManager.create(options);
    }

    /**
     * Abre la pantalla para registrar un nuevo libro.
     *
     * @param view botón pulsado (desde XML)
     */
    public void registerBook(View view) {
        Intent intent = new Intent(this, AddBookView.class);
        startActivity(intent);
    }

    /**
     * Método llamado cuando el estilo del mapa está completamente cargado.
     * Se utiliza para colocar los marcadores después de que el mapa está listo.
     */
    @Override
    public void onStyleLoaded(@NonNull Style style) {
        displayBookMarkers();
    }
}
