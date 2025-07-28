package com.svalero.mylibraryapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.svalero.mylibraryapp.domain.Book;

/**
 * Clase que representa la base de datos local de la aplicación.
 * Room generará automáticamente su implementación en tiempo de compilación.
 */
@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Método abstracto para acceder al DAO
    public abstract BookDao bookDao();

    // Instancia única (singleton)
    private static volatile AppDatabase INSTANCE;

    /**
     * Devuelve la instancia de la base de datos. La crea si no existe aún.
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "books_database"
                            )
                            .fallbackToDestructiveMigration() // borra la BD si cambia la estructura
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
