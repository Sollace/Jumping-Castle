package com.minelittlepony.jumpingcastle.sponge;

import org.spongepowered.api.network.ChannelBuf;

import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

public interface ChannelBufBinaryPayload extends IBinaryPayload {

    static IBinaryPayload of(ChannelBuf buff) {
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

    default byte[] bytes() {
        return buff().array();
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
}
