package com.minelittlepony.jumpingcastle;

import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;

public class DeserializedPayload {

    public final byte protocol;

    public final String channel;

    public final long objectType;

    public final Target target;

    public final BinaryPayload payload;

    public DeserializedPayload(BinaryPayload payload) {
        this.payload = payload;

        protocol = payload.readByte();
        channel = payload.readString();
        objectType = payload.readLong();
        target = Target.values()[payload.readByte()];
    }

    public DeserializedPayload(byte proto, BinaryPayload payload) {
        this.payload = payload;

        protocol = proto;
        channel = payload.readString();
        objectType = payload.readLong();
        target = Target.values()[payload.readByte()];
    }
}
