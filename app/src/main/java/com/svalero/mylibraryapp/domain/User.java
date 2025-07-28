package com.svalero.mylibraryapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

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
    private Date creationDate;
    private boolean active;

    /**
     * Constructor usado para crear un usuario desde cero.
     */
    public User(String name, String username, String password, String email, Date creationDate, boolean active) {
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
        creationDate = (Date) in.readSerializable(); // Sí, esto funciona. Pero sí, también es un poco feo.
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

    // Parcelable: describeContents() casi siempre devuelve 0.
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
        dest.writeSerializable(creationDate); // Sí, Date es Serializable, así que esto cuela
        dest.writeByte((byte) (active ? 1 : 0));
    }

    // --- Getters y Setters: lo que Android Studio genera para ganarse el sueldo ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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
