package com.minelittlepony.jumpingcastle.payload;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

public interface ByteBufBinaryPayload extends IBinaryPayload {

    static IBinaryPayload of(ByteBuf buff) {
        return (ByteBufBinaryPayload)(() -> buff);
    }

    @SuppressWarnings("unchecked")
    ByteBuf buff();

    @Override
    default String readString() {
        return buff().readCharSequence(readInt(), StandardCharsets.UTF_8).toString();
    }

    @Override
    default byte readByte() {
        return buff().readByte();
    }

    @Override
    default IBinaryPayload writeByte(byte b) {
        buff().writeByte(b);
        return this;
    }

    @Override
    default long readLong() {
        return buff().readLong();
    }

    @Override
    default IBinaryPayload writeLong(long l) {
        buff().writeLong(l);
        return this;
    }

    @Override
    default IBinaryPayload reverse() {
        buff().readerIndex(0);
        return this;
    }

    @Override
    default IBinaryPayload writeString(String s) {
        buff().writeInt(s.getBytes().length);
        buff().writeCharSequence(s, StandardCharsets.UTF_8);
        return this;
    }
}
