package com.minelittlepony.jumpingcastle.payload;

import com.google.gson.Gson;
import com.minelittlepony.jumpingcastle.api.IMessage;

import io.netty.buffer.ByteBuf;

public interface IBinaryPayload {

    Gson json = new Gson();

    static IBinaryPayload of(Object buffer) {
        if (buffer instanceof ByteBuf) {
            return new ByteBufBinaryPayload((ByteBuf)buffer);
        }
        return null;
    }

    long readLong();

    byte readByte();

    String readString();

    IBinaryPayload writeString(String s);

    default <T extends IMessage> T readBinary(Class<T> type) {
        return json.fromJson(readString(), type);
    }

    default <T extends IMessage> IBinaryPayload writeBinary(T message) {
        return writeString(json.toJson(message));
    }

    default public int readInt() {
        int i = 0;
        int j = 0;

        while (true)
        {
            byte b0 = readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }

            if ((b0 & 128) != 128) {
                break;
            }
        }

        return i;
    }
}
