package com.minelittlepony.jumpingcastle.api.payload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.Nullable;

import com.minelittlepony.jumpingcastle.api.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public interface BinaryPayload {

    @Nullable
    static BinaryPayload of(Object buffer) {
        if (buffer instanceof ByteBuf) {
            return ByteBufBinaryPayload.of((ByteBuf)buffer);
        }
        return null;
    }

    static BinaryPayload create() {
        return of(Unpooled.buffer());
    }

    <T> T buff();

    byte[] bytes();

    byte[] readToEnd();

    long readLong();

    BinaryPayload writeLong(long l);

    int readInt();

    BinaryPayload writeInt(int b);

    byte readByte();

    BinaryPayload writeByte(byte b);

    String readString();

    BinaryPayload writeString(String s);

    byte[] readBytes(int len);

    BinaryPayload writeBytes(byte[] bytes);

    BinaryPayload reverse();

    @SuppressWarnings("unchecked")
    default <T> T readBinary(Class<T> type) {
        try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(readBytes(readInt())))) {
            return (T)stream.readObject();
        } catch (IOException | ClassNotFoundException ignored) {}

        return null;
    }

    default BinaryPayload writeBinary(Message message) {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            ObjectOutputStream stream = new ObjectOutputStream(bytes);

            stream.writeObject(message);

            byte[] data = bytes.toByteArray();
            writeInt(data.length);
            writeBytes(data);
        } catch (IOException ignored) {}

        return this;
    }
}
