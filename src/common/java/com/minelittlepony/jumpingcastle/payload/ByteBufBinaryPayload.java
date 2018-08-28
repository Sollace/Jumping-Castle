package com.minelittlepony.jumpingcastle.payload;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

public final class ByteBufBinaryPayload implements IBinaryPayload {

    private final ByteBuf buff;

    public ByteBufBinaryPayload(ByteBuf buff) {
        this.buff = buff;
    }

    @Override
    public String readString() {
        return buff.readCharSequence(readInt(), StandardCharsets.UTF_8).toString();
    }

    @Override
    public byte readByte() {
        return buff.readByte();
    }

    @Override
    public long readLong() {
        return buff.readLong();
    }

    @Override
    public IBinaryPayload writeString(String s) {
        buff.writeInt(s.getBytes().length);
        buff.writeCharSequence(s, StandardCharsets.UTF_8);
        return this;
    }
}
