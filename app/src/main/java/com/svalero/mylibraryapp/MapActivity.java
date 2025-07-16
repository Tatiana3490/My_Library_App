package com.svalero.mylibraryapp;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements Style.OnStyleLoaded {

    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        // Carga el estilo y notifica en onStyleLoaded
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, this);

        // Inicializa el manager de anotaciones ANTES de que cargue el estilo
        initializePointAnnotationManager();
    }

    private void initializePointAnnotationManager() {
        // Obtiene el plugin y crea el manager de puntos
        AnnotationPlugin annotationPlugin =
                AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig config = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt
                .createPointAnnotationManager(annotationPlugin, config);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        // Aquí el estilo ya está listo: añade tus marcadores
        // Ejemplo: un marcador fijo en Zaragoza
        addMarker(40.4168, -3.7038, "Madrid");

        // O recorre tu lista de libros:
        // for (Book b : bookList) {
        //     addMarker(b.getLatitude(), b.getLongitude(), b.getTitle());
        // }

        // Y centra la cámara en ese punto (opcional)
        setCameraPosition(39.8581, -4.02263);
    }

    private void addMarker(double latitude, double longitude, String title) {
        PointAnnotationOptions options = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude))
                .withIconImage(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.red_marker))
                .withTextField(title);
        pointAnnotationManager.create(options);
    }

    private void setCameraPosition(double latitude, double longitude) {
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .zoom(15.0)
                .pitch(45.0)
                .bearing(0.0)
                .build();
        mapView.getMapboxMap().setCamera(cameraPosition);
    }
}
