package com.minelittlepony.jumpingcastle.payload;

import com.minelittlepony.jumpingcastle.api.Target;

public class DeserializedPayload {

    public final byte protocol;

    public final String channel;

    public final long objectType;

    public final Target target;

    public final IBinaryPayload payload;

    public DeserializedPayload(IBinaryPayload payload) {
        this.payload = payload;

        protocol = payload.readByte();
        channel = payload.readString();
        objectType = payload.readLong();
        target = Target.values()[payload.readByte()];
    }

    public DeserializedPayload(byte proto, IBinaryPayload payload) {
        this.payload = payload;

        protocol = proto;
        channel = payload.readString();
        objectType = payload.readLong();
        target = Target.values()[payload.readByte()];
    }
}
