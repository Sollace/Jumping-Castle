package com.minelittlepony.jumpingcastle.payload;

import java.io.IOException;
import java.util.UUID;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

// com.mojang:authlib:1.5.25
//  - com.mojang.util.UUIDTypeAdapter
final class UUIDTypeAdapter extends TypeAdapter<UUID> {
    @Override
    public void write(final JsonWriter out, final UUID value) throws IOException {
        out.value(value.toString().replace("-", ""));
    }

    @Override
    public UUID read(final JsonReader in) throws IOException {
        return UUID.fromString(in.nextString().replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
