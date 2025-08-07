package com.svalero.mylibraryapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {
    private long id;

    public Author(long id) {
        this.id = id;
    }

    protected Author(Parcel in) {
        id = in.readLong();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
    }
}
