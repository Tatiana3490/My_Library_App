package com.svalero.mylibraryapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


/**
 * Representa un libro que se almacena en Room y se puede pasar entre Activities (Parcelable).
 */

@Entity(tableName = "books")
public class Book implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String genre;
    private long categoryId;
    private int pages;
    private double price;
    private boolean available;
    private double latitude;
    private double longitude;
    private boolean favorite;

    // Constructor principal para usar al crear un libro
    public Book(String title, String genre, long categoryId, int pages, double price,
                boolean available, double latitude, double longitude) {
        this.title = title;
        this.genre = genre;
        this.categoryId = categoryId;
        this.pages = pages;
        this.price = price;
        this.available = available;
        this.latitude = latitude;
        this.longitude = longitude;
        this.favorite = false; // por defecto no favorito
    }

    // Constructor vacío por si Room o Gson lo necesitan
    @Ignore
    public Book() {}

    // Constructor que lee desde un Parcel
    protected Book(Parcel in) {
        id = in.readLong();
        title = in.readString();
        genre = in.readString();
        categoryId = in.readLong();
        pages = in.readInt();
        price = in.readDouble();
        available = in.readByte() != 0;
        latitude = in.readDouble();
        longitude = in.readDouble();
        favorite = in.readByte() != 0;
    }

    // Parcelable: se necesita para enviar libros entre Activities
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // --- Getters & Setters clásicos de Java ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    // --- Métodos requeridos por Parcelable ---

    @Override
    public int describeContents() {
        return 0; // No hay contenido especial
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(genre);
        dest.writeLong(categoryId);
        dest.writeInt(pages);
        dest.writeDouble(price);
        dest.writeByte((byte) (available ? 1 : 0));
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    // --- Para debugging/logs bonitos ---
    @Override
    public String toString() {
        return title + " (" + genre + ") - " + price + "€";
    }
}
