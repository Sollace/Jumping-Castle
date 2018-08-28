package com.minelittlepony.jumpingcastle.payload;

import com.minelittlepony.jumpingcastle.api.Target;

public final class DeserializedPayload {

    public final String channel;

    public final long objectType;

    public final Target target;

    public final IBinaryPayload payload;

    public DeserializedPayload(IBinaryPayload payload) {
        this.payload = payload;

        channel = payload.readString();
        objectType = payload.readLong();
        target = Target.values()[payload.readByte()];
    }
}
