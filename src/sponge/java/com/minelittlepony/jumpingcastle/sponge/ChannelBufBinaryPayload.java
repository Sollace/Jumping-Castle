package com.minelittlepony.jumpingcastle.sponge;

import org.spongepowered.api.network.ChannelBuf;

import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

public interface ChannelBufBinaryPayload extends IBinaryPayload {

    static IBinaryPayload of(ChannelBuf buff) {
        IBinaryPayload payload = IBinaryPayload.of(buff);

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
    default IBinaryPayload writeByte(byte b) {
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
    default IBinaryPayload writeLong(long l) {
        buff().writeLong(l);
        return this;
    }

    @Override
    default IBinaryPayload reverse() {
        buff().setReadIndex(0);
        return this;
    }

    @Override
    default IBinaryPayload writeString(String s) {
        buff().writeString(s);
        return this;
    }

    @Override
    default IBinaryPayload writeBytes(byte[] bytes) {
        buff().writeBytes(bytes);
        return this;
    }

    @Override
    default int readInt() {
        return buff().readInteger();
    }
}
