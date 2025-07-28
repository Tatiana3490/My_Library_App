package com.svalero.mylibraryapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;

/**
 * Representa una categoría de libro. Puede ser pasada entre Activities (implementa Parcelable).
 */
public class BookCategory implements Parcelable {

    private long id;
    private String name;
    private String description;
    private boolean active;
    private LocalDate createdDate;   // LocalDate no es automáticamente parcelable se convierte a String para parcelar y luego se vuelve a parsear.
    private int numberBooks;

    // --- Constructor normal ---
    public BookCategory(String name, String description, boolean active,
                        LocalDate createdDate, int numberBooks) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdDate = createdDate;
        this.numberBooks = numberBooks;
    }

    // --- Constructor para leer desde Parcel ---
    protected BookCategory(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        active = in.readByte() != 0;

        String dateStr = in.readString(); // LocalDate como string (ISO 8601)
        if (dateStr != null && !dateStr.isEmpty()) {
            createdDate = LocalDate.parse(dateStr);
        } else {
            createdDate = null;
        }

        numberBooks = in.readInt();
    }

    // --- Métodos de Parcelable ---

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(createdDate != null ? createdDate.toString() : null); // ISO 8601
        dest.writeInt(numberBooks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookCategory> CREATOR = new Creator<BookCategory>() {
        @Override
        public BookCategory createFromParcel(Parcel in) {
            return new BookCategory(in);
        }

        @Override
        public BookCategory[] newArray(int size) {
            return new BookCategory[size];
        }
    };

    // --- Getters y Setters ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public int getNumberBooks() {
        return numberBooks;
    }

    public void setNumberBooks(int numberBooks) {
        this.numberBooks = numberBooks;
    }

    // --- toString() para depurar ---
    @Override
    public String toString() {
        return name + " (" + numberBooks + " libros)";
    }
}
