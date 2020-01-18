package com.minelittlepony.jumpingcastle.api;

import java.util.UUID;

import com.minelittlepony.jumpingcastle.DeserializedPayload;

public interface Server {

    void onPayload(UUID senderId, DeserializedPayload payload);

    void stopTrackingPlayer(UUID playerId);
}
