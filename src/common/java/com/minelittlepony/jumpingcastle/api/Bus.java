package com.minelittlepony.jumpingcastle.api;

import java.util.UUID;

import com.minelittlepony.jumpingcastle.JumpingServerImpl;
import com.minelittlepony.jumpingcastle.api.payload.BinaryPayload;

/**
 * Implementor for a Jumping Castle API bus.
 */
public interface Bus {
    void sendToServer(String channel, long id, Message message, Target target);

    void sendToClient(String channel, long id, Message message, UUID playerId);

    void sendToClient(UUID playerId, BinaryPayload forwarded);

    default Server getServer() {
        return JumpingServerImpl.INSTANCE;
    }
}
