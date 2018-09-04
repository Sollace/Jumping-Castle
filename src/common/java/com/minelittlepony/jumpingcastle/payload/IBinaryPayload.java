package com.minelittlepony.jumpingcastle.payload;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minelittlepony.jumpingcastle.api.IMessage;

import io.netty.buffer.ByteBuf;

public interface IBinaryPayload {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    static IBinaryPayload of(Object buffer) {
        if (buffer instanceof ByteBuf) {
            return ByteBufBinaryPayload.of((ByteBuf)buffer);
        }
        return null;
    }

    <T> T buff();

    byte[] bytes();

    byte[] readToEnd();

    long readLong();

    IBinaryPayload writeLong(long l);

    byte readByte();

    IBinaryPayload writeByte(byte b);

    String readString();

    IBinaryPayload writeString(String s);

    IBinaryPayload writeBytes(byte[] bytes);

    IBinaryPayload reverse();

    default <T> T readBinary(Class<T> type) {
        return gson.fromJson(readString(), type);
    }

    default IBinaryPayload writeBinary(IMessage message) {
        return writeString(gson.toJson(message));
    }

    int readInt();
}
