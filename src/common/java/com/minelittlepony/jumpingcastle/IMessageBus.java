package com.minelittlepony.jumpingcastle;

import java.util.UUID;

import com.minelittlepony.jumpingcastle.api.IMessage;
import com.minelittlepony.jumpingcastle.api.Target;
import com.minelittlepony.jumpingcastle.payload.IBinaryPayload;

public interface IMessageBus {
    void sendToServer(String channel, long id, IMessage message, Target target);

    void sendToClient(String channel, long id, IMessage message, UUID playerId);

    void sendToClient(UUID playerId, IBinaryPayload forwarded);
}
