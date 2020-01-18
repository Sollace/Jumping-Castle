package com.minelittlepony.jumpingcastle.sponge;

import org.spongepowered.api.network.ChannelBuf;

import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;

public interface ChannelBufBinaryPayload extends BinaryPayload {

    static BinaryPayload of(ChannelBuf buff) {
        BinaryPayload payload = BinaryPayload.of(buff);

        if (payload != null) {
            return payload;
        }

        return (ChannelBufBinaryPayload)(() -> buff);
    }

    @SuppressWarnings("unchecked")
    ChannelBuf buff();

    @Override
    default String readString() {
        return buff().readString();
    }

    @Override
    default byte readByte() {
        return buff().readByte();
    }

    @Override
    default BinaryPayload writeByte(byte b) {
        buff().writeByte(b);
        return this;
    }

    @Override
    default byte[] bytes() {
        return buff().array();
    }

    @Override
    default byte[] readToEnd() {
        return buff().readBytes(buff().available());
    }

    @Override
    default long readLong() {
        return buff().readLong();
    }

    @Override
    default BinaryPayload writeLong(long l) {
        buff().writeLong(l);
        return this;
    }

    @Override
    default BinaryPayload reverse() {
        buff().setReadIndex(0);
        return this;
    }

    @Override
    default BinaryPayload writeString(String s) {
        buff().writeString(s);
        return this;
    }

    @Override
    default byte[] readBytes(int len) {
        return buff().readByteArray(len);
    }

    @Override
    default BinaryPayload writeBytes(byte[] bytes) {
        buff().writeBytes(bytes);
        return this;
    }

    @Override
    default int readInt() {
        return buff().readInteger();
    }

    @Override
    default BinaryPayload writeInt(int b) {
        buff().writeInteger(b);
        return this;
    }
}
