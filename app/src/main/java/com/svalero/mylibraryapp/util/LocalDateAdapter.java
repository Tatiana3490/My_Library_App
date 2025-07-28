package com.svalero.mylibraryapp.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Adaptador personalizado para que Gson pueda serializar/deserializar LocalDate.
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value != null) {
            out.value(value.toString()); // Formato ISO: "2024-11-02"
        } else {
            out.nullValue();
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        String date = in.nextString();
        return (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
    }
}
