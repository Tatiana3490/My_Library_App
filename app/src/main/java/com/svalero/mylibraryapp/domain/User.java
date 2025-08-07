package com.svalero.mylibraryapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;

/**
 * Representa un usuario en la aplicación.
 * Se implementa Parcelable para poder pasar objetos entre Activities.
 */
public class User implements Parcelable {

    private long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String creationDate; // Vamos a usar LocalDate en TODO
    private boolean active;

    /**
     * Constructor usado para crear un usuario desde cero.
     */
    public User(String name, String username, String password, String email, String creationDate, boolean active) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.creationDate = creationDate;
        this.active = active;
    }

    /**
     * Constructor usado por Parcelable al recibir datos desde un Parcel.
     */
    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        username = in.readString();
        password = in.readString();
        email = in.readString();
        String dateString = in.readString(); // Recuperamos fecha como string
        creationDate = String.valueOf(LocalDate.parse(dateString));
        active = in.readByte() != 0; // 1 = true, 0 = false
    }

    /**
     * Parcelable: Este objeto permite que Android "desempaque" un User desde un Parcel.
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcelable: escribe los atributos del objeto User en un Parcel.
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(creationDate.toString()); // Guardamos fecha como texto ISO (aaaa-MM-dd)
        dest.writeByte((byte) (active ? 1 : 0));
    }

    // --- Getters y Setters: con LocalDate ya, no java.util.Date ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
