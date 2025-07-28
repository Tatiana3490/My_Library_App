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

import java.util.ArrayList;

public class MapBookView extends AppCompatActivity implements Style.OnStyleLoaded{

    private ArrayList<Book> bookList;
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    private Button btnZoomIn, btnZoomOut;
    private double zoomLevel = 14.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_book);

        Intent intent = getIntent();
        bookList = intent.getParcelableArrayListExtra("bookList");

        mapView = findViewById(R.id.mapView);
        btnZoomIn = findViewById(R.id.btn_zoom_in);
        btnZoomOut = findViewById(R.id.btn_zoom_out);

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, this);
        pointAnnotationManager = MapUtil.initializePointAnnotationManager(mapView);

        // Configurar botones de zoom
        btnZoomIn.setOnClickListener(v -> {
            zoomLevel += 1.0;
            updateZoom();
        });

        btnZoomOut.setOnClickListener(v -> {
            zoomLevel -= 1.0;
            updateZoom();
        });
    }
    private void updateZoom() {
        mapView.getMapboxMap().setCamera(new CameraOptions.Builder()
                .zoom(zoomLevel)
                .build());
    }

    private void viewBooks() {
        if (bookList == null || bookList.isEmpty()) {
            Log.e("MapBookView", "La lista de libros está vacía o es null");
            return;
        }
        for ( Book book : bookList) {
            addMarker(book.getTitle(), book.getLatitude(), book.getLongitude());
        }
    }

    private void addMarker(String message, double latitude, double longitude) {
        PointAnnotationOptions marker = new PointAnnotationOptions()
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker))
                .withTextField(message)
                .withPoint(Point.fromLngLat(longitude, latitude));
        pointAnnotationManager.create(marker);
    }

    public void registerBook (View view) {
        Intent intent = new Intent(this, AddBookView.class);
        startActivity(intent);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        viewBooks();
    }
}
