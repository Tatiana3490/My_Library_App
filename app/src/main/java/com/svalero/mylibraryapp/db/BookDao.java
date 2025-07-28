package com.svalero.mylibraryapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.mylibraryapp.domain.Book;

import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a la base de datos
 * Room la implementará automáticamente.
 */
@Dao
public interface BookDao {

    // Insertar un libro
    @Insert
    void insert(Book book);

    // Obtener todos los libros
    @Query("SELECT * FROM books")
    List<Book> getAllBooks();

    // Borrar todos los libros
    @Query("DELETE FROM books")
    void deleteAll();

    // Obtener solo los libros marcados como favoritos
    @Query("SELECT * FROM books WHERE isFavorite = 1")
    List<Book> getFavoriteBooks();

    // Marcar o desmarcar como favorito
    @Query("UPDATE books SET isFavorite = :favorite WHERE id = :bookId")
    void setFavorite(long bookId, boolean favorite);

    // Borrar un libro individual
    @Delete
    void delete(Book book);

    // Actualizar datos de un libro
    @Update
    void update(Book book);
}
